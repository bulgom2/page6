package com.page6.dto;

import com.page6.entity.Member;
import com.page6.members.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoDto {
    private Long id;        // 멤버 인덱스
    private String name;    // 닉네임
    private String email;   // 이메일
    private String number;   // 폰 번호
    private Role role;

    public static MemberInfoDto of(Member member) {
        return MemberInfoDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .number(member.getNumber())
                .role(member.getRole())
                .build();
    }
}
