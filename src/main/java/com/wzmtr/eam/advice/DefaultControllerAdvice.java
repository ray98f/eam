package com.wzmtr.eam.advice;

import com.wzmtr.eam.entity.response.BaseResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class DefaultControllerAdvice {

    public static final int THREE = 3;
    public static final int TWO = 2;
    public static final int ONE = 1;
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = CommonException.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse handleCommonException(CommonException e) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(e.getMessage(),
                null, locale);
        if (!Objects.isNull(e.getParams())) {
            message = messageSource.getMessage(ErrorCode.messageOf(e.getCode()),
                    e.getParams(), locale);
        }
        return new BaseResponse().code(e.getCode()).message(message);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse handMethodArgumentNotValidAndBindException(Exception e) {
        FieldError fieldError = null;
        String defaultMessage = "";
        if (e instanceof BindException) {
            BindException e1 = (BindException) e;
            fieldError = e1.getBindingResult().getFieldError();
            defaultMessage = Objects.requireNonNull(fieldError).getDefaultMessage();
        }
        Locale locale = LocaleContextHolder.getLocale();
        if (null != defaultMessage) {
            Integer code = Integer.valueOf(defaultMessage);
            Object[] arguments = Objects.requireNonNull(fieldError).getArguments();
            String field = fieldError.getField();
            if (null != arguments) {
                String message = null;
                if (arguments.length >= THREE) {
                    message = messageSource.getMessage(ErrorCode.messageOf(code),
                            new Object[]{field, arguments[TWO], arguments[ONE]}, locale);
                }
                if (arguments.length == TWO) {
                    message = messageSource.getMessage(ErrorCode.messageOf(code),
                            new Object[]{field, arguments[ONE]}, locale);
                }
                if (arguments.length == ONE) {
                    message = messageSource.getMessage(ErrorCode.messageOf(code),
                            new Object[]{field}, locale);
                }
                log.error(message);
                return new BaseResponse().code(code).message(message);
            }
        }
        return null;
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse handMethodArgumentNotValidAndBindException(ConstraintViolationException e) {
        Locale locale = LocaleContextHolder.getLocale();
        Optional<ConstraintViolation<?>> optional = e.getConstraintViolations().stream().findFirst();
        if (optional.isPresent()) {
            ConstraintViolation<?> violation = optional.get();
            Integer code = Integer.valueOf(violation.getMessageTemplate());
            String field = e.getMessage().split(":")[0].split("\\.")[ONE];
            String message = messageSource.getMessage(ErrorCode.messageOf(code),
                    new Object[]{field}, locale);
            return new BaseResponse().code(code).message(message);
        }
        return null;
    }
}
