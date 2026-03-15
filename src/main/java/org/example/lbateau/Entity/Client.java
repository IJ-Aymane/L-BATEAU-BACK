package org.example.lbateau.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Document(collection = "clients")
public class Client {
    @Id
    private String id;
    private String nomComplet;
    private String telephone;
    private String cin;
    private Date dateCreation;
}
