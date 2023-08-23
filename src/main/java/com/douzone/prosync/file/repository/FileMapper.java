package com.douzone.prosync.file.repository;

import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper {

    void save(File file);

    void saveFileInfo(FileInfo fileInfo);

    // soft delete
    void delete(Long fileId);

    List<File> findFiles(@Param("tableName") String target, @Param("tableKey") Long targetId);

    @Delete("DELETE FROM file WHERE file_id=#{fileId} AND is_deleted IS NULL")
    int deleteFile(Long fileId);

    @Delete("DELETE FROM file_info WHERE file_id=#{fileId}")
    Integer deleteFileInfo(Long fileId);
}
