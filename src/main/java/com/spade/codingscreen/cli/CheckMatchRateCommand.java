package com.spade.codingscreen.cli;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CLI command to check the match rate of the solution endpoint.
 * Equivalent to Django's check_match_rate management command.
 */
@Component
public class CheckMatchRateCommand {

    private final ObjectMapper objectMapper;

    public CheckMatchRateCommand() {
        this.objectMapper = new ObjectMapper();
    }

    public void run(String[] args) {
        String baseUrl = "http://localhost:5000";

        // Parse arguments
        for (int i = 0; i < args.length; i++) {
            if ("--base-url".equals(args[i]) && i + 1 < args.length) {
                baseUrl = args[++i];
            }
        }

        String solutionUrl = baseUrl + "/solution/";

        // Load test requests from JSON file
        Path requestsPath = Paths.get("data/requests.json");
        Path truthPath = Paths.get("data/truth_set.json");

        List<Map<String, Object>> testRequests;
        Map<String, JsonNode> truthSet;

        try {
            if (!Files.exists(requestsPath)) {
                System.err.println("ERROR: Could not find " + requestsPath + " file");
                return;
            }
            testRequests = objectMapper.readValue(requestsPath.toFile(),
                new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            System.err.println("ERROR: Invalid JSON in " + requestsPath + " file: " + e.getMessage());
            return;
        }

        try {
            if (!Files.exists(truthPath)) {
                System.err.println("ERROR: Could not find " + truthPath + " file. " +
                    "Generate it first with the appropriate command.");
                return;
            }
            truthSet = objectMapper.readValue(truthPath.toFile(),
                new TypeReference<Map<String, JsonNode>>() {});
        } catch (IOException e) {
            System.err.println("ERROR: Invalid JSON in " + truthPath + " file: " + e.getMessage());
            return;
        }

        int totalRequests = testRequests.size();
        int successfulMatches = 0;
        int failedRequests = 0;
        int mismatchedIds = 0;

        System.out.println("Testing " + totalRequests + " requests against " + solutionUrl);
        System.out.println("-".repeat(50));

        WebClient webClient = WebClient.create();

        for (int i = 0; i < testRequests.size(); i++) {
            Map<String, Object> requestData = testRequests.get(i);
            String requestId = (String) requestData.get("requestId");
            String merchantName = (String) requestData.get("merchantName");

            Map<String, String> merchantData = new HashMap<>();
            merchantData.put("merchantName", merchantName);
            merchantData.put("address", (String) requestData.get("address"));
            merchantData.put("city", (String) requestData.get("city"));
            merchantData.put("region", (String) requestData.get("region"));
            merchantData.put("postalCode", (String) requestData.get("postalCode"));

            try {
                JsonNode response = webClient.post()
                    .uri(solutionUrl)
                    .header("Content-Type", "application/json")
                    .bodyValue(merchantData)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

                if (response == null) {
                    failedRequests++;
                    System.out.println("✗ Request " + (i + 1) + " (" + requestId + "): " + merchantName + " - No response");
                    continue;
                }

                boolean hasCorporation = response.has("corporation");
                boolean hasLocation = response.has("location");

                if (!(hasCorporation && hasLocation)) {
                    failedRequests++;
                    System.out.println("✗ Request " + (i + 1) + " (" + requestId + "): " + merchantName + " - Missing corporation or location");
                    continue;
                }

                JsonNode corporationNode = response.get("corporation");
                JsonNode locationNode = response.get("location");

                String corporationId = corporationNode.has("id") ? corporationNode.get("id").asText() : null;
                String locationId = locationNode.has("id") ? locationNode.get("id").asText() : null;

                if (corporationId == null || locationId == null) {
                    failedRequests++;
                    System.out.println("✗ Request " + (i + 1) + " (" + requestId + "): " + merchantName + " - Missing IDs");
                    continue;
                }

                JsonNode expected = truthSet.get(requestId);
                if (expected == null) {
                    failedRequests++;
                    System.out.println("✗ Request " + (i + 1) + " (" + requestId + "): " + merchantName + " - No truth mapping available");
                    continue;
                }

                String expectedCorpId = expected.get("corporation").get("id").asText();
                String expectedLocId = expected.get("location").get("id").asText();

                if (corporationId.equals(expectedCorpId) && locationId.equals(expectedLocId)) {
                    successfulMatches++;
                    System.out.println("✓ Request " + (i + 1) + " (" + requestId + "): " + merchantName + " - ID MATCH");
                } else {
                    mismatchedIds++;
                    System.out.println("✗ Request " + (i + 1) + " (" + requestId + "): " + merchantName + " - ID MISMATCH " +
                        "(expected corp=" + expectedCorpId + ", loc=" + expectedLocId + "; " +
                        "got corp=" + corporationId + ", loc=" + locationId + ")");
                }

            } catch (Exception e) {
                failedRequests++;
                System.out.println("✗ Request " + (i + 1) + ": " + merchantName + " - Error: " + e.getMessage());
            }
        }

        System.out.println("-".repeat(50));
        System.out.println("Total requests: " + totalRequests);
        System.out.println("Successful exact ID matches: " + successfulMatches);
        System.out.println("ID mismatches: " + mismatchedIds);
        System.out.println("Failed requests (HTTP/errors/missing data): " + failedRequests);

        if (totalRequests > 0) {
            double matchRate = ((double) successfulMatches / totalRequests) * 100;
            System.out.printf("Exact ID match rate: %.2f%%%n", matchRate);
        } else {
            System.err.println("No requests to process");
        }
    }
}

