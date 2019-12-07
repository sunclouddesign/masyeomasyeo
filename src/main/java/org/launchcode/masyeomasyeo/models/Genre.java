package org.launchcode.masyeomasyeo.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "genres")
public class Genre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private Set<Song> songs = new HashSet<>();
    /*private List<Song> songs = new ArrayList<>();*/

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

    public Genre(@NotNull String name) {
        this.name = name;
    }

    public Genre() {
    }
}
