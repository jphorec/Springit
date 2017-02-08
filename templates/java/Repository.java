package com.{{.Company}}.{{.ProjectName}}.repository;

import com.{{.Company}}.{{.ProjectName}}.domain.{{.ClassName}};
import org.springframework.data.repository.CrudRepository;

public interface {{.ClassName}}Repository extends CrudRepository<{{.ClassName}}, String> {

}
