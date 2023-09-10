package com.sparta.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter // 직렬화 Json -> 객체(String)
@Setter // 역직렬화 객체(String) -> Json
public class ReplyRequestDto {
    private Long commentId;
    private String content;
}
