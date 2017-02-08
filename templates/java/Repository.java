package com.thd.{{.ProjectName}}.repository;

import com.thd.{{.ProjectName}}.domain.{{.ClassName}};
import org.springframework.data.repository.CrudRepository;

public interface {{.ClassName}}Repository extends CrudRepository<{{.ClassName}}, String> {

}
