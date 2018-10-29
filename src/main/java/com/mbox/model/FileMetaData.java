package com.mbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date")
  private Date createDate;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modify_date")
  private Date modifyDate;

  private String title;

  private String description;

  private String url;

  private String owner;

  private long size;

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

  public Date getCreateDate() {
    return createDate;
  }

  public FileMetaData setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getModifyDate() {
    return modifyDate;
  }

  public FileMetaData setModifyDate(Date modifyDate) {
    this.modifyDate = modifyDate;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public FileMetaData setDescription(String description) {
    this.description = description;
    return this;
  }

  public long getSize() {
    return size;
  }

  public FileMetaData setSize(long size) {
    this.size = size;
    return this;
  }
}