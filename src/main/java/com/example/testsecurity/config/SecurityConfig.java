package com.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean //암호화
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Bean //계층 권한설정
//    public RoleHierarchy roleHierarchy() {
//        return RoleHierarchyImpl.fromHierarchy("""
//            ROLE_C > ROLE_B
//            ROLE_B > ROLE_A
//            """);
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login","join","joinProc").permitAll() //경로를 설정하는거이다. permitAll() 모두가능하게
                        .requestMatchers("/admin").hasRole("ADMIN") //관리자만 사용가능
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER") //my page는 관리자랑 user만 가능
                        .anyRequest().authenticated() //로그인되면 접근가능
                );


        //계층권한
//        http
//                .authorizeHttpRequests((auth) -> auth
//                .requestMatchers("/login").permitAll()
//                .requestMatchers("/").hasAnyRole("A")
//                .requestMatchers("/manager").hasAnyRole("B")
//                .requestMatchers("/admin").hasAnyRole("C")
//                .anyRequest().authenticated()
//        );

        //formLogin 방식
//        http
//                .formLogin((auth) -> auth.loginPage("/login") //login으로가게됨
//                        .loginProcessingUrl("/loginProc") //자동으로 경로로 이동함 
//                        .permitAll() //경로가 모두가 접근가능하게
//                ); 


        //        CSRF란?
//                CSRF(Cross-Site Request Forgery)는 요청을 위조하여 사용자가 원하지 않아도 서버측으로
//                특정 요청을 강제로 보내는 방식이다. (회원 정보 변경, 게시글 CRUD를 사용자 모르게 요청)
//        http
//                .csrf((auth) -> auth.disable()); //csrf 설정 disable
//

        http
                .sessionManagement((auth) -> auth 
                        .maximumSessions(1) //1개의 아이디에서 동시접속 로그인
                        .maxSessionsPreventsLogin(true)); //초과했을때 어떻게할지 true는 새로운거 차단

        http
                .sessionManagement((auth) -> auth //세션보호
                        .sessionFixation().changeSessionId());
//        sessionManagement().sessionFixation().none() : 로그인 시 세션 정보 변경 안함
//        sessionManagement().sessionFixation().newSession() : 로그인 시 세션 새로 생성
//        sessionManagement().sessionFixation().changeSessionId() : 로그인 시 동일한 세션에 대한 id 변경


        //HttpBasic 방식
        http
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
