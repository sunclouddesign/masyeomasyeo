package org.launchcode.masyeomasyeo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "artists")
/*public class Artist {*/
public class Artist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "artists", fetch = FetchType.LAZY)
    @JsonBackReference
    /*private List<Song> songs = new ArrayList<>();*/
    private Set<Song> songs = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    /*public List<Song> getSongs() {
        return songs;
    }*/

    public Set<Song> getSongs() {
        return songs;
    }

    public void addItem(Song item) {
        songs.add(item);
    }

    public Artist(@NotNull String name) {

        this.name = name;
    }

    public Artist() {
    }
}
