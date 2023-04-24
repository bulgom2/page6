package com.page6.repository;

import com.page6.entity.DeleteLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeleteLogRepository extends JpaRepository<DeleteLog, Long>  {

    //삭제글 이력
    @Override
    //Page<DeleteLog> findAll(Pageable pageable);
    List<DeleteLog> findAll();
}
