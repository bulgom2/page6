package com.page6.repository;


import com.page6.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

   // Member findByEmail(String email);


    Optional<Member> findByEmail(String email);
}