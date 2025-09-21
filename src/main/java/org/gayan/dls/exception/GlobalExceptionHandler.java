package org.gayan.dls.exception;


import lombok.RequiredArgsConstructor;
import org.gayan.dls.dto.generic.ApiResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import jakarta.validation.ConstraintViolationException;
import org.gayan.dls.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/20/25
 * Time: 9:43 PM
 */

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseBuilder responseBuilder;


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        return responseBuilder.error(HttpStatus.BAD_REQUEST,"", errors);
    }

    // ✅ Handle ConstraintViolation (e.g. from @Validated on query params)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));

        return responseBuilder.error(HttpStatus.BAD_REQUEST, errors);
    }

    // ✅ Handle custom business exceptions
    @ExceptionHandler(BookException.class)
    public ResponseEntity<ApiResponse<Object>> handleBookException(BookException ex) {
        return responseBuilder.error(ex.getHttpStatus(), ex.getMessage());
    }
    // ✅ Handle custom business exceptions
    @ExceptionHandler(BorrowerException.class)
    public ResponseEntity<ApiResponse<Object>> handleBorrowerException(BorrowerException ex) {
        return responseBuilder.error(ex.getHttpStatus(), ex.getMessage());
    }
    @ExceptionHandler(LibraryOperationException.class)
    public ResponseEntity<ApiResponse<Object>> handleLibraryOperationException(LibraryOperationException ex) {
        return responseBuilder.error(ex.getHttpStatus(), ex.getMessage());
    }
    @ExceptionHandler(NoContentFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoContentFoundException(NoContentFoundException ex) {
        return responseBuilder.error(HttpStatus.NO_CONTENT, ex.getMessage());
    }

    // ✅ Fallback for all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {
        return responseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
    }
}
