package org.launchcode.masyeomasyeo.controllers;

import org.launchcode.masyeomasyeo.exceptions.RecordNotFoundException;
import org.launchcode.masyeomasyeo.models.Artist;
import org.launchcode.masyeomasyeo.models.Genre;
import org.launchcode.masyeomasyeo.models.Song;
import org.launchcode.masyeomasyeo.models.data.ArtistDao;
import org.launchcode.masyeomasyeo.models.data.GenreDao;
import org.launchcode.masyeomasyeo.models.data.SongDao;
import org.launchcode.masyeomasyeo.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller // This means that this class is a Controller
public class SongController {
// TODO: remove Daos from controllers and keep them in services
    @Autowired // This means to get the bean called songDao
    // Which is auto-generated by Spring, we will use it to handle the data
    private SongDao songDao;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private ArtistDao artistDao;

    @Autowired
    private SongService songService;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("songs", songDao.findAll());
        model.addAttribute("genres", genreDao.findAll());
        model.addAttribute("artists", artistDao.findAll());
        model.addAttribute("title","Song Database");

        return "song/list";
    }

    @GetMapping(value = "song/list") // Map only GET requests
    public String getSongs(
            Model model,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "1") Integer asc)
    {
        // custom method songService.getAllSongs replaces songDao.findAll to enable sorting & pagination
        List<Song> list = songService.getAllSongs(pageNo, pageSize, sortBy, asc);
        model.addAttribute("songs",list);
        model.addAttribute("genres", genreDao.findAll());
        model.addAttribute("artists", artistDao.findAll());
        model.addAttribute("title","Song Database");
        model.addAttribute("asc",asc);
        return "song/list";

    }

    @RequestMapping(value = "song/{id}")
    public String displaySingleSong(Model model, @PathVariable int id) throws RecordNotFoundException {
        Song aSong = songDao.findById(id).orElse(null);
        Integer songtempo = aSong.getTempo();
        String songkey = aSong.getMkey();
        Integer tempo1 = songtempo -30;
        Integer tempo2 = songtempo + 30;
        //var songlist = (List<Song>) songService.getRecommendations(8);
        var songlist = (List<Song>) songService.findRecs(songkey,tempo1,tempo2);
        model.addAttribute("song", aSong);
        model.addAttribute("genres", genreDao.findAll());
        model.addAttribute("artists", artistDao.findAll());
        model.addAttribute("title", " Recommendations for Song " + aSong.getName());
        model.addAttribute("songlist",songlist);
        return "song/single";
    }

    @RequestMapping(value = "song/add", method = RequestMethod.GET)
    public String displayAddSongForm(Model model) {
        model.addAttribute("title", "Add Song");
        model.addAttribute(new Song());
        model.addAttribute("genres", genreDao.findAll());
        model.addAttribute("artists", artistDao.findAll());
        return "song/add";
    }

    @PostMapping(value = "song/add") // Map ONLY POST Requests
    public String processAddSongForm(Model model, @ModelAttribute @Valid Song newSong,
                      Errors errors, @RequestParam int[] genre_id,
                      @RequestParam int[] artist_id) {

        // @RequestParam means it is a parameter from the GET or POST request

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Song");
            model.addAttribute(new Song());
            model.addAttribute("genres", genreDao.findAll());
            model.addAttribute("artists", artistDao.findAll());
            return "song/add";
        }

        for (int artistId : artist_id) {
            Artist art = artistDao.findById(artistId).orElse(null);
            newSong.getArtists().add(art);
        }
        for (int genreId : genre_id) {
            Genre aGen = genreDao.findById(genreId).orElse(null);
            newSong.getGenres().add(aGen);
        }

        songDao.save(newSong);
        System.out.println(newSong);
        System.out.println(songDao.findAll());
        return "redirect:";
    }

    @RequestMapping(value = "song/edit/{id}", method = RequestMethod.GET)
    public String displayEditSongForm(Model model, @PathVariable int id) {
        model.addAttribute("title", "Edit Song");
        Song aSong = songDao.findById(id).orElse(null);
        model.addAttribute("song", aSong);
        model.addAttribute("genres", genreDao.findAll());
        model.addAttribute("artists", artistDao.findAll());
        return "song/edit";
    }

    @RequestMapping(value = "song/edit/{id}", method = RequestMethod.POST)
    public String processEditSongForm(@RequestParam int id,
                                      @RequestParam String name,
                                      @RequestParam String mkey,
                                      @RequestParam int tempo,
                                      @RequestParam int[] genre_id,
                                      @RequestParam int[] artist_id ) {

        // TODO: add error messages to update form
        /*if (errors.hasErrors()) {
            return "redirect:";

            model.addAttribute("title", "Edit Song");
            Song aSong = songDao.findById(id).orElse(null);
            model.addAttribute("song", aSong);
            model.addAttribute("genres", genreDao.findAll());
            model.addAttribute("artists", artistDao.findAll());
            return "song/edit";
        }*/

        Song updateSong = songDao.findById(id).orElse(null);
        updateSong.setName(name);
        updateSong.setMkey(mkey);
        updateSong.setTempo(tempo);

        // To update and existing record, we first delete all genre and artist mappings
        updateSong.getGenres().removeAll(genreDao.findAll());

        updateSong.getArtists().removeAll((Collection<?>) artistDao.findAll());

        // Then add the checked ones
        for (int artistId : artist_id) {
            Artist art = artistDao.findById(artistId).orElse(null);
            updateSong.getArtists().add(art);
        }
        for (int genreId : genre_id) {
            Genre aGen = genreDao.findById(genreId).orElse(null);
            updateSong.getGenres().add(aGen);
        }

        songDao.save(updateSong);
        //System.out.println(updateSong);
        //System.out.println(songDao.findAll());
        return "redirect:/";
    }

    @RequestMapping(value = "song/delete/{id}")
    public String deleteSong(@PathVariable int id) {

        Song aSong = songDao.findById(id).orElse(null);
        songDao.delete(aSong);

        return "redirect:/";
    }


}
