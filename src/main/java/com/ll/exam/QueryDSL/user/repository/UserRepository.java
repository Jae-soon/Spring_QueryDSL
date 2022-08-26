package com.ll.exam.QueryDSL.user.repository;

import com.ll.exam.QueryDSL.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long>, UserRepositoryCustom {

}
