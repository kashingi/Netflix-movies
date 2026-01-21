package com.netflix.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//Add your annotations here
@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is not required")
    private String newPassword;
}
