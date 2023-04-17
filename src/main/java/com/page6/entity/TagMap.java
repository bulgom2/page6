package com.page6.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="TAG_MAP_TB")
@Getter
@Setter
@NoArgsConstructor
public class TagMap {
    @Id
    @Column(name="TAG_MAP_PK", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BOARD_FK", nullable = false)
    private Board board;    // 작성 게시글

    @ManyToOne
    @JoinColumn(name = "TAG_FK", nullable = false)
    private Tag tag;    // 작성 게시글
}
