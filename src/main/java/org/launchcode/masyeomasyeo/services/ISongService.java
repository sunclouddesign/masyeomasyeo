package org.launchcode.masyeomasyeo.services;

import org.launchcode.masyeomasyeo.models.Song;

import java.util.List;

//TODO: Figure out why I have to have this go-between interface to Song Service
public interface ISongService {

    List<Song> findRecs(String mkey, Integer tempo1, Integer tempo2);
}
