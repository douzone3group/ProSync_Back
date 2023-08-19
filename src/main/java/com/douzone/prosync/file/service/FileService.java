package com.douzone.prosync.file.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.file.FileDto;
import com.douzone.prosync.file.basic.BasicImage;
import com.douzone.prosync.file.entity.FileInfo;
import com.douzone.prosync.file.repository.FileMapper;
import com.douzone.prosync.member.dto.response.MemberGetResponse;
import com.douzone.prosync.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3Client;

    private final MemberService memberService;

    private final FileMapper fileMapper;

    /**
     * 회원 프로필 이미지 저장/수정
     */
    public String uploadUserProfileImage(MultipartFile multipartFile, Long memberId) throws IOException {

        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.split("\\.")[1];

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        fileName += "-" + date.format(new Date());

        // user profile image - jpg, jpeg, png, gif 만 허용
        if (!List.of("jpg, jpeg, png, gif").contains(ext)) {
            throw new ApplicationException(ErrorCode.INVALID_FILE_TYPE);
        }

        MemberGetResponse member = memberService.selectMember(memberId);

        deleteUserProfileImage(member.getProfileImage());

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, convert(multipartFile)).withCannedAcl(CannedAccessControlList.PublicRead));

        String path = amazonS3Client.getUrl(bucket, fileName).toString();

        // TODO: member profile image는 여기서 처리한 후 member service에서 PATH 받아서 업데이트 처리?

        return path;
    }

    /**
     * 기존 이미지가 기본이미지가 아닌 경우 해당 이미지를 S3에서 지움
     */
    private void deleteUserProfileImage(String userProfileImage) {
        if (!"".equals(userProfileImage) && userProfileImage != null) {
            if (!userProfileImage.equals(BasicImage.BASIC_USER_IMAGE.getPath())) {
                amazonS3Client.deleteObject(bucket, userProfileImage);
            }
        }
    }


    /**
     * 업무 파일 저장
     */
    public List<FileDto> uploadTaskFile(List<MultipartFile> multipartFiles, Long taskId) {

        return multipartFiles.stream().map((multipartFile) -> {

                String fileName = multipartFile.getOriginalFilename();

                SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
                fileName += "-" + date.format(new Date());

                File file = null;
                try {
                        file = convert(multipartFile);
                    System.out.println(file.getName());
                    //TODO : S3 저장 확인
                    amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));

                } catch (SdkClientException | IOException e) {
                    e.getStackTrace();
                }
                String path = amazonS3Client.getUrl(bucket, fileName).toString();
                System.out.println("path : " + path);

                // save file
                com.douzone.prosync.file.entity.File createdFile = com.douzone.prosync.file.entity.File.create(multipartFile.getSize(), path, fileName);
                fileMapper.saveUserProfileImage(createdFile);

                // save file_info
                FileInfo fileInfo = FileInfo.create("task", taskId, createdFile.getFileId());
                fileMapper.saveFileInfo(fileInfo);
                return (FileDto.response(createdFile));

        }).collect(Collectors.toList());
    }




    // MultipartFile to file
    private File convert(MultipartFile file) throws  IOException {

        if (Optional.ofNullable(file).isEmpty()) {
            throw new ApplicationException(ErrorCode.FILE_NOT_FOUND);
        }
        File convertFile = new File(file.getOriginalFilename());
        file.transferTo(convertFile);

        return convertFile;
    }

}
