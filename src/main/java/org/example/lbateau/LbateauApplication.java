package org.example.lbateau;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class LbateauApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LbateauApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LbateauApplication.class);
    }

    @Bean
    CommandLineRunner createAllCollections(MongoTemplate mongoTemplate, MongoMappingContext mappingContext) {
        return args -> {
            System.out.println("DEBUG: Spring se connecte à la base : " + mongoTemplate.getDb().getName());

            mappingContext.getPersistentEntities().forEach(entity -> {
                Class<?> entityType = entity.getType();

                if (entityType.getSimpleName().equals("Contenu")) {
                    long count = mongoTemplate.count(new org.springframework.data.mongodb.core.query.Query(),
                            entityType);
                    System.out.println("DEBUG: [STARTUP] Nombre de documents dans 'contenu' : " + count);
                }


                if (!mongoTemplate.collectionExists(entityType)) {
                    mongoTemplate.createCollection(entityType);
                    System.out.println("✅ Auto-created collection for: " + entityType.getSimpleName());
                }
            });
        };
    }
}
