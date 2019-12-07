package org.launchcode.masyeomasyeo.controllers;

import org.launchcode.masyeomasyeo.models.Genre;
import org.launchcode.masyeomasyeo.models.data.GenreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("genre")
public class GenreController {

    @Autowired
    private GenreDao genreDao;

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

}
