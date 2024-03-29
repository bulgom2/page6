package com.page6.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="DELETE_TB")
@Getter
@Setter
@NoArgsConstructor
public class DeleteLog {
    @Id
    @Column(name="DELETED_PK", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        //삭제 인덱스

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOARD_FK")
    private Board board;  //삭제 게시물 인덱스

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER_FK")
    private Member member;  //삭제자 인덱스

    @Column(name="DELETED_FL", columnDefinition = "bit(1) default 0")
    private boolean flag;   //삭제인지 복구인지 여부. 삭제면 flase, 복구면 true

    @CreationTimestamp
    @Column(name="DELETED_REGDT", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regdate;

    public DeleteLog(Board board, Member member, boolean flag) {
        this.board = board;
        this.member = member;
        this.flag = flag;
    }
}
