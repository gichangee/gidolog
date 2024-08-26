package com.gildong.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gildong.api.domain.Post;
import com.gildong.api.repository.PostRepository;
import com.gildong.api.request.PostCreate;
import com.gildong.api.request.PostEdit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.hodolman.com",uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @DisplayName("글 단건 조회 테스트")
    void test1() throws Exception {

        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}",1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry", RequestDocumentation.pathParameters(
                            RequestDocumentation.parameterWithName("postId").description("게시글 ID")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("id").description("게시글 ID"),
                                PayloadDocumentation.fieldWithPath("title").description("제목"),
                                PayloadDocumentation.fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @DisplayName("글 등록 테스트")
    void test2() throws Exception {

        //given

        PostCreate request = PostCreate.builder()
                .title("호돌맨")
                .content("반포자이.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/posts")
                        .header("authorization", "hodolman")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("title").description("제목")
                                        .attributes(Attributes.key("constraint").value("좋은 제목 입력해주세용")),
                                PayloadDocumentation.fieldWithPath("content").description("내용").optional()
                        )
                ));
    }

    @Test
    @DisplayName("글 수정")
    void test3() throws Exception {

        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();


        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/posts/{postId}",post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("post-edit", RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("postId").description("게시글 ID")
                )
        ));
    }

    @Test
    @DisplayName("글 삭제")
    void test4() throws Exception {

    }

    @Test
    @DisplayName("글 전체 조회")
    void test5() throws Exception {

    }

}
