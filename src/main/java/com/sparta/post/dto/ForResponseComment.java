package com.sparta.post.dto;

import com.sparta.post.entity.Comment;
import com.sparta.post.entity.Post;
import com.sparta.post.entity.Reply;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class ForResponseComment {

    private Long id;

    private String content;

    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long likeCount;
    private List<ReplyResponseDto> replys = new ArrayList<>();

    public ForResponseComment(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = comment.getLikeCount();

        this.replys = comment.getReplys().stream()
                .map(ReplyResponseDto::new).toList();

//        for(Reply reply : comment.getReplys()){
//            ReplyResponseDto rr = new ReplyResponseDto(reply);
//            replys.add(rr);
//        }
    }
}
