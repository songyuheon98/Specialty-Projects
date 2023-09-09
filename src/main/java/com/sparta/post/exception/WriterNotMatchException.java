package com.sparta.post.exception;

public class WriterNotMatchException extends RuntimeException{
    public WriterNotMatchException(String msg){
        super(msg);
    }
}
