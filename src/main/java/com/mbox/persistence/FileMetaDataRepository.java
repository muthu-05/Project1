package com.mbox.persistence;

import com.mbox.model.FileMetaData;
import org.springframework.data.repository.CrudRepository;

public interface FileMetaDataRepository extends CrudRepository<FileMetaData, Long> {

}