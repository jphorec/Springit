package com.springit.testuserservice.domain;

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
@Table(name = "player")
@Data
public class Player implements Identifiable<Integer> {


    @Id
    @Column(name = "p_id")
    private Integer pId;

    
    @Column(name = "t_id")
    private Integer tId;

    
    @Column(name = "l_id")
    private Integer lId;

    
    @Column(name = "first_name")
    private String firstName;

    
    @Column(name = "last_name")
    private String lastName;

    
    @Column(name = "jersey_number")
    private Integer jerseyNumber;

    
    @Column(name = "email_address")
    private String emailAddress;

    
    @Column(name = "pw")
    private String pw;

    
    @Column(name = "contact_number")
    private String contactNumber;

    
    @Column(name = "emergency_contact_name")
    private String emergencyContact;

    
    @Column(name = "emeergency_contact_number")
    private Integer emeergencyContact;


  @Override
  @JsonIgnore
  public Integer getId() {
    return pId;
  }
}
