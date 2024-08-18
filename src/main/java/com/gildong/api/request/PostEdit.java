package com.gildong.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

//PostCreate와 PostEdit은 동일한거 아닌가?
//PostCreate에 담아서 보내면 안됨?
//그러면 큰일남
//기능이 다르면 안에 코드가 똑같아도 일단은 명확하게 분리해주고 시작해야함


@Getter
@Builder
@ToString
public class PostEdit {
    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }

}

