package org.wsb.api;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class FileDTO {
    private String url;

    private String name;

    private String owner;

    private String type;

    private byte[] data;
}
