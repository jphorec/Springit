package com.springit.SpringitTest.rest.controller;

import com.thd.SpringitTest.domain.films;
import com.thd.SpringitTest.repository.filmsRepository;
import com.thd.SpringitTest.service.hateoas.GenericResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;

@RestController
@RequestMapping("/films")
public class FilmsController {
    private FilmsRepository FilmsRepository;
    private GenericResourceAssembler<Films, String> resourceAssembler;

    @Autowired
    public FilmsController(
            final FilmsRepository FilmsRepository,
            final GenericResourceAssembler<Films, String> resourceAssembler) {

        this.FilmsRepository = FilmsRepository;
        this.resourceAssembler = resourceAssembler;
    }

    /* CREATE METHOD*/
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Resource<Films>> createFilms(@Valid final Films Films) {

        if (filmsRepository.exists(films.getId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            filmsRepository.save(films);
            return new ResponseEntity<>(resourceAssembler.toResource(films), HttpStatus.CREATED);
        }
    }

    /* READ */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Resource<Films>> getFilmsById(@PathVariable("id") final String id) {

        Films Films = FilmsRepository.findOne(id);
        if (Films == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resourceAssembler.toResource(Films), HttpStatus.OK);
        }
    }

    /* UPDATE */
    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    public HttpEntity<Resource<Films>> updateFilmsById(@Valid final Films Films) {

        if (FilmsRepository.exists(Films.getId())) {
            FilmsRepository.save(Films);
            //create date should not be overridden - returning newly updated user
            //TODO look at this more closely
            Films updatedFilms = FilmsRepository.findOne(Films.getId());
            return new ResponseEntity<>(resourceAssembler.toResource(updatedFilms), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /* DELETE */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public HttpEntity<Resource> deleteFilmsById(@PathVariable("id") final String id) {

        if (FilmsRepository.exists(id)) {
            FilmsRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
