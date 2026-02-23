package com.vatek.hrmtool.service.serviceImpl;

import com.vatek.hrmtool.dto.ContactDto;
import com.vatek.hrmtool.entity.Contact;
import com.vatek.hrmtool.respository.old.ContactRepository;
import com.vatek.hrmtool.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;
    @Override
    public Contact createContact(ContactDto contactDto){
        Contact contactData = new Contact();
        contactData.setFirstName(contactDto.getFirstName());
        contactData.setLastName(contactDto.getLastName());
        contactData.setEmail(contactDto.getEmail());
        contactData.setPhone(contactDto.getPhone());
        contactData.setOtherContact(contactDto.getOtherContact());
        contactData.setCountry(contactDto.getCountry());
        contactData.setCompanyName(contactDto.getCompanyName());
        contactData.setRole(contactDto.getRole());
        contactData.setRequest(contactDto.getRequest());
        contactData.setAllowReceiveOffers(contactDto.getAllowReceiveOffers());
        contactData.setAgreePrivacyPolicy(contactDto.getAgreePrivacyPolicy());
        contactData.setIdeaName(contactDto.getIdeaName());
        contactData.setType(contactDto.getType());
        contactData.setMessage(contactDto.getMessage());
        contactData.setServicesFocusing(contactDto.getServicesFocusing());
        return contactRepository.save(contactData);
    }
    @Override
    public List<Contact> findAll(){
        return contactRepository.findAllByOrderByDateDesc();
    }
}
