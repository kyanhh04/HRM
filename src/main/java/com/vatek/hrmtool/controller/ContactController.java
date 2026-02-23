package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.dto.ContactDto;
import com.vatek.hrmtool.entity.Contact;
import com.vatek.hrmtool.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;
    @PostMapping("/create")
    public ResponseEntity<?> createContact(@RequestBody ContactDto contactDto){
        Contact contact = contactService.createContact(contactDto);
        return ResponseEntity.ok(contact);
    }
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(contactService.findAll());
    }
}
