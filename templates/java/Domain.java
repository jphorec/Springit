package com.thd.{{.Table}}.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.hateoas.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "{{.Table}}")
@Data
public class {{.Table}} implements Identifiable<String> {

{{ range .Attributes }}
    @Column(name = "{{.AttributeName}}")
    private {{.AttributeType}} {{.AttributeName}};
{{ end }}

}
