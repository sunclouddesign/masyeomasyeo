package org.launchcode.masyeomasyeo.controllers;

import org.launchcode.masyeomasyeo.models.Artist;
import org.launchcode.masyeomasyeo.models.Genre;
import org.launchcode.masyeomasyeo.models.Song;
import org.launchcode.masyeomasyeo.models.data.ArtistDao;
import org.launchcode.masyeomasyeo.models.data.GenreDao;
import org.launchcode.masyeomasyeo.models.data.SongDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class SongController {

    @Autowired
    private SongDao songDao;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private ArtistDao artistDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("songs", songDao.findAll());
        model.addAttribute("genres", genreDao.findAll());
        model.addAttribute("artists", artistDao.findAll());
        model.addAttribute("title","Public Database");

        return "song/index";
    }

    @RequestMapping(value = "song/add", method = RequestMethod.GET)
    public String displayAddSongForm(Model model) {
        model.addAttribute("title", "Add Song");
        model.addAttribute(new Song());
        model.addAttribute("genres", genreDao.findAll());
        model.addAttribute("artists", artistDao.findAll());
        return "song/add";
    }

@RequestMapping(value = "song/add", method = RequestMethod.POST)
    public String processAddSongForm(Model model, @ModelAttribute @Valid Song newSong,
                      Errors errors, @RequestParam int[] genre_id,
                      @RequestParam int[] artist_id) {

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


}
