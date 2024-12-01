package org.wsb.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/file")
@Secured("ROLE_STORAGE")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping
    private List<FileDTO> getFiles() {

        return null;
    }


}
