package org.wsb.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {
    @Id
    @UuidGenerator
    private String id;

    private String name;

    private String owner;

    private String type;

    @Lob
    byte[] data;

}
