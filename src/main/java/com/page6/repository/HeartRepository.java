package com.page6.repository;


import com.page6.entity.Board;
import com.page6.entity.Heart;
import com.page6.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByBoardAndMember(Board board, Member member);
}
