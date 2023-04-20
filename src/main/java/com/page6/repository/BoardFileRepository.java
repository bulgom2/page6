package com.page6.repository;

import com.page6.entity.Board;
import com.page6.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile,Long> {
    List<BoardFile> findAllByBoard(Board board);
}
