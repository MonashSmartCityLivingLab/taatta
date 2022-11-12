package edu.monash.humanise.smartcity.collector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Builder
@AllArgsConstructor
public class Router {
    private String message;
    public void route(){
        log.info("The message receive from broker: {}", this.message);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(this.message);
            if(jsonObject.has("data") ) {
                RestTemplate restTemplate = new RestTemplate();

                MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
                Map map = new HashMap<String, String>();
                map.put("Content-Type", "application/json");
                headers.setAll(map);

                HttpEntity<?> request = new HttpEntity<>(this.message, headers);
                String deviceProfile = jsonObject.getString("deviceProfileName");

                if(deviceProfile.endsWith("rhf1s001")) {
                    ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:4040/api/payload", request, String.class);
                    log.info("Response from {} is : {}", deviceProfile, response.toString());
                } else if (deviceProfile.endsWith("wqm101")) {
                    ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:4042/api/payload", request, String.class);
                    log.info("Response from {} is : {}", deviceProfile, response.toString());
                } else if (deviceProfile.endsWith("df702")) {
                    ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:4044/api/payload", request, String.class);
                    log.info("Response from {} is : {}", deviceProfile, response.toString());
                } else if (deviceProfile.endsWith("tbs220")) {
                    ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:4046/api/payload", request, String.class);
                    log.info("Response from {} is : {}", deviceProfile, response.toString());
                } else if (deviceProfile.endsWith("pcr2")) {
                    ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:4048/api/payload", request, String.class);
                    log.info("Response from {} is : {}", deviceProfile, response.toString());
                } else {
                    log.error("This device type {} not implemented yet.", deviceProfile);
                }
            } else {
                log.warn("No data field found.");
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

}
