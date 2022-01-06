package com.cybertek.exeption;

import com.cybertek.dto.DefaultExceptionMessageDto;
import com.cybertek.entity.ResponseWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

@RestControllerAdvice  // making this class global
@Order(Ordered.HIGHEST_PRECEDENCE) // Dont execute spring own serviceExeption, mine has precedence...use this one...
public class ExceptionMessageHandler {

    @ExceptionHandler(TicketingProjectExeption.class)
    public ResponseEntity<ResponseWrapper> serviceException(@NotNull TicketingProjectExeption se){
        String message = se.getMessage();
        return new ResponseEntity<>(ResponseWrapper
                .builder()
                .success(false)
                .code(HttpStatus.CONFLICT.value())
                .message(message)
                .build(),HttpStatus.CONFLICT);
    }

    // if role is wrong..spring will throw exeption which is below generic exeption...we have to throw this exeption somewhere...
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseWrapper> accessDeniedException(@NotNull AccessDeniedException se){
        String message = se.getMessage(); // spring
        return new ResponseEntity<>(ResponseWrapper
                .builder()
                .success(false)
                .code(HttpStatus.FORBIDDEN.value())
                .message(message)
                .build(),HttpStatus.CONFLICT);
    }

    // we are customizing exeption.class by making a generic
    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class, BadCredentialsException.class})
    public ResponseEntity<ResponseWrapper> genericException(Throwable e, @NotNull HandlerMethod handlerMethod) {

        Optional<DefaultExceptionMessageDto> defaultMessage = getMessageFromAnnotation(handlerMethod.getMethod());
        if (defaultMessage.isPresent() && !ObjectUtils.isEmpty(defaultMessage.get().getMessage())) {
            ResponseWrapper response = ResponseWrapper
                    .builder()
                    .success(false)
                    .message(defaultMessage.get().getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(ResponseWrapper.builder().success(false).message("Action failed: An error occurred!").code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Optional<DefaultExceptionMessageDto> getMessageFromAnnotation(@NotNull Method method) {
        com.cybertek.annotation.DefaultExceptionMessage defaultExceptionMessage = method.getAnnotation(com.cybertek.annotation.DefaultExceptionMessage.class);
        if (defaultExceptionMessage != null) {
            DefaultExceptionMessageDto defaultExceptionMessageDto = DefaultExceptionMessageDto
                    .builder()
                    .message(defaultExceptionMessage.defaultMessage())
                    .build();
            return Optional.of(defaultExceptionMessageDto);
        }
        return Optional.empty();
    }
}