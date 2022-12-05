package com.example.eunboard.shared.exception;

import com.example.eunboard.shared.exception.custom.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@ControllerAdvice
public class CommonException {

    @ExceptionHandler(value =  Exception.class) //모든 예외를 의미
    public ResponseEntity exception(Exception e) {
        log.info("! 특정 예외 발생 ! ");
        log.info((e.getClass().getName())); //예외가 어디서 발생했는지 찾음

        log.info(("!!!!!! 예외 발생 !!!!!!!!"));
        log.info((e.getLocalizedMessage())); //예외 메세지 출력
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleBoardException(CustomException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e){
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.PARAMETER_NOT_VALID);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 특정 예외를 처리하는 메서드
//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
//
////        return ResponseEntity<Map<asdf,asdf>;
//    }

}
