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
            SiteUser u2 = SiteUser.builder()
                    .username("user2")
                    .password("{noop}1234")
                    .email("user2@test.com")
                    .build();

            userRepository.saveAll(Arrays.asList(u1, u2)); // 자식요소에서 중복이 날 경우 부모부터 저장 후 실행

            u1.addInterestKeywordContent("축구");
            u1.addInterestKeywordContent("농구");

            u2.addInterestKeywordContent("클라이밍");
            u2.addInterestKeywordContent("마라톤");
            u2.addInterestKeywordContent("농구");

            userRepository.saveAll(Arrays.asList(u1, u2));
        };
    }
}
