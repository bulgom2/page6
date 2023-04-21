package com.page6.service;

import com.page6.entity.Member;
import com.page6.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.page6.members.Role;


@RequiredArgsConstructor
@Transactional
@Service
//MemberService가 UserDetailsService를 구현
public class MemberService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @Transactional(readOnly = true)             //회원찾기
    public Member findMember(String email) {
        Member member = memberRepository.findByEmail(email).orElseGet(()->{
            return new Member();
        });
        return member;
    }

    //  @Transactional(readOnly = true)
//  public int fineMember(String email) {                     // 회원찾기
//   Member member = memberRepository.findByEmail(email);
//   if(member == null){
//      return 1;
//     }
//      return 0;
//   }

    @Transactional
    public int singUp(Member member) {                              //회원가입
        String rawPassword = member.getPassword(); // 1234 원문
        String encPassword = encoder.encode(rawPassword); // 해쉬
        member.setPassword(encPassword);
        member.setRole(Role.USER);
        try {
            memberRepository.save(member);
            return 1;
        } catch (Exception e) {
            return -1;
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

//    @Transactional
//    public void singUp(Member member) {
//
//
//            member.setRole(Role.USER);
//            memberRepository.save(member);
//
//    }



//    @Transactional
//    public Member saveMember(Member member) {
//        validateDuplicateMember(member);
//
//        return memberRepository.save(member);
//    }

//    private void validateDuplicateMember(Member member) {
//        Member findMember = memberRepository.findByEmail(member.getEmail());
//        if (findMember != null) {
//            throw new IllegalStateException("이미 가입된 회원입니다.");
//        }
//    }

    /*UserDetailsService 인터페이스의 loadUserByUsername() 메소드를 오버라이딩
     로그인할 유저의 email을 파라미터로 전달받습니다.
     */
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Member member = memberRepository.findByEmail(email);
//
//        if (member == null) {
//            throw new UsernameNotFoundException(email);
//        }
//
//        /* userDetail을 구현하고 있는 user 객체 반환. User 객체를 생성하기 위해서
//        생성자로 회원의 이메일,비밀번호,role을 파라미터 넘겨줌
//         */
//        return User.builder()
//                .username(member.getEmail())
//                .password(member.getPassword())
//                .roles(member.getRole().toString())
//                .build();
//
//    }


}