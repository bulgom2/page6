package com.page6.config;

import com.page6.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;


    @Autowired
    MemberService memberService;


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean // IoC
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(encodePWD());
    }

    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     //   auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    //}


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();

        http.formLogin()
                .loginPage("/members/login")    //로그인페이지 url설정
                .defaultSuccessUrl("/")         //로그인성공시 이동할 url
                .usernameParameter("email")     //로그인성공시 파라미터 이름으로 email지정
                .failureUrl("/members/login/error") //로그인실패시 이동할 url 설정
                .successHandler(authenticationSuccessHandler)   //로그인 성공 시 뒤로가기
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃url설정
                .logoutSuccessUrl("/"); //로그아웃 성공시 이동할 url 설정



        http.authorizeRequests()    //권한설정
                .mvcMatchers("/", "/members/**", "/board/**", "/auth/**","/oauth/**").permitAll()
                .mvcMatchers("/board/write", "/write").hasRole("USER")
                .anyRequest().authenticated();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//
//       return new BCryptPasswordEncoder();
//    }



}