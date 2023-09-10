package com.sparta.post.service;

import com.sparta.post.dto.CommentResponseDto;
import com.sparta.post.dto.ReplyRequestDto;
import com.sparta.post.dto.ReplyResponseDto;
import com.sparta.post.entity.*;
import com.sparta.post.exception.UserNotFoundException;
import com.sparta.post.exception.WriterNotMatchException;
import com.sparta.post.jwt.SecurityUtil;
import com.sparta.post.repository.CommentRepository;
import com.sparta.post.repository.ReplyRepository;
import com.sparta.post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> createReply(ReplyRequestDto requestDto) {
        String username = SecurityUtil.getPrincipal().get().getUsername();
        //RequestDto -> Entity
        Reply reply = new Reply(requestDto, username);
        //DB 저장
        Reply saveReply = replyRepository.save(reply);

        Comment comment = commentRepository.findById(requestDto.getCommentId()).orElseThrow(()->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.")
                );

        comment.addReplyList(reply); // Transaction 없기에 save merge
        System.out.println("댓글에 대댓글이 추가되었습니다.");

//        Comment saveComment = commentRepository.save(comment);
        //Entity -> ResponseDto
        // 수정 필요 ReplyResponseDto
        return new ResponseEntity<>(new ReplyResponseDto(saveReply),null, HttpStatus.OK );
    }

    @Transactional
    public ResponseEntity<?> updateReply(Long id, ReplyRequestDto requestDto) {
        User principal = SecurityUtil.getPrincipal().get();

        // 해당 유저의 댓굴 id 값이 DB 에 존재하는지 확인
        Reply reply = replyRepository.findById(id).orElseThrow(()->
                new UserNotFoundException("해당 대댓글이 존재하지 않습니다.")
        );

        String username = principal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()->
                new UserNotFoundException("회원을 찾을 수 없습니다.")
        );

        System.out.println(user.getRole());
        if(user.getRole().equals(UserRoleEnum.ADMIN)){
            System.out.println("관리자가 접근하였습니다.");
        } else if(!reply.getUsername().equals(user.getUsername()) ){
            throw new WriterNotMatchException("작성자만 삭제/수정할 수 있습니다.");
        }

        // comment 수정
        reply.update(requestDto);

        return new ResponseEntity<>(new ReplyResponseDto(reply),null, HttpStatus.OK );
    }

    public ResponseEntity<?> deleteReply(Long id) {

        Message msg = new Message("댓글 삭제 성공", 200);

        User principal = SecurityUtil.getPrincipal().get();

        // 해당 유저의 댓굴 id 값이 DB 에 존재하는지 확인
        Reply reply = replyRepository.findById(id).orElseThrow(()->
                new UserNotFoundException("해당 대댓글이 존재하지 않습니다.")
        );
        String username = principal.getUsername();

        User user = userRepository.findByUsername(username).orElseThrow(()->
                new UserNotFoundException("회원을 찾을 수 없습니다.")
        );

        if(user.getRole().equals(UserRoleEnum.ADMIN)){
            System.out.println("관리자가 접근하였습니다.");
        } else if(!reply.getUsername().equals(user.getUsername()) ){
            throw new WriterNotMatchException("작성자만 삭제/수정할 수 있습니다.");
        }
        // comment 삭제
        replyRepository.delete(reply);

        return new ResponseEntity<>(msg, null, HttpStatus.OK);
    }

}
