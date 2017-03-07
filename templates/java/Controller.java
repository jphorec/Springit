package com.{{.Company}}.{{.ProjectName}}.rest.controller;

import com.{{.Company}}.{{.ProjectName}}.domain.{{.ClassName}};
import com.{{.Company}}.{{.ProjectName}}.repository.{{.ClassName}}Repository;
import com.{{.Company}}.{{.ProjectName}}.service.hateoas.GenericResourceAssembler;
import com.{{.Company}}.{{.ProjectName}}.service.hateoas.GenericResourcesAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;

@RestController
@RequestMapping("/{{.Table}}")
public class {{.ClassName}}Controller {
    private {{.ClassName}}Repository {{.LowerClass}}Repository;
    private GenericResourceAssembler<{{.ClassName}}, {{.PrimaryType}}> resourceAssembler;
    private GenericResourcesAssembler<{{.ClassName}}> resourcesAssembler;

    @Autowired
    public {{.ClassName}}Controller(
            final {{.ClassName}}Repository {{.LowerClass}}Repository,
            final GenericResourceAssembler<{{.ClassName}}, {{.PrimaryType}}> resourceAssembler,
            final GenericResourcesAssembler<{{.ClassName}}> resourcesAssembler) {

        this.{{.LowerClass}}Repository = {{.LowerClass}}Repository;
        this.resourceAssembler = resourceAssembler;
        this.resourcesAssembler = resourcesAssembler;
    }

    /* GET ALL */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<Resources<Resource<{{.ClassName}}>>> getAll{{.ClassName}}s() {
        Iterable<{{.ClassName}}> {{.LowerClass}}s = {{.LowerClass}}Repository.findAll();
        return new ResponseEntity<>(resourcesAssembler.toResource({{.LowerClass}}s), HttpStatus.OK);
    }

    /* CREATE METHOD*/
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Resource<{{.ClassName}}>> create{{.ClassName}}(@Valid final {{.ClassName}} {{.LowerClass}}) {

        if ({{.LowerClass}}Repository.exists({{.LowerClass}}.getId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            {{.LowerClass}}Repository.save({{.LowerClass}});
            return new ResponseEntity<>(resourceAssembler.toResource({{.LowerClass}}), HttpStatus.CREATED);
        }
    }

    /* READ */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Resource<{{.ClassName}}>> get{{.ClassName}}ById(@PathVariable("id") final {{.PrimaryType}} id) {

        {{.ClassName}} {{.LowerClass}} = {{.LowerClass}}Repository.findOne(id);
        if ({{.LowerClass}} == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resourceAssembler.toResource({{.LowerClass}}), HttpStatus.OK);
        }
    }

    /* UPDATE */
    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    public HttpEntity<Resource<{{.ClassName}}>> update{{.ClassName}}ById(@Valid final {{.ClassName}} {{.LowerClass}}) {

        if ({{.LowerClass}}Repository.exists({{.LowerClass}}.getId())) {
            {{.LowerClass}}Repository.save({{.LowerClass}});
            //create date should not be overridden - returning newly updated user
            //TODO look at this more closely
            {{.ClassName}} updated{{.ClassName}} = {{.LowerClass}}Repository.findOne({{.LowerClass}}.getId());
            return new ResponseEntity<>(resourceAssembler.toResource(updated{{.ClassName}}), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /* DELETE */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public HttpEntity<Resource> delete{{.ClassName}}ById(@PathVariable("id") final {{.PrimaryType}} id) {

        if ({{.LowerClass}}Repository.exists(id)) {
            {{.LowerClass}}Repository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
