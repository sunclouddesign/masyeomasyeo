package org.launchcode.masyeomasyeo.controllers;

import org.launchcode.masyeomasyeo.exceptions.RecordNotFoundException;
import org.launchcode.masyeomasyeo.models.Genre;
import org.launchcode.masyeomasyeo.models.Song;
import org.launchcode.masyeomasyeo.models.data.GenreDao;
import org.launchcode.masyeomasyeo.models.data.SongDao;
import org.launchcode.masyeomasyeo.services.GenreService;
import org.launchcode.masyeomasyeo.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("genre")
public class GenreController {

    @Autowired
    private GenreDao genreDao;

/*    @Autowired
    private GenreService genreService;*/

    @Autowired
    private SongService songService;

    // Request path: /genre
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("genres", genreDao.findAll());
        model.addAttribute("title","Music Genres");

        return "genre/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddGenreForm(Model model) {
        model.addAttribute("title", "Add Genre");
        model.addAttribute(new Genre());
        return "genre/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddGenreForm(@ModelAttribute @Valid Genre newGenre,
                                         Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Genre");
            return "genre/add";
        }

        genreDao.save(newGenre);
        return "redirect:";
    }

    @RequestMapping(value = "{gen}")
    public String viewGenre(Model model,
                            @PathVariable int gen,
                            @RequestParam(defaultValue = "0") Integer pageNo,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "id") String sortBy,
                            @RequestParam(defaultValue = "1") Integer asc) {

        Genre aGenre = genreDao.findById(gen).orElse(null);
        List<Song> list = songService.getAllSongs(pageNo, pageSize, sortBy, asc);
        model.addAttribute("songs",list);
        // TODO: Replace null with exception above
        model.addAttribute("title","Songs in Genre: " + aGenre.getName());
        model.addAttribute("genre",aGenre);
        model.addAttribute("asc",asc);

        return "genre/single";

    }

}
