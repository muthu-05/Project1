package com.mbox.persistence;

import com.mbox.model.FileMetaData;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FileMetaDataRepository extends CrudRepository<FileMetaData, Long> {
  @Query("SELECT e FROM filemetadata e WHERE e.owner = ?1 and e.id=?2")
  public FileMetaData findByIdForOwner(String owner, Long id);

  @Query("SELECT e FROM filemetadata e WHERE e.id=?1")
  public FileMetaData findByItemId(Long id);

  @Query("SELECT e FROM filemetadata e WHERE e.owner = ?1 and e.title=?2")
  public FileMetaData findByTitleForOwner(String owner, String title);

  @Query("SELECT e FROM filemetadata e WHERE e.title=?1")
  public FileMetaData findByTitle(String title);

  @Query("SELECT e FROM filemetadata e WHERE e.owner = ?1")
  public List<FileMetaData> findAllForOwner(String owner);

  @Query("SELECT e FROM filemetadata e")
  public List<FileMetaData> findAll();

}