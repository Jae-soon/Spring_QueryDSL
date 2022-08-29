package com.ll.exam.QueryDSL.base;

import com.ll.exam.QueryDSL.user.entity.SiteUser;
import com.ll.exam.QueryDSL.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test") // 이 클래스가 정의된 Bean들은 test모드에서만 활성화
public class TestInitData {
    // CommandLineRunner : 주로 앱 실행 직후 초기데이터 세팅 및 초기화에 사용 -> 알아서 나중에 실행됨
    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            SiteUser u1 = SiteUser.builder()
                    .username("user1")
                    .password("{noop}1234")
                    .email("user1@test.com")
                    .build();
            SiteUser u2 = new SiteUser(null, "user2", "{noop}1234", "user2@test.com");

            userRepository.saveAll(Arrays.asList(u1, u2));
        };
    }
}
