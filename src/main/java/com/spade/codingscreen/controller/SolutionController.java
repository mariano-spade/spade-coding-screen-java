package com.spade.codingscreen.controller;

import com.spade.codingscreen.dto.MerchantRequest;
import com.spade.codingscreen.repository.CorporationRepository;
import com.spade.codingscreen.repository.LocationRepository;
import com.spade.codingscreen.model.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



/**
 * Controller for the merchant matching solution endpoint.
 */
@RestController
public class SolutionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolutionController.class);
    
    private final LocationRepository locationRepository;
    private final CorporationRepository corporationRepository;

    public SolutionController(LocationRepository locationRepository, CorporationRepository corporationRepository) {
        this.locationRepository = locationRepository;
        this.corporationRepository = corporationRepository;
    }

    @PostMapping("/solution/")
    public ResponseEntity<Map<String, Object>> solution(@RequestBody MerchantRequest request) {
        // TODO: Your solution goes here        
        // {
        //   "location": {
        //     "id": "4b22ad83-85d6-3144-898b-d27040118adc",
        //     "name": "Walgreens"
        //   },
        //   "corporation": {
        //     "id": "8eec1cf5-856a-48d5-80f7-1251a86a427e",
        //     "legal_name": "Walgreen Co.",
        //     "doing_business_as": "Walgreens"
        //   }
        // }
        var locations = this.locationRepository.findByStateAndCityAndPostalCode(request.getRegion(), request.getCity(), request.getPostalCode());
        

        LOGGER.info("Locations: " + locations.size());        
        var maybeLocation = locations.stream().findFirst();            

        // maybeLocation.map(location -> location.getCorporation().getDoingBusinessAs()))
        // var maybeCorporation = this.corporationRepository.findByDoingBusinessAs(request.getMerchantName())
        //     .map(corporation -> new CorporationResponse(corporation.getId(), corporation.getLegalName(), corporation.getDoingBusinessAs()));
            
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("location", maybeLocation.map(location -> new LocationResponse(location.getId(), location.getName()))
                     .orElse(null));
        response.put("corporation", maybeLocation.map(location -> location.getCorporation())
                     .map(corporation -> new CorporationResponse(corporation.getId(), corporation.getLegalName(), corporation.getDoingBusinessAs())));
        return ResponseEntity.ok(response);
    }

    private record LocationResponse(UUID id, String name) {


    }

    public record CorporationResponse(UUID id, String legalName, String doingBusinessAs) {
    
    }
}

