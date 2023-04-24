package com.page6.repository;

import com.page6.entity.Deleted;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedRepository  extends JpaRepository<Deleted, Long>  {

}
