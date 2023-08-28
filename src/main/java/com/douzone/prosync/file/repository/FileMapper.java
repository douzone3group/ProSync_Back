package com.douzone.prosync.file.repository;

import com.douzone.prosync.file.dto.FileResponseDto;
import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    void save(File file);

    void saveFileList(List<File> fileList);

    @Select("select * from file where file_id=#{fileId}")
    File findById(Long fileId);

    // soft delete
    Integer delete(Long fileId);

    @Delete("UPDATE file SET is_deleted = true WHERE file_id IN #{fileIdList}")
    void deleteAll(List<Long> fileIdList);

    List<FileResponseDto> findFilesByTableInfo(@Param("tableName") String tableName, @Param("tableKey") Long tableKey);

    List<File> findByIdList(List<Long> fileIds);

    // file info
    void saveFileInfo(FileInfo fileInfo);

    void saveFileInfoList(List<FileInfo> fileInfoList);

    @Delete("DELETE FROM file_info WHERE file_id=#{fileId}")
    Integer deleteFileInfo(Long fileId);

}
