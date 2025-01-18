package org.wsb.mainserver;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Service
public class MainServerConnectionService {
    private static final String MAIN_SERVER_URI = "http://localhost:15001/storage/upload";

    public boolean upload(MultipartFile file, String owner) {
        RestTemplate mainServerClient = new RestTemplate();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        body.add("owner", owner);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBasicAuth("file-check-server", "file-check-server-password");

        HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(body, headers);
        ResponseEntity<String> rsp = mainServerClient.exchange(MAIN_SERVER_URI, HttpMethod.POST, req, String.class);
        return rsp.getStatusCode().is2xxSuccessful();
    }
}
