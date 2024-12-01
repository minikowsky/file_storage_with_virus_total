package org.wsb.api;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FileDTO {
    private String id;
    private LocalDate created;
    private String uri;
}
