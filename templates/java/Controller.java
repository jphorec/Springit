package com.thd.{{.ProjectName}}.rest.controller;

import com.thd.{{.ProjectName}}.domain.{{.Table}};
import com.thd.{{.ProjectName}}.repository.{{.Table}}Repository;
import com.thd.{{.ProjectName}}.service.hateoas.GenericResourceAssembler;
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
@RequestMapping("/{{.Table}}s")
public class {{.Table}}Controller {
    private {{.Table}}Repository {{.Table}}Repository;
    private GenericResourceAssembler<{{.Table}}, String> resourceAssembler;

    @Autowired
    public {{.Table}}Controller(
            final {{.Table}}Repository {{.Table}}Repository,
            final GenericResourceAssembler<{{.Table}}, String> resourceAssembler) {

        this.{{.Table}}Repository = {{.Table}}Repository;
        this.resourceAssembler = resourceAssembler;
    }

    /* CREATE METHOD*/
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Resource<{{.Table}}>> create{{.Table}}(@Valid final {{.Table}} {{.Table}}) {

        if ({{.Table}}Repository.exists({{.Table}}.getId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            {{.Table}}Repository.save({{.Table}});
            return new ResponseEntity<>(resourceAssembler.toResource({{.Table}}), HttpStatus.CREATED);
        }
    }

    /* READ */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Resource<{{.Table}}>> get{{.Table}}ById(@PathVariable("id") final String id) {

        {{.Table}} {{.Table}} = {{.Table}}Repository.findOne(id);
        if ({{.Table}} == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resourceAssembler.toResource({{.Table}}), HttpStatus.OK);
        }
    }

    /* UPDATE */
    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    public HttpEntity<Resource<{{.Table}}>> update{{.Table}}ById(@Valid final {{.Table}} {{.Table}}) {

        if ({{.Table}}Repository.exists({{.Table}}.getId())) {
            {{.Table}}Repository.save({{.Table}});
            //create date should not be overridden - returning newly updated user
            //TODO look at this more closely
            {{.Table}} updated{{.Table}} = {{.Table}}Repository.findOne({{.Table}}.getId());
            return new ResponseEntity<>(resourceAssembler.toResource(updated{{.Table}}), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /* DELETE */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public HttpEntity<Resource> delete{{.Table}}ById(@PathVariable("id") final String id) {

        if ({{.Table}}Repository.exists(id)) {
            {{.Table}}Repository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
