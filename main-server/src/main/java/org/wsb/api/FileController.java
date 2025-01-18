package org.wsb.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/storage")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    @Secured("ROLE_UPLOAD")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String owner) {
        try {
            fileService.store(file, owner);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Uploaded the file successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload the file: " + file.getOriginalFilename() + "!");
        }
    }

    @GetMapping("/files")
    @Secured("ROLE_READ")
    public ResponseEntity<List<FileDTO>> getListFiles() {
        List<FileDTO> files = fileService.getUserFiles().stream().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/storage/files/")
                    .path(dbFile.getUrl())
                    .toUriString();

            return new FileDTO.FileDTOBuilder()
                    .url(fileDownloadUri)
                    .name(dbFile.getName())
                    .owner(dbFile.getOwner())
                    .type(dbFile.getType())
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        Optional<FileDTO> file = fileService.getUserFile(id);
        return file.map(fileDTO -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDTO.getName() + "\"")
                .body(fileDTO.getData())).orElseGet(() -> ResponseEntity.noContent().build());
    }

}
