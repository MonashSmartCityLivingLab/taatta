package edu.monash.humanise.smartcity.collector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Builder
@AllArgsConstructor
public class Router {
    @NonNull
    private final Message<?> message;

    private final boolean isDocker = Objects.equals(System.getenv("TAATTA_DOCKER"), "1");

    public void route() {
        String payload = message.getPayload().toString();
        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);
        assert topic != null;
        log.info("New message. Topic: {}, payload: {}", topic, payload);

        if (topic.startsWith("athom-smart-plug")) {
            // Topic structure: <hostname>/sensors/<sensor_name>/state
            String[] topicComponents = topic.split("/");
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceName", topicComponents[0]);
                jsonObject.put("data", payload);
                jsonObject.put("sensor", topicComponents[2]);

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<?> request = new HttpEntity<>(jsonObject.toString(), headers);
                String url = String.format("http://%s:4050/api/payload", isDocker? "athom-smart-plug" : "localhost");
                ResponseEntity<?> response = restTemplate.postForEntity(url, request, String.class);
                log.info("Response from {} is : {}",topicComponents[0], response);
            } catch (JSONException e) {
                log.error("Cannot create JSON object for smart plug", e);
                throw new RuntimeException(e);
            }
        } else {
            // from chirpstack
            try {
                JSONObject jsonObject = new JSONObject(payload);
                if (jsonObject.has("data")) {
                    RestTemplate restTemplate = new RestTemplate();

                    MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
                    Map map = new HashMap<String, String>();
                    map.put("Content-Type", "application/json");
                    headers.setAll(map);

                    HttpEntity<?> request = new HttpEntity<>(payload, headers);
                    String deviceProfile = jsonObject.getString("deviceProfileName");

                    if (deviceProfile.endsWith("rhf1s001")) {
                        String url = String.format("http://%s:4040/api/payload", isDocker? "rhf1s001" : "localhost");
                        ResponseEntity<?> response = restTemplate.postForEntity(url, request, String.class);
                        log.info("Response from {} is : {}", deviceProfile, response.toString());
                    } else if (deviceProfile.endsWith("wqm101")) {
                        String url = String.format("http://%s:4042/api/payload", isDocker? "wqm101" : "localhost");
                        ResponseEntity<?> response = restTemplate.postForEntity(url, request, String.class);
                        log.info("Response from {} is : {}", deviceProfile, response.toString());
                    } else if (deviceProfile.endsWith("df702")) {
                        String url = String.format("http://%s:4044/api/payload", isDocker? "df702" : "localhost");
                        ResponseEntity<?> response = restTemplate.postForEntity(url, request, String.class);
                        log.info("Response from {} is : {}", deviceProfile, response.toString());
                    } else if (deviceProfile.endsWith("tbs220")) {
                        String url = String.format("http://%s:4046/api/payload", isDocker? "tbs220" : "localhost");
                        ResponseEntity<?> response = restTemplate.postForEntity(url, request, String.class);
                        log.info("Response from {} is : {}", deviceProfile, response.toString());
                    } else if (deviceProfile.endsWith("pcr2")) {
                        String url = String.format("http://%s:4048/api/payload", isDocker? "pcr2" : "localhost");
                        ResponseEntity<?> response = restTemplate.postForEntity(url, request, String.class);
                        log.info("Response from {} is : {}", deviceProfile, response.toString());
                    } else {
                        log.error("This device type {} not implemented yet.", deviceProfile);
                    }
                } else {
                    log.warn("No data field found.");
                }
            } catch (JSONException e) {
                log.error("Cannot parse JSON", e);
                throw new RuntimeException(e);
            }
        }
    }
}
