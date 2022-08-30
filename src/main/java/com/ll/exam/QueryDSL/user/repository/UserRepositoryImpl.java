package com.ll.exam.QueryDSL.user.repository;

import com.ll.exam.QueryDSL.user.entity.QSiteUser;
import com.ll.exam.QueryDSL.user.entity.SiteUser;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ll.exam.QueryDSL.user.entity.QSiteUser.siteUser;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SiteUser getQslUser(Long id) {
        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(siteUser.id.eq(id))
                .fetchOne();
    }

    @Override
    public long getQslUserCount() {
        return jpaQueryFactory
                .select(siteUser.count())
                .from(siteUser)
                .fetchOne();
    }

    @Override
    public SiteUser getQslOldestUser() {
        return jpaQueryFactory
                .selectFrom(siteUser)
                .orderBy(siteUser.id.asc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public List<SiteUser> getQslUsersAsc() {
        return jpaQueryFactory
                .selectFrom(siteUser)
                .orderBy(siteUser.id.asc())
                .fetch();
    }

    @Override
    public List<SiteUser> searchQsl(String query) {
        return jpaQueryFactory
                .selectFrom(siteUser)
                .where(siteUser.username.contains(query).or(siteUser.email.contains(query)))
                .fetch();
    }

    @Override
    public Page<SiteUser> searchQsl(String kw, Pageable pageable) {

        QueryResults<SiteUser> queryResults = jpaQueryFactory
                .selectFrom(siteUser)
                .where(siteUser.username.contains(kw).or(siteUser.email.contains(kw)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<SiteUser> content = queryResults.getResults();
        long total = queryResults.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}