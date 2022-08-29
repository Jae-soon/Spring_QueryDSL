package com.ll.exam.QueryDSL.user.repository;

import com.ll.exam.QueryDSL.user.entity.SiteUser;

import java.util.List;

public interface UserRepositoryCustom {
    SiteUser getQslUser(Long id);
    long getQslUserCount();
    SiteUser getQslOldestUser();

    List<SiteUser> getQslUsersAsc();

    List<SiteUser> searchQsl(String query);
}
