package com.thd.{{.ProjectName}}.repository;

import com.thd.{{.ProjectName}}.domain.{{.Table}};
import org.springframework.data.repository.CrudRepository;

public interface {{.Table}}Repository extends CrudRepository<{{.Table}}, String> {

}
