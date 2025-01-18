package org.wsb.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.wsb.auth.AuthenticationHelper;
import org.wsb.db.FileEntity;
import org.wsb.db.FileRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FileService {
    private final FileRepository repository;

    public void store(MultipartFile file, String owner) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileEntity enitity = FileEntity.builder()
                .name(fileName)
                .type(file.getContentType())
                .data(file.getBytes())
                .owner(owner)
                .build();
        repository.save(enitity);
    }

    public List<FileDTO> getUserFiles() {
        final String user = AuthenticationHelper.getUser();

        return repository.findAllByOwner(user)
                .stream()
                .map(fileEntity -> mapListItem(fileEntity, user))
                .toList();
    }

    public Optional<FileDTO> getUserFile(String id) {
        return mapFileData(repository.findById(id));
    }

    FileDTO mapListItem(FileEntity fileEntity, String owner) {
        return FileDTO.builder()
                .url(fileEntity.getId())
                .name(fileEntity.getName())
                .type(fileEntity.getType())
                .owner(owner)
                .build();
    }

    Optional<FileDTO> mapFileData(Optional<FileEntity> fileEntity) {
        if(fileEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                FileDTO.builder()
                        .data(fileEntity.get().getData())
                        .name(fileEntity.get().getName())
                .build());
    }
}
