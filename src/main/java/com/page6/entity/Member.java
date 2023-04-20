package com.page6.entity;

import com.page6.dto.MemberFormDto;
import com.page6.members.Role;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Entity
@Table(name="member")
@Getter
@Setter
@ToString
@NoArgsConstructor

public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //연결된 db의 넘버링 전략 따라감
    @Column(name = "member_id")
    private Long id;    // 시퀀스, auto_increment

    private String name;              // 닉네임

    private String password;          // 비밀번호

    @Column(unique = true)
    private String email;             // 이메일


    private String number;


    @Enumerated(EnumType.STRING)
    private Role role;

    private String oauth;


    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){

        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);

        member.setRole(Role.USER);
        return member;

    }
    @Builder
    public Member(String name, String email) {

        this.name = name;
        this.email = email;

    }
}
