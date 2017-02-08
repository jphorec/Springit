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
@RequestMapping("/{{.Table}}")
public class {{.ClassName}}Controller {
    private {{.ClassName}}Repository {{.ClassName}}Repository;
    private GenericResourceAssembler<{{.ClassName}}, String> resourceAssembler;

    @Autowired
    public {{.ClassName}}Controller(
            final {{.ClassName}}Repository {{.ClassName}}Repository,
            final GenericResourceAssembler<{{.ClassName}}, String> resourceAssembler) {

        this.{{.ClassName}}Repository = {{.ClassName}}Repository;
        this.resourceAssembler = resourceAssembler;
    }

    /* CREATE METHOD*/
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Resource<{{.ClassName}}>> create{{.ClassName}}(@Valid final {{.ClassName}} {{.ClassName}}) {

        if ({{.Table}}Repository.exists({{.Table}}.getId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            {{.Table}}Repository.save({{.Table}});
            return new ResponseEntity<>(resourceAssembler.toResource({{.Table}}), HttpStatus.CREATED);
        }
    }

    /* READ */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Resource<{{.ClassName}}>> get{{.ClassName}}ById(@PathVariable("id") final String id) {

        {{.ClassName}} {{.ClassName}} = {{.ClassName}}Repository.findOne(id);
        if ({{.ClassName}} == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resourceAssembler.toResource({{.ClassName}}), HttpStatus.OK);
        }
    }

    /* UPDATE */
    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    public HttpEntity<Resource<{{.ClassName}}>> update{{.ClassName}}ById(@Valid final {{.ClassName}} {{.ClassName}}) {

        if ({{.ClassName}}Repository.exists({{.ClassName}}.getId())) {
            {{.ClassName}}Repository.save({{.ClassName}});
            //create date should not be overridden - returning newly updated user
            //TODO look at this more closely
            {{.ClassName}} updated{{.ClassName}} = {{.ClassName}}Repository.findOne({{.ClassName}}.getId());
            return new ResponseEntity<>(resourceAssembler.toResource(updated{{.ClassName}}), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /* DELETE */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public HttpEntity<Resource> delete{{.ClassName}}ById(@PathVariable("id") final String id) {

        if ({{.ClassName}}Repository.exists(id)) {
            {{.ClassName}}Repository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
