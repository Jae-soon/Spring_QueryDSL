package com.ll.exam.QueryDSL.user.entity;

import com.ll.exam.QueryDSL.interestKeyword.entity.InterestKeyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    @Builder.Default
    private Set<InterestKeyword> interestKeywords = new HashSet<>();

    public void addInterestKeywordContent(String keywordContent) {
        interestKeywords.add(new InterestKeyword(keywordContent));
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @Builder.Default
    private Set<SiteUser> followers = new HashSet<>();

//    public void addFollower(SiteUser follower) {
//        followers.add(follower);
//    }

    public void follow(SiteUser following) { // u2의 팔로워들
        if (this == following) return; // follow할 사람이 본인일 경우 그냥 return
        if (following == null) return; // 팔로우 할 사람이 없을 경우 그냥 return
        if (this.getId() == following.getId()) return; // 팔로우 할 사람의 id와 본인의 id가 같을 경우 그냥 return

        following.getFollowers().add(this); // u2의 팔로워들에 u1을 추가한다.
        // 이 부분은 같은 클래스 내에 있어 private가 무시되지만, 다른 클래스에 존재할 경우 실행이 되지 않기에 쓰지 않는 것이 좋다.
        // following.followers.add(this);
    }
}
