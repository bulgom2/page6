package com.page6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.page6.dto.MemberFormDto;
import com.page6.entity.KakaoProfile;
import com.page6.entity.Member;
import com.page6.entity.OAuthToken;
import com.page6.members.Role;
import com.page6.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;


import javax.validation.Valid;


@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;           //final 임시수정
    private final PasswordEncoder passwordEncoder;       // final 임시수정


   @Value("${garlic6.key}")
   private String garlic6key;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm"; //localhost:8080/membersForm 뷰 열기 (가입 폼)
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";       //로그인 실패시 메시지 출력

    }

    @GetMapping(value = "/auth/kakao/callback")
    public String kakaoCallback(String code) {    //Data를 리턴해주는 컨트롤러 함수

        // POST 방식으로 key=value 데이터를 요청(카카오쪽으로)
        // Retrofit2
        // OkHttp
        // RestTemplate

        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "6ee1d611c8e35830f26c3bc84f8b7e9a");
        params.add("redirect_uri", "http://localhost:8080/members/auth/kakao/callback");
        params.add("code", code);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );


        //Gson, Json Simple, ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;


        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("카카오 엑세스 토큰:" +  oauthToken.getAccess_token());

        RestTemplate rt2 = new RestTemplate();
//
//        // HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


//
        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );
        System.out.println(response2.getBody());

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String email = kakaoProfile.getKakao_account().getEmail();
        String nickname = kakaoProfile.getProperties().getNickname();

        // Member 오브젝트 : username,password,email
        System.out.println("카카오 아이디(번호):" + nickname);
        System.out.println("카카오 이메일:" + email);



//        System.out.println("블로그서버 패스워드 :" +cosKey);

        Member kakaoMember = Member.builder()
                .name(nickname)
                .email(email)
                .oauth("kakao")
                .password(garlic6key)//패스워드키 넣어주기
                .role(Role.USER)
                .build();

         //가입자 혹은 비가입자 체크 해서 처리
      int a = memberService.findMember(email);
      if(a ==  1){
      System.out.println("회원이 아니기에 자동 회원가입을 진행합니다");
           memberService.singUp(kakaoMember);
      }

      System.out.println("자동 로그인을 진행합니다.");

      // 로그인 처리

        UserDetails userDetails =memberService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,"@codehows213", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
//      Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoMember.getEmail(), cosKey));
//       SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";

    }
}