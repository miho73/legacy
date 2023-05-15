package com.github.miho73.legacy.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table(schema = "identities", name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    @Length(min = 2, message = "{validation.name.size.too_short}")
    @Length(max = 10, message = "{validation.name.size.too_long}")
    @Column(name = "name", nullable = false)
    private String name;

    @Min(value = 1000, message = "code too small")
    @Max(value = 9999, message = "code too big")
    @Column(name = "code", nullable = false)
    private int code;

    @Length(min = 4, message = "{validation.name.size.too_short}")
    @Length(max = 20, message = "{validation.name.size.too_long}")
    @Column(name = "id", nullable = false)
    private String id;

    @Length(min = 6, message = "{validation.name.size.too_short}")
    @Length(max = 120, message = "{validation.name.size.too_long}")
    @Column(name = "pwd", nullable = false)
    private String pwd;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Column(name = "join_date", nullable = false)
    private Timestamp joinDate;

    @Column(name = "status", nullable = false)
    private USER_STATUS status;

    public enum USER_STATUS {
        UNACTIVATED,
        ACTIVATED,
        BANNED
    }
}
