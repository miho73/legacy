package com.github.miho73.legacy.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table(schema = "data", name = "articles")
public class Articles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    @Length(min = 1,  message = "{validation.name.size.too_short}")
    @Length(max = 10, message = "{validation.name.size.too_long}")
    @Column(name = "name", nullable = false)
    private String name;

    @Length(min = 1,    message = "{validation.name.size.too_short}")
    @Length(max = 1000, message = "{validation.name.size.too_long}")
    @Column(name = "explain", nullable = false)
    private String explain;

    @Length(min = 24, message = "{validation.name.size.too_short}")
    @Length(max = 24, message = "{validation.name.size.too_long}")
    @Column(name = "file_hash", nullable = false)
    private String fileHash;

    @Column(name = "tags", nullable = false)
    private int tags;

    @Column(name = "date_up", nullable = false)
    private Timestamp dateUp;

    @Column(name = "last_down")
    private Timestamp lastDown;

    @Column(name = "downs", nullable = false)
    private int downloads;

    @Column(name = "uploaded_by", nullable = false)
    private int uploadedBy;
}
