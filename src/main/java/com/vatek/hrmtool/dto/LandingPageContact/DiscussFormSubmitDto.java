package com.vatek.hrmtool.dto.LandingPageContact;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DiscussFormSubmitDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Message is required")
    private String message;

}
