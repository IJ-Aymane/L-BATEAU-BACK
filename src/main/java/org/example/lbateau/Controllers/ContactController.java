package org.example.lbateau.Controllers;

import org.example.lbateau.Entity.Contact;
import org.example.lbateau.Services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<Contact> createMessage(@RequestBody Contact contact) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.createMessage(contact));
    }

    @GetMapping
    public List<Contact> getAllMessages() {
        return contactService.getAllMessages();
    }

    @GetMapping("/{id}")
    public Optional<Contact> getMessageById(@PathVariable String id) {
        return contactService.getMessageById(id);
    }
}
