package com.page6.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="BOARD_TB")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Board {
    @Id
    @Column(name="BOARD_PK", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;        //게시글 인덱스

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_WRITER")
    private Member member;  //작성자

    @Column(name="BOARD_TITLE", nullable = false)
    private String title;   //제목

    @Column(name="BOARD_CONTENT", nullable = false, columnDefinition ="TEXT")
    private String content;   //본문

    @Column(name="BOARD_LIKE_COUNT", nullable = false, columnDefinition = "integer default 0")
    private int like_cnt;

    @Column(name="BOARD_VIEW_COUNT", nullable = false, columnDefinition = "integer default 0")
    private int view_cnt;

    @Column(name="BOARD_DELFL", nullable = false, columnDefinition = "bit(1) default 0")
    private boolean delete_flag;

    @CreationTimestamp
    @Column(name="BOARD_REGDT", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regdate;

//    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    @OrderBy("id asc")
//    private List<Comment> comments;

    @Builder
    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
/*
@Entity
@Table(name="BOARD_TB")
@Getter
@Setter
@ToString
public class Board {
    @Id
    @Column(name="BOARD_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;        //게시글 인덱스

//    @Column(name="BOARD_WRITER", nullable = false)
//    private long writer;    //작성자 인덱스

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_WRITER")
    private Member member;

    @Column(name="BOARD_TITLE", nullable = false)
    private String title;   //제목

    @Column(name="BOARD_CONTENT", nullable = false, columnDefinition ="TEXT")
    private String content;   //본문

    @Column(name="BOARD_LIKE_COUNT", nullable = false, columnDefinition = "integer default 0")
    private int like_count;

    @Column(name="BOARD_VIEW_COUNT", nullable = false)
    private int view_count;

    @Column(name="BOARD_DELFL", nullable = false)
    private boolean delete_flag;

    @CreationTimestamp
    @Column(name="BOARD_REGDT", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regdate;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private List<Comment> comments;

}

 */