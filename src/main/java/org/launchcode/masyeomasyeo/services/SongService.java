package org.launchcode.masyeomasyeo.services;

import org.launchcode.masyeomasyeo.exceptions.RecordNotFoundException;
import org.launchcode.masyeomasyeo.models.Song;
import org.launchcode.masyeomasyeo.models.data.SongDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//This is the Service class to provide basic services for manipulating and retrieving Song.
// This works as a wrapper over SongDao. Song controller is referencing this service by @Autowired.
// It is better practice to use Service classes instead of Controller classes directly to handle database operations.

@Service
public class SongService {

    @Autowired // This means to get the bean called songDao
    // Which is auto-generated by Spring, we will use it to handle the data
    private SongDao songDao;

    public List<Song> getAllSongs(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Song> pagedResult = songDao.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Song>();
        }
    }

    public Song getSongById(int id) throws RecordNotFoundException
    {
        Optional<Song> song = songDao.findById(id);

        if(song.isPresent()) {
            return song.get();
        } else {
            throw new RecordNotFoundException("No song record exist for given id");
        }
    }

    public Song createOrUpdateSong(Song entity) throws RecordNotFoundException
    {
        Optional<Song> song = songDao.findById(entity.getId());

        if(song.isPresent())
        {
            Song newEntity = song.get();
            newEntity.setTempo(entity.getTempo());
            newEntity.setMkey(entity.getMkey());
            newEntity.setName(entity.getName());

            newEntity = songDao.save(newEntity);

            return newEntity;
        } else {
            entity = songDao.save(entity);

            return entity;
        }
    }

    public void deleteSongById(int id) throws RecordNotFoundException
    {
        Optional<Song> song = songDao.findById(id);

        if(song.isPresent())
        {
            songDao.deleteById(id);
        } else {
            throw new RecordNotFoundException("No song record exist for given id");
        }
    }

}