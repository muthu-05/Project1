package com.mbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity (name="filemetadata")
@Table(name = "filemetadata")
public class FileMetaData {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String title;

  private String url;

  private String owner;

  public Long getId() {
    return id;
  }

  public FileMetaData setId(Long id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public FileMetaData setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public FileMetaData setUrl(String url) {
    this.url = url;
    return this;
  }

  public String getOwner() {
    return owner;
  }

  public FileMetaData setOwner(String owner) {
    this.owner = owner;
    return this;
  }
}