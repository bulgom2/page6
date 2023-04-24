package com.page6.entity;

import com.page6.dto.MemberFormDto;
import com.page6.members.Role;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Entity
@Table(name="member_tb")
@Getter
@Setter
@ToString
@NoArgsConstructor

public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //연결된 db의 넘버링 전략 따라감
    @Column(name = "member_pk", nullable = false)
    private Long id;    // 시퀀스, auto_increment

    @Column(name="member_name", nullable = false)
    private String name;              // 닉네임

    @Column(name="member_password", nullable = true)
    private String password;          // 비밀번호

    @Column(unique = true, nullable = false)
    private String email;             // 이메일

    @Enumerated(EnumType.STRING)
    @Column(name="member_role")
    private Role role;

    @Column(name="member_oauth")
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
    public Member(String name, String email, String oauth, String password, Role role) {

        this.name = name;
        this.email = email;
        this.oauth = oauth;
        this.password= password;
        this.role = role;

    }

}
