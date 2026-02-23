package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.dto.LandingPageContact.DiscussFormSubmitDto;
import com.vatek.hrmtool.dto.LandingPageContact.RequestFormSubmitDto;
import com.vatek.hrmtool.service.serviceImpl.LandingPageContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/landing-contact")
@Tag(name = "landing-contact")
public class LandingPageContactController {

    @Autowired
    private LandingPageContactService landingPageContactService;

    @PostMapping("/request-form")
    public ResponseEntity<?> submitRequestForm(@RequestBody RequestFormSubmitDto body) {
        landingPageContactService.submitRequestForm(body);
        return ResponseEntity.ok("Request form submitted successfully");
    }

    @PostMapping("/discuss-project-form")
    public ResponseEntity<?> submitDiscussForm(@RequestBody DiscussFormSubmitDto body) {
        landingPageContactService.submitDiscussForm(body);
        return ResponseEntity.ok("Discuss form submitted successfully");
    }
}
