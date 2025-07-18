package com.pragma.capabilities.infraestructure.exceptionhandler;

import com.pragma.capabilities.domain.exception.InvalidCapabilityException;
import com.pragma.capabilities.domain.exception.TechnologyNotExitException;
import com.pragma.capabilities.domain.model.enumdata.SortBy;
import com.pragma.capabilities.domain.model.enumdata.SortOrder;
import com.pragma.capabilities.domain.model.error.ErrorResponse;
import com.pragma.capabilities.domain.util.ExceptionConstans;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {
    private static final String MESSAGE = ExceptionConstans.FIRST_PART_MESSAGE_EXCEPTION;


    @ExceptionHandler(InvalidCapabilityException.class)
    public ResponseEntity<ErrorResponse> handleCapabilityInvalid (
            InvalidCapabilityException ex) {
        ErrorResponse body = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }
    @ExceptionHandler(TechnologyNotExitException.class)
    public ResponseEntity<ErrorResponse> handleTechnologyNotFound (
            TechnologyNotExitException ex) {

        ErrorResponse body = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleWebInput(ServerWebInputException ex) {

        HttpStatusCode status = ex.getStatusCode();

        Throwable cause = ex.getCause();

        if (cause instanceof ConversionFailedException cf && cf.getTargetType().getType().equals(SortOrder.class)) {
            String raw = cf.getValue() != null ? cf.getValue().toString() : "";
            return Mono.just(buildEnumError(status,"order", raw, SortOrder.values()));
        }
        if (cause instanceof ConversionFailedException cf2 && cf2.getTargetType().getType().equals(SortBy.class)) {
            String raw = cf2.getValue() != null ? cf2.getValue().toString() : "";
            return Mono.just(buildEnumError(status,"sortBy", raw, SortBy.values()));
        }

        ErrorResponse body = new ErrorResponse(status.value(), ex.getReason());
        return Mono.just(ResponseEntity.badRequest().body(body));
    }

    private ResponseEntity<ErrorResponse> buildEnumError(HttpStatusCode status,
                                                         String param,
                                                         String badValue,
                                                         Enum<?>[] allowed) {
        String valid = Arrays.stream(allowed)
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        String msg = String.format(ExceptionConstans.VALUE_NOT_IN_ENUM,
                MESSAGE, param, badValue, valid);
        ErrorResponse body = new ErrorResponse(status.value(), msg);
        return ResponseEntity.badRequest().body(body);
    }
}
