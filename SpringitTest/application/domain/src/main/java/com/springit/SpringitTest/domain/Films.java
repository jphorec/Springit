package com.springit.SpringitTest.domain;

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
@Table(name = "films")
@Data
public class Films implements Identifiable<String> {


    @Id
    @Column(name = "code")
    private String code;

    
    @Column(name = "title")
    private String title;

    
    @Column(name = "did")
    private Integer did;

    
    @Column(name = "date_prod")
    private String dateProd;

    
    @Column(name = "kind")
    private String kind;

    
    @Column(name = "len")
    private String len;


}
