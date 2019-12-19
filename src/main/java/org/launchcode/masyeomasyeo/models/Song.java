package org.launchcode.masyeomasyeo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@XmlRootElement(name="song")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity // This tells Hibernate to make a table out of this class
// UniqueConstraints specify combinations of columns whose combined values serve as unique constraints
@Table(name = "songs", uniqueConstraints = {@UniqueConstraint(columnNames = {"name","tempo","mkey"})})
//@NamedQuery(name = "Song.findRecs", query = "SELECT e FROM Song e WHERE e.mkey = ?1 AND e.tempo = 34 ORDER BY name DESC")
/*@NamedQueries({
        @NamedQuery(name = "Song.findRecs",
                query = "Select s from Song s where s.mkey = ?1 and s.tempo > ?2 and s.tempo < ?3"),
        @NamedQuery(name="Song.findRecs2",
                query = "Select s from Song s"),
                })*/
public class Song implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private Integer tempo;

    private String mkey;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonManagedReference
    @JoinTable(name = "songs_genres",
            joinColumns = {
                    @JoinColumn(name = "song_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "genre_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})

    private Set<Genre> genres = new HashSet<>();

    // TODO: Figure out what (fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) means
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonManagedReference
    @JoinTable(name = "songs_artists",
            joinColumns = {
                    @JoinColumn(name = "song_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "artist_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<Artist> artists = new HashSet<>();


    public Song() {}

    public Song(@NotNull String name, @NotNull int tempo, String mkey) {
        this.name = name;
        this.tempo = tempo;
        this.mkey = mkey;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTempo() {
        return tempo;
    }

    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public Set<Genre> getGenres(){
        return genres;
    }

    public void setGenres(Set<Genre> genres){
        this.genres = genres;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }


}
