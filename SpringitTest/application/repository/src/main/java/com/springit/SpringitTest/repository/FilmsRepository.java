package com.springit.SpringitTest.repository;

import com.springit.SpringitTest.domain.Films;
import org.springframework.data.repository.CrudRepository;

public interface FilmsRepository extends CrudRepository<Films, String> {

}
