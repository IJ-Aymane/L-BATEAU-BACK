package org.example.lbateau.Repository;


import org.example.lbateau.Entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {

    // Spring ghadi yfhem rasso bli bghiti tjbed client b la CIN dialo
    Optional<Client> findByCin(String cin);

    // Tjbed client b nmira d tilifon
    Optional<Client> findByTelephone(String telephone);
}