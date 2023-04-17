package com.page6.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="TAG_TB")
@Getter
@Setter
@NoArgsConstructor
public class Tag {

    @Id
    @Column(name="TAG_PK", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //해시태그 인덱스

    @Column(name="TAG_NAME", nullable = false)
    private String name; //태그 내용
}
