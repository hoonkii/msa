//package com.example.userservice.configuration;
//
//import com.example.userservice.security.AuthenticationFilter;
//import com.example.userservice.service.UserService;
//import lombok.RequiredArgsConstructor;
//
//
////@Configuration
////@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurity {
//
//    private final UserService userService;
////    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
//        http.authorizeRequests().antMatchers("/**")
//                .hasIpAddress("127.0.0.1")
//                .and()
//                .addFilter(getAuthenticationFilter());
//        return http.build();
//    }
//    // TODO 최신 버전에 맞게 셋업
//    private AuthenticationFilter getAuthenticationFilter() {
//        AuthenticationFilter filter = new AuthenticationFilter();
////        filter.setAuthenticationManager(authenticationManager());
//        return filter;
//    }
//
//}
