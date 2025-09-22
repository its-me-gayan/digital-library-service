package org.gayan.dls.util;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import org.gayan.dls.constant.ApplicationConstant;
import org.gayan.dls.constant.ResponseCodes;
import org.gayan.dls.dto.generic.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/** Author: Gayan Sanjeewa User: gayan Date: 9/19/25 Time: 11:30 PM */
@Component
public class DlsResponseBuilder {
  private final HttpServletRequest request;

  @Autowired
  public DlsResponseBuilder(HttpServletRequest request) {
    this.request = request;
  }

  public <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
    return ResponseEntity.ok(
        ApiResponse.<T>builder()
            .success(Boolean.TRUE)
            .timestamp(Instant.now())
            .apiVersion(ApplicationConstant.API_VERSION)
            .status(HttpStatus.OK.value())
            .path(request.getRequestURI()) // <-- auto injected
            .message(message)
            .responseCode(ResponseCodes.RP_SUCCESS)
            .data(data)
            .build());
  }

  public <T> ResponseEntity<ApiResponse<T>> error(
      HttpStatus status, String message, List<String> errors) {
    return ResponseEntity.status(status)
        .body(
            ApiResponse.<T>builder()
                .success(Boolean.FALSE)
                .timestamp(Instant.now())
                .apiVersion(ApplicationConstant.API_VERSION)
                .status(status.value())
                .path(request.getRequestURI()) // <-- auto injected
                .message(message)
                .responseCode(ResponseCodes.RP_VALIDATION_FAILURE)
                .errors(errors)
                .build());
  }

  public <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
    return ResponseEntity.status(status)
        .body(
            ApiResponse.<T>builder()
                .success(Boolean.FALSE)
                .timestamp(Instant.now())
                .apiVersion(ApplicationConstant.API_VERSION)
                .status(status.value())
                .path(request.getRequestURI()) // <-- auto injected
                .message(message)
                .responseCode(ResponseCodes.RP_UNEXPECTED_ERROR)
                .build());
  }
}
