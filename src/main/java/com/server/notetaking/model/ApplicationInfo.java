package com.server.notetaking.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class ApplicationInfo implements Serializable {

    @Id
    @Column(name = "APPID")
    private String appId;

    private String appName;

    private String appDescription;

    @OneToMany
    private List<Role> roles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> users;

    @CreationTimestamp
    private Date created;
}
