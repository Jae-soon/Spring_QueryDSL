package com.ll.exam.QueryDSL;

import com.ll.exam.QueryDSL.user.entity.SiteUser;
import com.ll.exam.QueryDSL.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
// 클래스의 각 테스트케이스에 전부 Transactional 붙은 것과 동일
// @Test + @Transactional 조합은 자동으로 롤백을 유발시킨다.
@Transactional
@ActiveProfiles("test") // 테스트 모드 활성화
class QueryDslApplicationTests {
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("회원 생성")
	void t1() {
		SiteUser u3 = SiteUser.builder()
				.username("user3")
				.password("{noop}1234")
				.email("user3@test.com")
				.build();
		SiteUser u4 = new SiteUser(null, "user4", "{noop}1234", "user4@test.com");

		userRepository.saveAll(Arrays.asList(u3, u4));
	}

	@Test
	@DisplayName("1번 회원 찾기")
	void t2() {
		SiteUser u1 = userRepository.getQslUser(1L);

		assertThat(u1.getId()).isEqualTo(1L);
		assertThat(u1.getUsername()).isEqualTo("user1");
		assertThat(u1.getPassword()).isEqualTo("{noop}1234");
		assertThat(u1.getEmail()).isEqualTo("user1@test.com");
	}

	@Test
	@DisplayName("2번 회원 찾기")
	void t3() {
		SiteUser u2 = userRepository.getQslUser(2L);

		assertThat(u2.getId()).isEqualTo(2L);
		assertThat(u2.getUsername()).isEqualTo("user2");
		assertThat(u2.getPassword()).isEqualTo("{noop}1234");
		assertThat(u2.getEmail()).isEqualTo("user2@test.com");
	}

	@Test
	@DisplayName("모든 회원의 수")
	void t4() {
		long count = userRepository.getQslUserCount();

		assertThat(count).isGreaterThan(0);
	}

	@Test
	@DisplayName("가장 오래된 회원")
	void t5() {
		SiteUser u1 = userRepository.getQslOldestUser();

		assertThat(u1.getId()).isEqualTo(1L);
		assertThat(u1.getUsername()).isEqualTo("user1");
		assertThat(u1.getPassword()).isEqualTo("{noop}1234");
		assertThat(u1.getEmail()).isEqualTo("user1@test.com");
	}

	@Test
	@DisplayName("전체 회원을 오래된 순으로 정렬")
	void t6() {
		List<SiteUser> users = userRepository.getQslUsersAsc();

		SiteUser u1 = users.get(0);
		SiteUser u2 = users.get(1);

		assertThat(u1.getId()).isEqualTo(1L);
		assertThat(u1.getUsername()).isEqualTo("user1");
		assertThat(u1.getPassword()).isEqualTo("{noop}1234");
		assertThat(u1.getEmail()).isEqualTo("user1@test.com");
		assertThat(u2.getId()).isEqualTo(2L);
		assertThat(u2.getUsername()).isEqualTo("user2");
		assertThat(u2.getPassword()).isEqualTo("{noop}1234");
		assertThat(u2.getEmail()).isEqualTo("user2@test.com");
	}

	@Test
	@DisplayName("유저 중 username 혹은 email에 user1이 포함된 회원")
	void t7() {
		List<SiteUser> users = userRepository.searchQsl("user1");

		SiteUser u1 = users.get(0);
		assertThat(u1.getId()).isEqualTo(1L);
		assertThat(u1.getUsername()).isEqualTo("user1");
		assertThat(u1.getPassword()).isEqualTo("{noop}1234");
		assertThat(u1.getEmail()).isEqualTo("user1@test.com");
	}

	@Test
	@DisplayName("검색, Page 리턴")
	void t8() {
		int itemsInAPage = 1; // 한 페이지에 보여줄 아이템 개수
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.asc("id"));
		Pageable pageable = PageRequest.of(1, itemsInAPage, Sort.by(sorts)); // 한 페이지에 10까지 가능
		Page<SiteUser> usersPage = userRepository.searchQsl("user", pageable);

		List<SiteUser> users = usersPage.get().toList();

		SiteUser u = users.get(0);

		assertThat(u.getId()).isEqualTo(2L);
		assertThat(u.getUsername()).isEqualTo("user2");
		assertThat(u.getEmail()).isEqualTo("user2@test.com");
		assertThat(u.getPassword()).isEqualTo("{noop}1234");

		assertThat(usersPage.getNumber()).isEqualTo(1);
		assertThat(usersPage.getTotalPages()).isEqualTo(2);
		assertThat(usersPage.getTotalElements()).isEqualTo(2);

		// 검색어 : user1
		// 한 페이지에 나올 수 있는 아이템 수 : 1개
		// 현재 페이지 : 1
		// 정렬 : id 역순

		// 내용 가져오는 SQL
        /*
        SELECT site_user.*
        FROM site_user
        WHERE site_user.username LIKE '%user%'
        OR site_user.email LIKE '%user%'
        ORDER BY site_user.id ASC
        LIMIT 1, 1
         */

		// 전체 개수 계산하는 SQL
        /*
        SELECT COUNT(*)
        FROM site_user
        WHERE site_user.username LIKE '%user%'
        OR site_user.email LIKE '%user%'
         */
	}
}