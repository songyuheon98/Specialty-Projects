package com.sparta.post.service;

import com.sparta.post.dto.LoginRequestDto;
import com.sparta.post.dto.SignupRequestDto;
import com.sparta.post.entity.Message;
import com.sparta.post.entity.User;
import com.sparta.post.entity.UserRoleEnum;
import com.sparta.post.jwt.JwtUtil;
import com.sparta.post.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public ResponseEntity<Message> signup(SignupRequestDto requestDto) {

        Message msg = new Message(200, "회원가입 성공");

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // username 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if(checkUsername.isPresent()){
            return new ResponseEntity<>(new Message(400, "중복된 username 입니다."), null, HttpStatus.BAD_REQUEST);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if(requestDto.isAdmin()){
            if(!ADMIN_TOKEN.equals(requestDto.getAdminToken())){
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능 합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);

        return new ResponseEntity<>(msg, null, HttpStatus.OK);
    }

    public ResponseEntity<Message> login(LoginRequestDto requestDto, HttpServletResponse res) {

        Message msg = new Message(200, "로그인 성공");

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        User user;
        if(checkUsername.isPresent()){
            user = checkUsername.get();
        } else{
            return new ResponseEntity<>(new Message(400, "회원을 찾을 수 없습니다."), null, HttpStatus.BAD_REQUEST);
        }

        // 비밀번호 확인 check 필요
        if(!passwordEncoder.matches(password, user.getPassword()))
            return new ResponseEntity<>(new Message(400, "비밀번호가 일치하지 않습니다."), null, HttpStatus.BAD_REQUEST);

        // 인증이 완료후 JWT 생성및 쿠키에 저장후 Response 객체에 추가
        // 구현후 객체 주입 필요
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());
        jwtUtil.addJwtToCookie(token, res);

        return new ResponseEntity<>(msg, null, HttpStatus.OK);

    }
}
