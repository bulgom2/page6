package com.page6.service;

import com.page6.entity.Member;
import com.page6.members.Role;
import com.page6.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
//MemberService가 UserDetailsService를 구현
public class MemberService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;


    // 마이페이지 닉네임 불러오기
    public String getMemberNameByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            return member.getName();
        } else {
            return null;
        }
    }

  @Transactional(readOnly = true)
  public int findMember(String email) {
   Member member = memberRepository.findByEmail(email);
   if(member == null){
      return 1;
     }
      return 0;
   }

    @Transactional
    public void singUp(Member member) {

            memberRepository.save(member);

    }


    //로그인 인물이 admin인지 판정
    public boolean isAdmin(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            // 해당 이메일을 가진 멤버가 존재하지 않는 경우 처리
            return false;
        }
        Role role = member.getRole();
        if (Role.ADMIN.equals(role))
            return true;    // 해당 멤버가 관리자인 경우 처리
        else
            return false;   // 해당 멤버가 일반 사용자인 경우 처리
    }






    @Transactional
    public Member saveMember(Member member) {
        validateDuplicateMember(member);

        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    /*UserDetailsService 인터페이스의 loadUserByUsername() 메소드를 오버라이딩
     로그인할 유저의 email을 파라미터로 전달받습니다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        /* userDetail을 구현하고 있는 user 객체 반환. User 객체를 생성하기 위해서
        생성자로 회원의 이메일,비밀번호,role을 파라미터 넘겨줌
         */
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();

    }


}