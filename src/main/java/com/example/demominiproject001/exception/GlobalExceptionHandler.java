package com.example.demominiproject001.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail buildProblem(HttpStatus status, String detail, HttpServletRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(status.getReasonPhrase());
        pd.setProperty("timestamp", Instant.now());

        if (request != null) {
            pd.setInstance(URI.create(request.getRequestURI()));
        }

        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.add(err.getDefaultMessage()));
        String detail = errors.isEmpty() ? "Validation failed" : errors.getFirst();
        return buildProblem(HttpStatus.BAD_REQUEST, detail, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {

        Map<String, String> violations = new HashMap<>();

        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            violations.put(v.getPropertyPath().toString(), v.getMessage());
        }

        String detail = violations.containsKey("role") ? violations.get("role") : "Constraint violation";

        return buildProblem(HttpStatus.BAD_REQUEST, detail, request);
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
//        java.util.List<String> errors = new java.util.ArrayList<>();
//        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
//            String path = v.getPropertyPath() != null ? v.getPropertyPath().toString() : "";
//            String msg = v.getMessage();
//            errors.add(msg);
//        }
//        String detail = errors.isEmpty() ? "Constraint violation" : errors.get(0);
//        return buildProblem(HttpStatus.BAD_REQUEST, detail, request);
//    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {

        Throwable root = ex.getMostSpecificCause();

        if (root instanceof InvalidFormatException ife && ife.getTargetType() != null && ife.getTargetType().isEnum()) {
            String field = (ife.getPath() != null && !ife.getPath().isEmpty()) ? ife.getPath().get(0).getFieldName() : "unknown";

            if ("role".equals(field)) {
                return buildProblem(HttpStatus.BAD_REQUEST, "Role is required and must be AUTHOR or READER", request);
            }

            Object[] allowed = ife.getTargetType().getEnumConstants();
            String allowedCsv = java.util.Arrays.stream(allowed).map(Object::toString).reduce((a,b) -> a + ", " + b).orElse("");
            String detail = "Invalid value for '" + field + "'. Allowed: [" + allowedCsv + "]";

            return buildProblem(HttpStatus.BAD_REQUEST, detail, request);
        }

        return buildProblem(HttpStatus.BAD_REQUEST, "Malformed JSON request", request);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidToken(InvalidTokenException ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String msg = "Parameter '" + ex.getName() + "' should be of type " + (ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "expected type");
        return buildProblem(HttpStatus.BAD_REQUEST, msg, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String msg = "Missing required parameter '" + ex.getParameterName() + "'";
        return buildProblem(HttpStatus.BAD_REQUEST, msg, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.UNAUTHORIZED, "Invalid username or password", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.CONFLICT, ex.getMessage() != null ? ex.getMessage() :"Data integrity violation: ", request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        return buildProblem(status != null ? status : HttpStatus.BAD_REQUEST, ex.getReason(), request);
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ProblemDetail handleErrorResponse(ErrorResponseException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        String detail = ex.getBody() != null && ex.getBody().getDetail() != null ? ex.getBody().getDetail() : ex.getMessage();
        return buildProblem(status != null ? status : HttpStatus.BAD_REQUEST, detail, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntime(RuntimeException ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ProblemDetail handleAuthorizationDenied(AuthorizationDeniedException ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.FORBIDDEN, "You do not have permission to access this resource.", request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobal(Exception ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request);
    }

}