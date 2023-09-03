package com.douzone.prosync.file.repository;

import com.douzone.prosync.file.dto.FileRequestDto;
import com.douzone.prosync.file.dto.FileResponseDto;
import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {

    void save(File file);

    void saveFileList(List<File> fileList);

    @Select("SELECT * FROM file WHERE file_id=#{fileId} AND is_deleted IS NULL")
    Optional<File> findById(Long fileId);

    @Delete("UPDATE file SET is_deleted = true WHERE file_id = #{fileId}")
    void deleteFile(Long fileId);

    void deleteFiles(FileRequestDto fileInfo);

    List<FileResponseDto> findFilesByTableInfo(FileRequestDto fileInfo);

    // file info
    void saveFileInfo(FileInfo fileInfo);

    void saveFileInfoList(List<FileInfo> fileInfoList);

    @Select("SELECT * FROM file_info WHERE file_info_id = #{fileInfoId}")
    Optional<FileInfo> findFileInfo(Long fileInfoId);

    @Delete("DELETE FROM file_info WHERE file_info_id = #{fileInfoId}")
    Integer deleteFileInfo(Long fileInfoId);

    @Delete("DELETE FROM file_info WHERE table_name = #{tableName} AND table_key = #{tableKey}")
    void deleteFileInfos(FileRequestDto dto);

    @Select("SELECT * FROM file_info WHERE file_id = #{fileId}")
    List<FileInfo> findFileInfoByFileId(Long fileId);

}
