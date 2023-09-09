package com.sparta.post.service;


import com.sparta.post.entity.*;
import com.sparta.post.repository.CommentLikeRepository;
import com.sparta.post.repository.CommentRepository;
import com.sparta.post.repository.PostLikeRepository;
import com.sparta.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseEntity<?> createLikePost(Long postId, Long userId) {

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글은 존재하지 않습니다.")
        );
        post.setLikeCount(post.getLikeCount() + 1);

        PostLike postLike= postLikeRepository.findById(postId).orElse(null);

        if(postLike == null) {
            postLikeRepository.save(new PostLike(postId, userId));
        } else {
            postLike.setPostLike((!postLike.getPostLike()));
        }
        return new ResponseEntity<>(new Message("게시글 좋아요 성공",200), null, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> createLikeComment(Long postId, Long commentId, Long userId) {
        /**
         * 좋아요가 눌릴 댓글이 존재하는 지 여부를 확인
         */
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new IllegalArgumentException("해당 댓글은 존재하지 않습니다.")
        );
        comment.setLikeCount(comment.getLikeCount()+1);

        CommentLike commentLike = commentLikeRepository.findById(commentId).orElse(null);
        if(commentLike==null)
            commentLikeRepository.save(new CommentLike(postId,commentId,userId));
        else
            commentLike.setCommentLike(!commentLike.getCommentLike());

        return new ResponseEntity<>(new Message("게시글 좋아요 성공",200), null, HttpStatus.OK);

    }

}
