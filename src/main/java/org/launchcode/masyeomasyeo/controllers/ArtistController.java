package org.launchcode.masyeomasyeo.controllers;

import org.launchcode.masyeomasyeo.models.Artist;
import org.launchcode.masyeomasyeo.models.Genre;
import org.launchcode.masyeomasyeo.models.data.ArtistDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("artist")
public class ArtistController {

    @Autowired
    private ArtistDao artistDao;

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

}
