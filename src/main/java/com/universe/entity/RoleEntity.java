package com.universe.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude
    private List<UserEntity> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "roles_authorities",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "authority_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Collection<AuthorityEntity> authorities = new ArrayList<>();

    public void addAuthority(AuthorityEntity authorityEntity) {
        this.authorities.add(authorityEntity);
        authorityEntity.getRoles().add(this);
    }

    public void removeAuthority(AuthorityEntity authorityEntity) {
        this.authorities.remove(authorityEntity);
        authorityEntity.getRoles().remove(this);
    }
}
