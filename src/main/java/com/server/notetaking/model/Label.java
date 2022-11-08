package com.server.notetaking.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Entity
public class Label implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "label_id")
    private Long id;

    @NotNull
    @Size(max = 32)
    private String name;

    @OneToMany(fetch = FetchType.EAGER,cascade={CascadeType.ALL})
    @JoinTable(name="note_label",joinColumns = @JoinColumn(name="labelid",referencedColumnName ="label_id"),inverseJoinColumns = @JoinColumn(name="noteid",referencedColumnName ="id"))
    private List<Note> notes = new ArrayList<>();


}
