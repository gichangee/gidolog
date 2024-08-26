package com.gildong.api.controller;

import com.gildong.api.config.data.UserSession;
import com.gildong.api.request.PostCreate;
import com.gildong.api.request.PostEdit;
import com.gildong.api.request.PostSearch;
import com.gildong.api.response.PostResponse;
import com.gildong.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;


    @GetMapping("/foo")
    public Long foo(UserSession userSession) {
        log.info(">>>{}",userSession.id);
        return userSession.id;
    }

    @GetMapping("/bar")
    public String bar() {
        return "인증이 필요없는 페이지";
    }

    @GetMapping("/bar2")
    public String bar2(UserSession userSession) {
        return "인증이 필요한 페이지";
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {

        request.validate();
        postService.write(request);

    }


    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }


    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
