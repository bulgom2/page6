package com.page6.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name="TAG_MAP_TB")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagMap {
    @Id
    @Column(name="TAG_MAP_PK", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // TAG_MAP 인덱스

    @ManyToOne
    @JoinColumn(name = "BOARD_FK", nullable = false)
    private Board board;    // 작성 게시글

    @ManyToOne
    @JoinColumn(name = "TAG_FK", nullable = false)
    private Tag tag;        // 작성 게시글

    @CreationTimestamp
    @Column(name="TAG_REGDT", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regdate;  //등록일

}
