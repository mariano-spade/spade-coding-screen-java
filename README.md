# Spade Coding Screen (Java)

Welcome to your tech screen at Spade!

## Merchant Matching

Merchant matching is the process of taking a customer's merchant data and linking it to a Spade Location/Corporation pairing.

For example, the following merchant information:

```json
{
    "merchantName": "Walgreens",
    "address": "4433 Dewey Ave",
    "city": "Rochester",
    "region": "NY",
    "postalCode": "14616"
}
```

Corresponds to the Location/Corporation pair below:

```json
{
  "location": {
    "id": "4b22ad83-85d6-3144-898b-d27040118adc",
    "name": "Walgreens"
  },
  "corporation": {
    "id": "8eec1cf5-856a-48d5-80f7-1251a86a427e",
    "legal_name": "Walgreen Co.",
    "doing_business_as": "Walgreens"
  }
}
```

## What you'll implement

- Implement the merchant matching logic in `SolutionController.java` to reach a 100% match rate.
- You can check the match rate by running the check-match-rate command (see below).

## Getting Started

### 1. Build the project

```bash
./mvnw clean install -DskipTests
```

### 2. Start the server

```bash
./mvnw spring-boot:run
```

The server will start on `http://localhost:5000`.

### 3. Check match rate

**Important:** The server must already be running (step 3) before you check the match rate. Open a **second terminal** and run:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--check-match-rate"
```

This will send requests to `http://localhost:5000/solution/` and compare the responses against the truth set.

To specify a different base URL (e.g., if your server is running on a different port):

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--check-match-rate,--base-url,http://localhost:8080"
```

### 4. Run tests

```bash
./mvnw test
```

## Example request

```bash
curl -X POST "http://localhost:5000/solution/" \
 -H "Content-Type: application/json" \
 -d '{"merchantName":"Walgreens","address":"4433 Dewey Ave","city":"Rochester","region":"NY","postalCode":"14616"}'
```

## Project Structure

```
root/
├── pom.xml                          # Maven dependencies
├── src/main/java/com/spade/codingscreen/
│   ├── CodingScreenApplication.java # Main entry point
│   ├── controller/
│   │   └── SolutionController.java  # REST endpoint (implement your solution here)
│   ├── model/
│   │   ├── Corporation.java         # Corporation entity
│   │   ├── Location.java            # Location entity
│   │   └── Countries.java           # Country enum
│   ├── repository/
│   │   ├── CorporationRepository.java
│   │   └── LocationRepository.java
│   ├── dto/
│   │   ├── MerchantRequest.java     # Request DTO
│   │   └── SolutionResponse.java    # Response DTO
│   └── cli/
│       ├── LoadCsvCommand.java      # CSV data loader
│       └── CheckMatchRateCommand.java # Match rate checker
└── src/test/java/com/spade/codingscreen/
    ├── SolutionControllerTest.java
    └── DatabaseTest.java
```

## Data

The data files are located in the `data/` directory:
- `corporations.csv` - Corporation data
- `locations.csv` - Location data
- `requests.json` - Test requests
- `truth_set.json` - Expected answers for validation

## Supplemental Material

The following resources may be helpful as you work on this project:

- [FuzzyWuzzy (Java)](https://github.com/xdrop/fuzzywuzzy): Useful for fuzzy string matching tasks.

## Key Classes to Modify

Your solution should be implemented in:

**`src/main/java/com/spade/codingscreen/controller/SolutionController.java`**

The `solution()` method receives a `MerchantRequest` and should return a response containing the matched `location` and `corporation` with their IDs.

