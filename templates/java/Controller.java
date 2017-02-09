package com.{{.Company}}.{{.ProjectName}}.rest.controller;

import com.{{.Company}}.{{.ProjectName}}.domain.{{.ClassName}};
import com.{{.Company}}.{{.ProjectName}}.repository.{{.ClassName}}Repository;
import com.{{.Company}}.{{.ProjectName}}.service.hateoas.GenericResourceAssembler;
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
@RequestMapping("/{{.Table}}")
public class {{.ClassName}}Controller {
    private {{.ClassName}}Repository {{.ClassName}}Repository;
    private GenericResourceAssembler<{{.ClassName}}, {{.PrimaryType}}> resourceAssembler;

    @Autowired
    public {{.ClassName}}Controller(
            final {{.ClassName}}Repository {{.ClassName}}Repository,
            final GenericResourceAssembler<{{.ClassName}}, {{.PrimaryType}}> resourceAssembler) {

        this.{{.ClassName}}Repository = {{.ClassName}}Repository;
        this.resourceAssembler = resourceAssembler;
    }

    /* CREATE METHOD*/
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Resource<{{.ClassName}}>> create{{.ClassName}}(@Valid final {{.ClassName}} {{.LowerClass}}) {

        if ({{.ClassName}}Repository.exists({{.LowerClass}}.getId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            {{.ClassName}}Repository.save({{.LowerClass}});
            return new ResponseEntity<>(resourceAssembler.toResource({{.LowerClass}}), HttpStatus.CREATED);
        }
    }

    /* READ */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Resource<{{.ClassName}}>> get{{.ClassName}}ById(@PathVariable("id") final {{.PrimaryType}} id) {

        {{.ClassName}} {{.LowerClass}} = {{.ClassName}}Repository.findOne(id);
        if ({{.LowerClass}} == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resourceAssembler.toResource({{.LowerClass}}), HttpStatus.OK);
        }
    }

    /* UPDATE */
    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    public HttpEntity<Resource<{{.ClassName}}>> update{{.ClassName}}ById(@Valid final {{.ClassName}} {{.LowerClass}}) {

        if ({{.ClassName}}Repository.exists({{.LowerClass}}.getId())) {
            {{.ClassName}}Repository.save({{.LowerClass}});
            //create date should not be overridden - returning newly updated user
            //TODO look at this more closely
            {{.ClassName}} updated{{.ClassName}} = {{.ClassName}}Repository.findOne({{.LowerClass}}.getId());
            return new ResponseEntity<>(resourceAssembler.toResource(updated{{.ClassName}}), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /* DELETE */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public HttpEntity<Resource> delete{{.ClassName}}ById(@PathVariable("id") final {{.PrimaryType}} id) {

        if ({{.ClassName}}Repository.exists(id)) {
            {{.ClassName}}Repository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
