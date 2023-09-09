package com.sparta.post.exception;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException(String msg){
        super(msg);
    }
}
