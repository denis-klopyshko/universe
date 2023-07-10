package com.universe.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@DiscriminatorValue("professor")
public class ProfessorEntity extends UserEntity {
    public ProfessorEntity() {

    }

    public String getFullName() {
        return String.format("%s %s", this.getFirstName(), this.getLastName());
    }
}
