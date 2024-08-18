package com.gildong.api.repository;

import com.gildong.api.domain.Post;
import com.gildong.api.domain.QPost;
import com.gildong.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {

        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset()) //쿼리 결과의 시작 지점을 설정합니다
                .orderBy(QPost.post.id.desc())
                .fetch(); // 작성된 쿼리를 실행하고, 결과를 List<Post>로 반환합니다
    }
}
