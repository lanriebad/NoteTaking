package com.server.notetaking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
@Entity
public class Note implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Size(max = 32)
    private String title;
    @NotNull
    @Column(name = "content", columnDefinition = "varchar(300)")
    private String content;
    private String username;
    private String appId;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "label_id")
    @JsonIgnore
    private Label label;


}
