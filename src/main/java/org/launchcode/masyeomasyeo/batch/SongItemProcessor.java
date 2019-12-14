package org.launchcode.masyeomasyeo.batch;

import org.launchcode.masyeomasyeo.models.Song;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class SongItemProcessor implements ItemProcessor<Song, Song> {

    @Override
    public Song process(Song song) throws Exception {
        return song;
    }

}