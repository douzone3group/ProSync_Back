package com.douzone.prosync.file.repository;

import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

    void saveUserProfileImage(File file);

    void saveFileInfo(FileInfo fileInfo);

}
