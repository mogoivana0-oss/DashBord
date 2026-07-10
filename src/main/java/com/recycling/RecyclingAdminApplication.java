package com.recycling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application principale du Dashboard Administrateur
 * Application de Recyclage et Valorisation des Déchets
 */
@SpringBootApplication
@EnableScheduling
public class RecyclingAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecyclingAdminApplication.class, args);
    }
}
