package com.douzone.prosync.file.repository;

import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    void save(File file);
    @Insert("INSERT INTO file VALUES ( #{fileList} )")
    void saveFileList(List<File> fileList);

    @Select("select * from file where file_id=#{fileId}")
    File findById(Long fileId);

    @Select("select * from file where file_id in #{fileList}")
    List<File> findByIdList(List<Long> fileId);

    void saveFileInfo(FileInfo fileInfo);

    @Insert("INSERT INTO file_info values (#{fileInfoList})")
    void saveFileInfoList(List<FileInfo> fileInfoList);

    // soft delete
    void delete(Long fileId);

    @Delete("UPDATE file SET is_deleted = true where file_id in #{fileIdList}")
    void deleteAll(List<Long> fileIdList);

    List<File> findFiles(@Param("tableName") String target, @Param("tableKey") Long targetId);

    @Delete("DELETE FROM file WHERE file_id=#{fileId} AND is_deleted IS NULL")
    int deleteFile(Long fileId);

    @Delete("DELETE FROM file_info WHERE file_id=#{fileId}")
    Integer deleteFileInfo(Long fileId);
}
