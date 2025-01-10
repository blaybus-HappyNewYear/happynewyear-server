package blaybus.happynewyear.config.security;

import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.member.jwt.JwtVerificationFilter;
import blaybus.happynewyear.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class securityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                //.formLogin(AbstractHttpConfigurer::disable)
                //.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/mypage/**").hasRole("USER")
                        .requestMatchers("/sign-out").hasRole("USER")
                        .requestMatchers("/jwt-test").hasRole("USER")
                        .anyRequest().permitAll())
                .addFilterBefore(new JwtVerificationFilter(jwtTokenProvider, redisService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
