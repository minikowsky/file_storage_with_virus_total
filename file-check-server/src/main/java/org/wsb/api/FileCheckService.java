package org.wsb.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wsb.auth.AuthenticationHelper;
import org.wsb.mainserver.MainServerConnectionService;
import org.wsb.virustotal.VirusTotalService;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class FileCheckService {
    private final VirusTotalService virusTotalService;
    private final MainServerConnectionService mainServerConnectionService;

    public boolean check(MultipartFile file) throws IOException, InterruptedException {
        //SEND TO VIRUS TOTAL TO CHECK
        return virusTotalService.verify(file);
    }

    public boolean send(MultipartFile file) {
        var owner = AuthenticationHelper.getUser();
        //SEND TO MAIN SERVER
        return mainServerConnectionService.upload(file, owner);
    }
}
