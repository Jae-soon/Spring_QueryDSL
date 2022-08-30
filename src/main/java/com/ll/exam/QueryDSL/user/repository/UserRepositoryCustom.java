package com.ll.exam.QueryDSL.user.repository;

import com.ll.exam.QueryDSL.user.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    SiteUser getQslUser(Long id);
    long getQslUserCount();
    SiteUser getQslOldestUser();

    List<SiteUser> getQslUsersAsc();

    List<SiteUser> searchQsl(String query);
    Page<SiteUser> searchQsl(String kw, Pageable pageable);
}
