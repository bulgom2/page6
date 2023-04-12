package com.page6.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="MEMBER_TB")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name = "MEMBER_PK", nullable = false, updatable = false, insertable = false)
    private String id;                // 회원 인덱스

    @Column(name = "MEMBER_NM", nullable = false, length = 15)
    private String name;              // 이름

//    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
//    private LocalDateTime m_regdate;    // 가입일자
//
//    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
//    private LocalDateTime m_updatedate; // 수정일자

}
