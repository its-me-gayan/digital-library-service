package org.gayan.dls.dto.generic;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/19/25
 * Time: 11:23 PM
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponse<T> {
    private boolean success;
    private String responseCode;
    private String message;
    private int status;
    private String path;
    private String apiVersion;
    private Instant timestamp;
    private T data;
    private List<String> errors;   // <-- new

}
