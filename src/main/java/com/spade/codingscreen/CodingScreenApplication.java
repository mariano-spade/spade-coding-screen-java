package com.spade.codingscreen;

import com.spade.codingscreen.cli.CheckMatchRateCommand;
import com.spade.codingscreen.cli.LoadCsvCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * Main Spring Boot application class for Spade Coding Screen.
 */
@SpringBootApplication
public class CodingScreenApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CodingScreenApplication.class);

        // Disable web server for CLI commands (they don't need it)
        boolean isCliCommand = Arrays.stream(args)
            .anyMatch(arg -> "--load-csv".equals(arg) || "--check-match-rate".equals(arg));

        if (isCliCommand) {
            app.setWebApplicationType(WebApplicationType.NONE);
        }

        app.run(args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(LoadCsvCommand loadCsvCommand, CheckMatchRateCommand checkMatchRateCommand) {
        return args -> {
            for (String arg : args) {
                if ("--load-csv".equals(arg)) {
                    loadCsvCommand.run(args);
                    System.exit(0);
                } else if ("--check-match-rate".equals(arg)) {
                    checkMatchRateCommand.run(args);
                    System.exit(0);
                }
            }
            // If no CLI command, just let the server run
        };
    }
}

