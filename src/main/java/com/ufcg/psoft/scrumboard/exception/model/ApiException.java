package com.ufcg.psoft.scrumboard.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ApiException {

    private HttpStatus httpStatus;
    private String message;

}
