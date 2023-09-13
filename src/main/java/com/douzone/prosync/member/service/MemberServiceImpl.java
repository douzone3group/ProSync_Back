package com.douzone.prosync.member.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.file.basic.BasicImage;
import com.douzone.prosync.file.dto.FileRequestDto;
import com.douzone.prosync.file.dto.FileResponseDto;
import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import com.douzone.prosync.file.service.FileService;
import com.douzone.prosync.mail.dto.CertificationCodeDto;
import com.douzone.prosync.mail.dto.MailDto;
import com.douzone.prosync.mail.service.AuthenticateService;
import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.dto.request.MemberLoginDto;
import com.douzone.prosync.member.dto.request.MemberPatchPasswordDto;
import com.douzone.prosync.member.dto.request.MemberPatchProfileDto;
import com.douzone.prosync.member.dto.request.MemberPostDto;
import com.douzone.prosync.member.dto.response.MemberGetResponse;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.member_project.service.MemberProjectService;
import com.douzone.prosync.redis.RedisService;
import com.douzone.prosync.security.jwt.HmacAndBase64;
import com.douzone.prosync.security.jwt.RefreshTokenProvider;
import com.douzone.prosync.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.douzone.prosync.constant.ConstantPool.AUTHORIZATION_HEADER;
import static com.douzone.prosync.constant.ConstantPool.REFRESH_HEADER;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{


    private final MemberRepository memberRepository;

    private final AuthenticateService authenticateService;

    private final RedisService redisService;

    private final PasswordEncoder passwordEncoder;

    private final HmacAndBase64 hmacAndBase64;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

    private final RefreshTokenProvider refreshTokenProvider;

    private final MemberProjectService memberProjectService;

    private final FileService fileService;


    /**
     * 회원가입 로직
     */
    public Member signup(MemberPostDto memberDto) {
        // 중복검사
        if (duplicateInspection(memberDto.getEmail())) {
            throw new ApplicationException(ErrorCode.DUPLICATED_USER_ID);
        }
        MemberDto member = memberDto.of(passwordEncoder.encode(memberDto.getPassword()));
        return memberRepository.save(member);
    }


    /**
     * Member pk로 조회하기
     */
    @Transactional(readOnly = true)
    public MemberGetResponse selectMember(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() ->new ApplicationException(ErrorCode.USER_NOT_FOUND));

            return MemberGetResponse.of(member);

    }

    /**
     * 프로필 수정
     */
    public void updateMemberProfile(Long memberId, MemberPatchProfileDto dto){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 프로젝트 이미지 - fileId 값이 있는 경우
        if (dto.getFileId() != null) {

            // 회원 이미지 세팅
            File file = fileService.findFile(dto.getFileId());
            fileService.saveFileInfo(FileInfo.createFileInfo(FileInfo.FileTableName.MEMBER, memberId, file.getFileId()));
            dto.setProfileImage(file.getPath());

            // 기본이미지 아닐 경우 기존 file 삭제
            if (!member.getProfileImage().equals(BasicImage.BASIC_USER_IMAGE.getPath())) {
                FileRequestDto profileImage = FileRequestDto.create(FileInfo.FileTableName.MEMBER, memberId);
                FileResponseDto findProfileFile = fileService.findFilesByTableInfo(profileImage, false).get(0);
                fileService.delete(findProfileFile.getFileInfoId());
            }

        } else {
            dto.setProfileImage(member.getProfileImage());
        }

        memberRepository.updateProfile(memberId, dto);
    }
    /**
     * 패스워드 수정
     */
    public void updateMemberPassword(Long memberId, MemberPatchPasswordDto dto){
        memberRepository.findById(memberId).orElseThrow(()->new ApplicationException(ErrorCode.USER_NOT_FOUND));
        memberRepository.updatePassword(memberId, passwordEncoder.encode(dto.getPassword()));
    }

    /**
     * 회원 탈퇴 처리
     */
    public void updateMemberDelete(Long memberId, HttpServletRequest request){
        memberRepository.findById(memberId).orElseThrow(()->new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 회원이 가입했던 프로젝트들 탈퇴하는 과정
        List<Long> projectIds = memberProjectService.findProjectIdsByMemberId(memberId);

        for (int i=0;i<projectIds.size();i++) {

            memberProjectService.exitProjectMember(projectIds.get(i),memberId);

            //Todo: 회원이 탈퇴하였다고 알려주기(본인이 나간거지만 탈퇴했다고 알려주기)
        }

        memberRepository.updateDeleted(memberId);

        try {
            // Refresh 토큰을 Redis에서 제거하는 작업
            redisService.removeRefreshToken("refresh:" + hmacAndBase64.crypt(request.getRemoteAddr(), "HmacSHA512") + "_" + memberId);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ApplicationException(ErrorCode.CRYPT_ERROR);
        }

        fileService.deleteFileList(FileRequestDto.create(FileInfo.FileTableName.MEMBER, memberId));

    }

    /**
     * Email로 Member 중복검사하기
     */
    public boolean duplicateInspection(String email) {
        return !(memberRepository.findByEmail(email).orElse(null)==null);
    }


    /**
     * 사용자의 이메일을 받아 인증번호를 전송하고 Redis에 인증번호를 저장하는 로직
     */
    @Override
    public void invalidateInspectionAndSend(MailDto mail) {
        // email이 DB에 등록되어 있는지 확인한다.
        if (duplicateInspection(mail.getEmail())) {
            throw new ApplicationException(ErrorCode.DUPLICATED_USER_ID);
        }

        String number = authenticateService.sendForAuthenticate(mail.getEmail());

        // Redis에 key값은 "email: 사용자 email" 형태로 인증번호 저장
        redisService.setEmailCertificationNumber("email:"+mail.getEmail(),number);

    }


    /**
     * 사용자에게 이메일과 인증번호를 받아 Redis에 있는지 확인하는 로직
     */
    @Override
    public void verifyCertificationNumber(CertificationCodeDto code) {
        String number = redisService.getEmailCertificationNumber("email:"+code.getEmail());

        if (number==null || !number.equals(code.getCertificationNumber())) {
            throw new ApplicationException(ErrorCode.CERTIFICATION_NUMBER_MISMATCH);
        }
        log.debug("{}","인증번호 통과, 인증번호를 지웁니다.");
        redisService.removeEmailCertificationNumber("email:"+code.getEmail());

    }

    @Override
    public String loginProcess(MemberLoginDto loginDto, HttpHeaders httpHeaders,HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        // authenticationToken을 이용해서 Authentication 객체를 생성하려고 authentication 메소드가 실행이 될 때
        // CustomUserDetailsService의 loadUserByUsername 메소드가 실행된다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);




        // createToken 메소드를 통해서 JWT Token을 생성한다.
        String jwt = tokenProvider.createToken(authentication);
        String refresh = refreshTokenProvider.createToken(authentication, ipAddress);

        httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwt);
        httpHeaders.add(REFRESH_HEADER, "Bearer " + refresh);

        try {
            // "refresh:암호화된IP_pk"을 key값으로 refreshToken을 Redis에 저장한다.
            redisService.setRefreshToken("refresh:" + hmacAndBase64.crypt(ipAddress, "HmacSHA512")
                    + "_" + authentication.getName(), refresh);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ApplicationException(ErrorCode.CRYPT_ERROR);
        }

        return authentication.getName();

    }
}