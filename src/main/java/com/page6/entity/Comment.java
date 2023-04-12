package com.page6.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "comments")
@Entity
public class Comment {

    @Id
    @Column(name="COMMENT_PK", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // 댓글 인덱스

    @ManyToOne
    @JoinColumn(name = "BOARD_FK")
    private Board board;    // 작성 게시글

//    @ManyToOne
//    @JoinColumn(name = "MEMBER_FK")
//    private Member member; // 작성자

    @Column(name="COMMENT_CONTENT", columnDefinition = "TEXT", nullable = false)
    private String comment; // 댓글 내용

    @Column(name="COMMENT_GROUP")
    private int group; // 댓글 계층 (null이면 그냥 댓글. 부모 댓글 인덱스가 있다면 대댓글)

    @CreationTimestamp
    @Column(name="COMMENT_REGDT", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regdate;
}
