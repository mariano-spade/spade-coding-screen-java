# Spade Coding Screen - Merchant Matching Application

## Overview
This is a Java Spring Boot application for merchant matching, designed as an interview coding screen for Spade. The application matches customer merchant data to Spade Location/Corporation pairings using fuzzy string matching algorithms.

**Current State**: Application is configured and running on Replit. The basic infrastructure is set up, but the merchant matching logic in `SolutionController.java` needs to be implemented to achieve a 100% match rate.

## Recent Changes
- **2025-11-26**: Initial import and Replit environment setup
  - Configured server to bind to 0.0.0.0:5000 for Replit environment
  - Set up workflow to run Spring Boot application
  - Verified application starts successfully
  - All dependencies installed via Maven

## Project Architecture

### Technology Stack
- **Language**: Java 17
- **Framework**: Spring Boot 3.2.0
- **Database**: SQLite (using Hibernate ORM)
- **Build Tool**: Maven
- **Key Libraries**:
  - Spring Data JPA for database operations
  - OpenCSV for CSV parsing
  - FuzzyWuzzy for fuzzy string matching
  - Spring WebFlux for HTTP client operations

### Directory Structure
```
├── src/
│   ├── main/
│   │   ├── java/com/spade/codingscreen/
│   │   │   ├── CodingScreenApplication.java    # Main entry point
│   │   │   ├── controller/
│   │   │   │   └── SolutionController.java     # REST endpoint (NEEDS IMPLEMENTATION)
│   │   │   ├── model/
│   │   │   │   ├── Corporation.java            # Corporation entity
│   │   │   │   ├── Location.java               # Location entity
│   │   │   │   └── Countries.java              # Country enum
│   │   │   ├── repository/
│   │   │   │   ├── CorporationRepository.java  # JPA repository
│   │   │   │   └── LocationRepository.java     # JPA repository
│   │   │   ├── dto/
│   │   │   │   ├── MerchantRequest.java        # Request DTO
│   │   │   │   └── SolutionResponse.java       # Response DTO
│   │   │   └── cli/
│   │   │       ├── LoadCsvCommand.java         # CSV data loader
│   │   │       └── CheckMatchRateCommand.java  # Match rate checker
│   │   └── resources/
│   │       └── application.properties          # Configuration
│   └── test/
│       └── java/com/spade/codingscreen/
│           ├── SolutionControllerTest.java
│           └── DatabaseTest.java
├── data/                                       # CSV data files (if present)
├── pom.xml                                     # Maven dependencies
└── README.md                                   # Detailed project documentation
```

### Server Configuration
- **Port**: 5000
- **Host**: 0.0.0.0 (configured for Replit proxy environment)
- **Database**: SQLite file-based database (`db.sqlite3`)
- **API Endpoint**: POST `/solution/`

## How to Use

### Running the Application
The application runs automatically via the configured workflow. To restart:
1. Use the Replit workflow controls
2. Or run manually: `./mvnw spring-boot:run`

### Loading Data
To load CSV data into the database:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--load-csv"
```

### Checking Match Rate
To verify your implementation's accuracy:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--check-match-rate"
```

### Running Tests
```bash
./mvnw test
```

### Making Requests
Example API request:
```bash
curl -X POST "http://localhost:5000/solution/" \
  -H "Content-Type: application/json" \
  -d '{"merchantName":"Walgreens","address":"4433 Dewey Ave","city":"Rochester","region":"NY","postalCode":"14616"}'
```

## Development Notes

### What Needs to be Implemented
The core merchant matching logic in `SolutionController.java` needs to be completed. The goal is to:
1. Match incoming merchant requests to the correct Location and Corporation in the database
2. Use fuzzy matching algorithms (FuzzyWuzzy library is available)
3. Consider address, city, region, and postal code in matching logic
4. Achieve 100% match rate on the test data set

### Available Tools
- **FuzzyWuzzy**: Already included for fuzzy string matching
- **Spring Data JPA**: Use repositories to query Location and Corporation entities
- **SQLite Database**: Pre-configured and ready to use

### Testing Strategy
1. Load the CSV data using the `--load-csv` command
2. Implement matching logic in `SolutionController.java`
3. Test with sample requests
4. Run the match rate checker to verify accuracy
5. Run unit tests with `./mvnw test`

## Replit-Specific Configuration
- Server configured to bind to 0.0.0.0 to work with Replit's proxy
- Workflow configured to automatically start the Spring Boot application
- Port 5000 is exposed for web preview
