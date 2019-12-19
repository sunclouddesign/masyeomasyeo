package org.launchcode.masyeomasyeo.models.data;

import org.launchcode.masyeomasyeo.models.Genre;
import org.launchcode.masyeomasyeo.models.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called songDao
// CRUD refers Create, Read, Update, Delete

@Repository
@Transactional
public interface SongDao extends JpaRepository<Song, Integer> {

    @Query("Select s from Song s where s.mkey = ?1 and s.tempo > ?2 and s.tempo < ?3")
    List<Song> findRecs(String mkey, Integer tempo1, Integer tempo2);

}
