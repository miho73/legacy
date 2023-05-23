package com.github.miho73.legacy.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
@NoArgsConstructor
@Table(schema = "data", name = "uploads")
public class Files {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    @Length(min = 24, message = "{validation.name.size.too_short}")
    @Length(max = 24, message = "{validation.name.size.too_long}")
    @Column(name = "hash", nullable = false)
    private String fileHash;
    
    @Column(name = "datum", nullable = false)
    private byte[] data;

    @Column(name = "file_name", nullable = false)
    private String fileName;
}
