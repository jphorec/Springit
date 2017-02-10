package com.springit.testuserservice.repository;

import com.springit.testuserservice.domain.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

}
