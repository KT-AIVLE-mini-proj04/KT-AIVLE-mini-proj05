package com.aivle.bookapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) {}
}
