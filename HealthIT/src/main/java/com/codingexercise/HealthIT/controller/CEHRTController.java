package com.codingexercise.HealthIT.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.codingexercise.HealthIT.model.MeaningfulUseAcceleration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class CEHRTController {

	@GetMapping("/api/{year}")
	public ResponseEntity<List<MeaningfulUseAcceleration>> getEligibleAndCriticalAccessHospitals(
			@PathVariable int year, @RequestParam(value = "region", required = false) String region) {
		
		RestTemplate restTemplate = new RestTemplate();
		List<MeaningfulUseAcceleration> list = new ArrayList<MeaningfulUseAcceleration>();

		String externalUrl = "https://www.healthit.gov/data/open-api?source=Meaningful-Use-Acceleration-Scorecard.csv&period=" + year;
		if(region != null && region.trim().length() > 0) {	// region is optional
			externalUrl += "&region=" + region;
		}
		
		try {
			// json string as response
			String jsonStr = restTemplate.getForObject(externalUrl, String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(jsonStr);
			Iterator<Map.Entry<String, JsonNode>> iter = root.fields();
	        while (iter.hasNext()) {	// iterating json string to convert it into POJO
	            Map.Entry<String, JsonNode> entry = iter.next();
	            MeaningfulUseAcceleration obj = mapper.readValue(entry.getValue().toString(), MeaningfulUseAcceleration.class);
	            obj.setId(entry.getKey());
	            list.add(obj);
	        }
	        // sorting list by REGION in DESC order
	        list = list.stream()
	        		.sorted(Comparator.comparing(MeaningfulUseAcceleration::getRegion, Comparator.reverseOrder()))
	        		.collect(Collectors.toList());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}		
		return ResponseEntity.ok().body(list);
	}
}
