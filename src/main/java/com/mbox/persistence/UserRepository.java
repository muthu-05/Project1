package com.mbox.persistence;

import com.mbox.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
  @Query("SELECT e FROM User e WHERE e.email LIKE ?1")
  public List<User> findByEmail(String email);
}