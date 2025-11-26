package com.spade.codingscreen.controller;

import com.spade.codingscreen.dto.MerchantRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller for the merchant matching solution endpoint.
 */
@RestController
public class SolutionController {

    @PostMapping("/solution/")
    public ResponseEntity<Map<String, Object>> solution(@RequestBody MerchantRequest request) {
        // TODO: Your solution goes here
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("detail", "solution endpoint stub");
        response.put("echo", request);
        return ResponseEntity.ok(response);
    }
}

