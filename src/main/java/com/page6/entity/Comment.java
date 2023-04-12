package com.page6.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="COMMENT_TB")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COMMENT_NO", nullable = false, updatable = false, insertable = false)
    private Long id;    //댓글 인덱스

    @ManyToOne
    @JoinColumn(name="BOARD_FK")
    private Board board;    //게시글

    @ManyToOne
    @JoinColumn(name="MEMBER_FK")
    private Member member;  //작성자

//    @Column(name="BOARD_FK", nullable = false)
//    private Long board_no;  //작성 게시글 인덱스
//
//    @Column(name="MEMBER_FK", nullable = false)
//    private String writer;  //작성자 인덱스

    @Column(name="COMMENT_CONTENT", nullable = false)
    private String content; //댓글 내용

    @Column(name="COMMENT_DEPTH", nullable = false)
    private int depth;  //댓글 순서

    @CreationTimestamp
    @Column(name="COMMENT_REGDT", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regdate;  //생성일자

}
