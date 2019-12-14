package org.launchcode.masyeomasyeo.models;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

    @XmlRootElement(name="songs")
    @XmlAccessorType(XmlAccessType.FIELD)
    public class Songs {

        @XmlElement(name="song")
        private List<Song> songs = null;

        public List<Song> getSongs() {
            return songs;
        }

        public void setSongs(List<Song> songs) {
            this.songs = songs;
        }
    }

