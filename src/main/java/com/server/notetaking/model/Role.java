package com.server.notetaking.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@Entity
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROLEID")
    private Long Id;
    private String roleName;
    @CreationTimestamp
    private Date created;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "APPID")
    private ApplicationInfo applicationInfo;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> users;
}
