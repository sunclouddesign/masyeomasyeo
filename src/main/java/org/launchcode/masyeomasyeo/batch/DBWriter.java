package org.launchcode.masyeomasyeo.batch;

import org.launchcode.masyeomasyeo.models.Song;
import org.launchcode.masyeomasyeo.models.data.SongDao;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<Song> {

    @Autowired
    private SongDao songDao;

    @Override
    public void write(List<? extends Song> songs) throws Exception {

        System.out.println("Data Saved for Songs: " + songs);
        songDao.saveAll(songs);
    }
}