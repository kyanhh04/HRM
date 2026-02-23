package com.vatek.hrmtool.controller.advice;

import com.vatek.hrmtool.constant.ResponseConstant;
import com.vatek.hrmtool.dto.ErrorBindingDto;
import com.vatek.hrmtool.response.common.CommonRes;
import com.vatek.hrmtool.response.common.SuccessRes;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
@AllArgsConstructor
public class ResponseHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public CommonRes handleAccessDeniedException(AccessDeniedException ex) {
        return CommonRes
                .commonBuilder()
                .code(ResponseConstant.Code.PERMISSION_DENIED)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public CommonRes handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        return CommonRes
                .commonBuilder()
                .code(ResponseConstant.Code.AUTHENTICATION_ERROR)
                .message(ex.getMessage())
                .build();
    }


    @ExceptionHandler(value = DisabledException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonRes handleDisableException() {
        return CommonRes
                .commonBuilder()
                .code(ResponseConstant.Code.USER_INACTIVE)
                .message(ResponseConstant.Message.USER_INACTIVE)
                .build();
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public SuccessRes<List<ErrorBindingDto>> handleBindingErrors(BindException ex) {
        var errorResponse = new SuccessRes<List<ErrorBindingDto>>(null);
        errorResponse.setCode(ResponseConstant.Code.MISSING_FIELD);

        var errors = ex.getBindingResult().getFieldErrors();

        var errorBindingDtoList = errors.stream().map(x -> {
            ErrorBindingDto errorBindingDto = new ErrorBindingDto();
            errorBindingDto.setFieldError(x.getField());
            errorBindingDto.setErrorMessage(x.getDefaultMessage());
            return errorBindingDto;
        }).toList();

        errorResponse.setData(errorBindingDtoList);

        return errorResponse;
    }


    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonRes handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(ResponseConstant.Code.INTERNAL_SERVER_ERROR);
        commonRes.setMessage(ex.getMessage());
        return commonRes;
    }
    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonRes handleDuplicateKeyException(DuplicateKeyException ex){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(ResponseConstant.Code.ALREADY_EXISTS);
        commonRes.setMessage(ex.getMessage());
        return commonRes;
    }
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonRes handleRuntimeException(RuntimeException ex){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(ResponseConstant.Code.INTERNAL_SERVER_ERROR);
        commonRes.setMessage(ex.getMessage());
        return commonRes;
    }
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonRes handleIllegalArgumentException(IllegalArgumentException ex){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(ResponseConstant.Code.BAD_REQUEST);
        commonRes.setMessage(ex.getMessage());
        return commonRes;
    }
    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonRes handleResourceNotFoundException(ResourceNotFoundException ex){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(ResponseConstant.Code.NOT_FOUND);
        commonRes.setMessage(ex.getMessage());
        return commonRes;
    }
    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonRes handleIllegalStateException(IllegalStateException ex){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(ResponseConstant.Code.BAD_REQUEST);
        commonRes.setMessage(ex.getMessage());
        return commonRes;
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    public CommonRes handleResponseStatusException(ResponseStatusException ex){
        CommonRes commonRes = new CommonRes();
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        commonRes.setCode(mapHttpStatusToCode(status));
        commonRes.setMessage(ex.getReason());
        return commonRes;
    }

    private String mapHttpStatusToCode(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST, UNSUPPORTED_MEDIA_TYPE -> ResponseConstant.Code.BAD_REQUEST;
            case UNAUTHORIZED -> ResponseConstant.Code.AUTHENTICATION_ERROR;
            case FORBIDDEN -> ResponseConstant.Code.PERMISSION_DENIED;
            case NOT_FOUND -> ResponseConstant.Code.NOT_FOUND;
            case CONFLICT -> ResponseConstant.Code.ALREADY_EXISTS;
            default -> ResponseConstant.Code.INTERNAL_SERVER_ERROR;
        };
    }
}
