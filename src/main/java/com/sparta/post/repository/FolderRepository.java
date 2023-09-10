package com.sparta.post.repository;

import com.sparta.post.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder,Long> {

    Folder findByFolderNumber(Long folderNumber);
}
