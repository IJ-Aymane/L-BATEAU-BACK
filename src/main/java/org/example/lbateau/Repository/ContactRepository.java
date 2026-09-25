package org.example.lbateau.Repository;

import org.example.lbateau.Entity.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends MongoRepository<Contact, String> {
}
