package com.example.eunboard.exception;

import com.example.eunboard.exception.custom.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonException {

    @ExceptionHandler(value =  Exception.class) //모든 예외를 의미
    public ResponseEntity exception(Exception e) {
        System.out.println("! 특정 예외 발생 ! ");
        System.out.println(e.getClass().getName()); //예외가 어디서 발생했는지 찾음

        System.out.println("!!!!!! 예외 발생 !!!!!!!!");
        System.out.println(e.getLocalizedMessage()); //예외 메세지 출력
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleBoardException(CustomException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    // 특정 예외를 처리하는 메서드
//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
//
////        return ResponseEntity<Map<asdf,asdf>;
//    }

}
