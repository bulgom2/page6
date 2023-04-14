package com.page6.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comment_tb")
@Entity
public class Comment {

    @Id
    @Column(name="COMMENT_PK", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // 댓글 인덱스

    @ManyToOne
    @JoinColumn(name = "BOARD_FK", nullable = false)
    private Board board;    // 작성 게시글

    @ManyToOne
    @JoinColumn(name = "MEMBER_FK", nullable = false)
    private Member writer; // 작성자

    @Column(name="COMMENT_CONTENT", columnDefinition = "TEXT", nullable = false)
    private String content; // 댓글 내용

    @CreationTimestamp
    @Column(name="COMMENT_REGDT", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regdate;  //등록일

    @Column(name="COMMENT_DEPTH")
    private int depth;  //깊이 (0이면 일반 댓, 그 이상이면 대댓)

    @Column(name="COMMENT_GROUP")
    private long group;  //댓글 그룹


//    @ManyToOne
//    @JoinColumn(name = "COMMENT_PARENT")
//    private Comment parent; // 댓글 계층 (null이면 그냥 댓글. 부모 댓글 인덱스가 있다면 대댓글)
//
//    @Builder.Default
//    @OneToMany(mappedBy = "parent", orphanRemoval = true)
//    private List<Comment> children = new ArrayList<>();

}
