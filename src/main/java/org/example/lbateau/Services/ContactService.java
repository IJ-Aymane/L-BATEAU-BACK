package org.example.lbateau.Services;

import org.example.lbateau.Entity.Contact;
import org.example.lbateau.Repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> getAllMessages() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getMessageById(String id) {
        return contactRepository.findById(id);
    }

    public Contact createMessage(Contact contact) {
        if (contact.getDateCreation() == null) {
            contact.setDateCreation(new Date());
        }
        if (contact.getStatus() == null || contact.getStatus().isBlank()) {
            contact.setStatus("NOUVEAU");
        }
        if (contact.getSource() == null || contact.getSource().isBlank()) {
            contact.setSource("contact_page");
        }
        contact.setWebsite(null);
        return contactRepository.save(contact);
    }
}
