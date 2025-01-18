package org.wsb.api;

import jakarta.persistence.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
@Secured("ROLE_CHECK")
@RequiredArgsConstructor
public class FileCheckApi {
    private final FileCheckService fileCheckService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if(fileCheckService.check(file)) {
                //System.out.println("NOT A MALWARE");
                if(fileCheckService.send(file)){
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("File:" + file.getOriginalFilename() + " checked successfully. File has been saved!");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("File: " + file.getOriginalFilename() + " is not malware but failed to save!");
                }

            } else {
                //System.out.println("IT IS MALWARE");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("File: " + file.getOriginalFilename() + " is not valid! Malware!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not check the file: " + file.getOriginalFilename() + "! File has not been saved!");
        }
    }
}
