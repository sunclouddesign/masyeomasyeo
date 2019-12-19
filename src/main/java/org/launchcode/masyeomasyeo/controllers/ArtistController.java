package org.launchcode.masyeomasyeo.controllers;

import org.launchcode.masyeomasyeo.models.Artist;
import org.launchcode.masyeomasyeo.models.Genre;
import org.launchcode.masyeomasyeo.models.Song;
import org.launchcode.masyeomasyeo.models.data.ArtistDao;
import org.launchcode.masyeomasyeo.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("artist")
public class ArtistController {

    @Autowired
    private ArtistDao artistDao;

    @Autowired
    private SongService songService;

    // Request path: /artist
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("artists", artistDao.findAll());
        model.addAttribute("title","Artists");

        return "artist/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddGenreForm(Model model) {
        model.addAttribute("title", "Add Artist");
        model.addAttribute(new Artist());
        return "artist/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddGenreForm(@ModelAttribute @Valid Artist newArtist,
                                         Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Artist");
            return "artist/add";
        }

        artistDao.save(newArtist);
        return "redirect:";
    }

    @RequestMapping(value = "{art}")
    public String viewArtist(Model model,
                            @PathVariable int art,
                            @RequestParam(defaultValue = "0") Integer pageNo,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "id") String sortBy,
                             @RequestParam(defaultValue ="1") Integer asc) {

        Artist anArtist = artistDao.findById(art).orElse(null);
        List<Song> list = songService.getAllSongs(pageNo, pageSize, sortBy,asc);
        model.addAttribute("songs",list);
        // TODO: Replace null with exception above
        model.addAttribute("title","Songs with artist: " + anArtist.getName());
        model.addAttribute("artist",anArtist);
        model.addAttribute("asc",asc);

        return "artist/single";

    }

}
