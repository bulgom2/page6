package com.page6.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="FILE_TB")
@Getter
@Setter
@NoArgsConstructor
public class BoardFile {
    @Id
    @Column(name="FILE_PK", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        //파일 인덱스

    @Column(name="FILE_NAME", nullable = false)
    private String fileName; //파일명

    @Column(name="ORI_FILE_NAME", nullable = false)
    private String oriFileName; //원본 파일명

    @Column(name="FILE_PATH", nullable = false)
    private String filePath;    //파일 조회 경로(책에서 imgUrl)

    @ManyToOne
    @JoinColumn(name = "BOARD_FK", nullable = false)
    private Board board;    // 작성 게시글

    public void updateFile(String oriFileName, String fileName, String filePath, Board board) {
        this.oriFileName = oriFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.board = board;
    }
}
