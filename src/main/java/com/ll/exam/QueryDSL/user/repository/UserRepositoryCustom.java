package com.ll.exam.QueryDSL.user.repository;

import com.ll.exam.QueryDSL.user.entity.SiteUser;

public interface UserRepositoryCustom {
    SiteUser getQslUser(Long id);
    long getQslUserCount();
}
