package org.example.lbateau.Services;

import org.example.lbateau.Entity.Client;
import org.example.lbateau.Repository.ClientRepository; // T2ked mn smyat l'package dial repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(String id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        client.setDateCreation(new Date());
        return clientRepository.save(client);
    }

    public Client updateClient(String id, Client clientDetails) {
        Optional<Client> clientExistant = clientRepository.findById(id);
        if (clientExistant.isPresent()) {
            Client client = clientExistant.get();
            client.setNomComplet(clientDetails.getNomComplet());
            client.setTelephone(clientDetails.getTelephone());
            client.setCin(clientDetails.getCin());
            return clientRepository.save(client);
        }
        return null;
    }

    public void deleteClient(String id) {
        clientRepository.deleteById(id);
    }
}