package com.page6.entity;

import com.page6.dto.MemberFormDto;
import com.page6.members.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member3")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PROFILE_PK", nullable = false, updatable = false, insertable = false)
    private Long id;

    @Column(name="BOARD_TITLE", nullable = false)
    private String name;    // 닉네임


    @Column(unique = true)
    private String email;   // 이메일

    private String password;    // 비밀번호

    private String number;

    @Enumerated(EnumType.STRING)
    private Role role;


    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){

        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);

        member.setRole(Role.USER);
        return member;

}
}
