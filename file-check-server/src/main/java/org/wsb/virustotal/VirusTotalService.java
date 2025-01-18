package org.wsb.virustotal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class VirusTotalService {

    private static final String VIRUS_TOTAL_URI = "https://www.virustotal.com/api/v3";
    private static final String VIRUS_TOTAL_FILES_URI = VIRUS_TOTAL_URI + "/files";
    private static final String VIRUS_TOTAL_ANALYSES_URI = VIRUS_TOTAL_URI + "/analyses";

    public boolean verify(MultipartFile file) throws IOException, InterruptedException {
        String id = uploadFile(file);
        System.out.println(id);
        for(int i=0;i<14;i++) {
            LinkedHashMap<String, LinkedHashMap> rsp = getReport(id);
            LinkedHashMap<String, LinkedHashMap> rspData = rsp.get("data");
            LinkedHashMap<String, Object> rspDataAttr = rspData.get("attributes");
            String rspDataAttrStatus = (String) rspDataAttr.get("status");
            if(!Objects.equals(rspDataAttrStatus, "completed")) {
                Thread.sleep(5000L);
                System.out.println("Waiting for the analysis");
            } else {
                System.out.println("Analysis completed!");
                LinkedHashMap<String, Integer> rspDataAttrStats = (LinkedHashMap) rspDataAttr.get("stats");
                return rspDataAttrStats.get("malicious") <= 0;
            }
        }
        throw new RuntimeException("Could not scan");
    }

    private String uploadFile(MultipartFile file) throws IOException {
        RestTemplate virusTotalClient = new RestTemplate();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("x-apikey", "ba7e2864100bfef2d70cb3116a715321b5966dc6e2a8bf2d27a39110403e9aab");

        HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(body, headers);
        LinkedHashMap<String, LinkedHashMap> rsp = virusTotalClient.postForObject(VIRUS_TOTAL_FILES_URI, req, LinkedHashMap.class);
        LinkedHashMap<String, String> rspData = rsp.get("data");
        return rspData.get("id");
    }

    private LinkedHashMap<String, LinkedHashMap> getReport(String id) {
        RestTemplate virusTotalClient = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-apikey", "ba7e2864100bfef2d70cb3116a715321b5966dc6e2a8bf2d27a39110403e9aab");
        HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(headers);
        ResponseEntity<LinkedHashMap> rsp = virusTotalClient.exchange(VIRUS_TOTAL_ANALYSES_URI + "/" + id, HttpMethod.GET, req,  LinkedHashMap.class);
        return rsp.getBody();
    }
}
