package com.netflix.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
//Add your annotations here
@Getter
@AllArgsConstructor
public class EmailValidationResponse {

    private boolean exists;
    private boolean available;
}
