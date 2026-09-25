package io.github.davidnest.teste.model.exception;

import io.github.davidnest.teste.controller.dto.message.ValidationFieldError;
import io.github.davidnest.teste.controller.dto.message.ResponseError;
import io.github.davidnest.teste.model.exception.car.CarAlreadySoldException;
import io.github.davidnest.teste.model.exception.car.CarNotFoundException;
import io.github.davidnest.teste.model.exception.client.ClientNotFoundException;
import io.github.davidnest.teste.model.exception.client.InsufficientBalanceException;
import io.github.davidnest.teste.model.exception.usuario.UsuarioCadastradoException;
import io.github.davidnest.teste.model.exception.usuario.UsuarioNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ResponseError> carNotFound(CarNotFoundException e){
        return montarErro(HttpStatus.NOT_FOUND.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ResponseError> clientNotFound(ClientNotFoundException e){
        return montarErro(HttpStatus.NOT_FOUND.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseError> invalidArgument(IllegalArgumentException e){
        return montarErro(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(UsuarioCadastradoException.class)
    public ResponseEntity<ResponseError> usuarioCadastrado(UsuarioCadastradoException e){
        return montarErro(HttpStatus.CONFLICT.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ResponseError> usuarioNotFound(UsuarioNotFoundException e){
        return montarErro(HttpStatus.NOT_FOUND.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> argumentInvalid(MethodArgumentNotValidException e){
        List<ValidationFieldError> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationFieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        return montarErro(HttpStatus.BAD_REQUEST.value(), "Erro de validação!", errors);
    }

    @ExceptionHandler(CarAlreadySoldException.class)
    public ResponseEntity<ResponseError> carAlreadySold(CarAlreadySoldException e){
        return montarErro(HttpStatus.CONFLICT.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ResponseError> insufficientBalance(InsufficientBalanceException e){
        return montarErro(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    private ResponseEntity<ResponseError>
    conflictOfCompetition(ObjectOptimisticLockingFailureException e){
    return montarErro(
                    HttpStatus.CONFLICT.value(),
            "Outra operação alterou esses dados ao mesmo tempo. Tente novamente.",
            List.of());
    }

    private ResponseEntity<ResponseError> montarErro(Integer status, String message, List<ValidationFieldError> errors) {
        ResponseError erro = new ResponseError(status, message, LocalDateTime.now(), errors);
        return ResponseEntity.status(status).body(erro);
    }
}
