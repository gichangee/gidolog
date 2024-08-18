package com.gildong.api.repository;

import com.gildong.api.domain.Post;
import com.gildong.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
