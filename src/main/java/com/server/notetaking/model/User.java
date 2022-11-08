package com.server.notetaking.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name="user_info",uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })

public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Id;

    @NotNull
    @Size(max = 32)
    private String name;

    private String password;

    @NotNull
    @Size(max = 32)
    private String surname;

    private String username;

    private String email;

    private String refreshToken;

    private Date refreshTokenExpiry;

    @CreationTimestamp
    private Date created;

    private String activeStatus;

    private int failedLoginAttempts;

    private Date lastLoginDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ROLEID")
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "APPID")
    private ApplicationInfo applicationInfo;



}
