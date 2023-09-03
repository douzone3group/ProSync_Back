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

    @Select("SELECT * FROM file WHERE file_id=#{fileId}")
    Optional<File> findById(Long fileId);

    List<FileResponseDto> findFilesByTableInfo(FileRequestDto fileInfo);

    // file info
    void saveFileInfo(FileInfo fileInfo);

    void saveFileInfoList(List<FileInfo> fileInfoList);

    @Select("SELECT * FROM file_info WHERE file_info_id = #{fileInfoId} AND deleted_at IS NULL")
    Optional<FileInfo> findFileInfo(Long fileInfoId);

    @Update("UPDATE file_info SET deleted_at = now() WHERE file_info_id = #{fileInfoId}")
    Integer deleteFileInfo(Long fileInfoId);

    @Update("UPDATE file_info SET deleted_at = now() WHERE table_name = #{tableName} AND table_key = #{tableKey}")
    void deleteFileInfos(FileRequestDto dto);

    @Select("SELECT * FROM file_info WHERE file_id = #{fileId}")
    List<FileInfo> findFileInfoByFileId(Long fileId);

}
