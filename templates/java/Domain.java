package com.{{.Company}}.{{.ProjectName}}.domain;

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
public class {{.ClassName}} implements Identifiable<{{.PrimaryType}}> {

{{ range .Attributes }}
    {{if .Primary}}@Id{{end}}
    @Column(name = "{{.Column}}")
    private {{.AttributeType}} {{.AttributeName}};
{{ end }}

}
