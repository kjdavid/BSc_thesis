/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author kjdavid
 */
@Entity
@Table(name = "USERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private String regCode;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = RegistrationCode.class)
    private Set<RegistrationCode> generatedCodes = new HashSet<>();

    @OneToMany(mappedBy = "seller")
    @JsonIgnore
    private Set<Sale> sales = new HashSet<Sale>();

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "STORE_ID", referencedColumnName="id")
    @JsonIgnoreProperties({"storeItem"})
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public enum Role {
        ADMIN, COMPANY_ADMIN, SELLER
    }
    @Override
    public int hashCode(){
        return this.getId().intValue();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof User){
            return ((User) o).getId()==this.getId();
        }else{
            return false;
        }
    }
}
