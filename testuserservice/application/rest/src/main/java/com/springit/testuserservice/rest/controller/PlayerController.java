package com.springit.testuserservice.rest.controller;

import com.springit.testuserservice.domain.Player;
import com.springit.testuserservice.repository.PlayerRepository;
import com.springit.testuserservice.service.hateoas.GenericResourceAssembler;
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
@RequestMapping("/player")
public class PlayerController {
    private PlayerRepository PlayerRepository;
    private GenericResourceAssembler<Player, Integer> resourceAssembler;

    @Autowired
    public PlayerController(
            final PlayerRepository PlayerRepository,
            final GenericResourceAssembler<Player, Integer> resourceAssembler) {

        this.PlayerRepository = PlayerRepository;
        this.resourceAssembler = resourceAssembler;
    }

    /* CREATE METHOD*/
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Resource<Player>> createPlayer(@Valid final Player player) {

        if (PlayerRepository.exists(player.getId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            PlayerRepository.save(player);
            return new ResponseEntity<>(resourceAssembler.toResource(player), HttpStatus.CREATED);
        }
    }

    /* READ */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Resource<Player>> getPlayerById(@PathVariable("id") final Integer id) {

        Player player = PlayerRepository.findOne(id);
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resourceAssembler.toResource(player), HttpStatus.OK);
        }
    }

    /* UPDATE */
    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    public HttpEntity<Resource<Player>> updatePlayerById(@Valid final Player player) {

        if (PlayerRepository.exists(player.getId())) {
            PlayerRepository.save(player);
            //create date should not be overridden - returning newly updated user
            //TODO look at this more closely
            Player updatedPlayer = PlayerRepository.findOne(player.getId());
            return new ResponseEntity<>(resourceAssembler.toResource(updatedPlayer), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /* DELETE */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public HttpEntity<Resource> deletePlayerById(@PathVariable("id") final Integer id) {

        if (PlayerRepository.exists(id)) {
            PlayerRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
