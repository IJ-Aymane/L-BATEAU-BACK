package org.example.lbateau.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "contact_messages")
@TypeAlias("org.example.lbateau.Entity.Contact")
public class Contact {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private String status;
    private String source;
    private Boolean consent;
    private String website;
    private Date dateCreation;
}
