package com.spade.codingscreen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spade.codingscreen.dto.MerchantRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the SolutionController endpoint.
 * Equivalent to test_solution_endpoint_returns_stub_response in Python.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SolutionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSolutionEndpointReturnsStubResponse() throws Exception {
        MerchantRequest request = new MerchantRequest();
        request.setMerchantName("Test Merchant");
        request.setAddress("123 Test St");
        request.setCity("Test City");
        request.setRegion("TX");
        request.setPostalCode("12345");

        mockMvc.perform(post("/solution/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.detail").value("solution endpoint stub"))
            .andExpect(jsonPath("$.echo.merchantName").value("Test Merchant"))
            .andExpect(jsonPath("$.echo.address").value("123 Test St"))
            .andExpect(jsonPath("$.echo.city").value("Test City"))
            .andExpect(jsonPath("$.echo.region").value("TX"))
            .andExpect(jsonPath("$.echo.postalCode").value("12345"));
    }

    @Test
    public void testSolutionEndpointWithExampleRequest() throws Exception {
        // Test with the example payload from Python tests
        String payload = "{\"exampleRequest\": \"example\"}";

        MvcResult result = mockMvc.perform(post("/solution/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.detail").value("solution endpoint stub"))
            .andReturn();

        // Verify response contains echo of input
        String responseBody = result.getResponse().getContentAsString();
        assertEquals(true, responseBody.contains("echo"));
    }
}

