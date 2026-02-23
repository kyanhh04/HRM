package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.ContactDto;
import com.vatek.hrmtool.entity.Contact;

import java.util.List;

public interface ContactService {
    Contact createContact(ContactDto contactDto);
    List<Contact> findAll();
}
