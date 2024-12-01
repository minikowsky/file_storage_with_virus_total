package org.wsb.api;

import lombok.RequiredArgsConstructor;
import org.wsb.auth.AuthenticationHelper;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class FileService {

    public List<FileDTO> getUserFiles() {
        final String user = AuthenticationHelper.getUser();


        return Collections.emptyList();
    }

}
