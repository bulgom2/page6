package com.page6.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="HEART_TB")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Heart {
    @Id
    @Column(name="HEART_PK", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;        //좋아요 인덱스

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="MEMBER_FK")
    private Member member;    //댓글 작성자 id

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="BOARD_FK")
    private Board board;    //게시글 id

    public Heart(Board board, Member member) {
        this.member = member;
        this.board = board;
    }
}