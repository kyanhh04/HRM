package com.vatek.hrmtool.dto.LandingPageContact;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RequestFormSubmitDto {

    @NotBlank(message = "Project stage is required")
    private String projectStage;

    @NotEmpty(message = "Product type is required")
    private List<String> productType;

    private List<String> typeOfHelp;

    @NotEmpty(message = "Technologies is required")
    private List<String> technologies;

    private List<String> frontEndTech;

    private List<String> backEndTech;

    private List<String> techAdviceNeeded;

    @NotNull(message = "Design needed flag is required")
    private Boolean isDesignNeeded;

    private String generalDescription;

    @NotBlank(message = "Project size is required")
    private String projectSize;

    @NotBlank(message = "Preferred team is required")
    private String preferedTeam;

    @NotBlank(message = "Time to deliver is required")
    private String timeToDeliver;

    @NotBlank(message = "Expected budget is required")
    private String expectedBudget;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Client name is required")
    private String clientName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    private String email;

}
