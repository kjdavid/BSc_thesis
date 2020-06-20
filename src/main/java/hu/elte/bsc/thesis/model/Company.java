/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.model;

/**
 *
 * @author kjdavid
 */
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "COMPANIES")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Company extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String companyName;

    //@JsonIgnoreProperties({"company"})
    //@JsonIgnore
    @OneToMany(mappedBy = "company")
    private Set<CompanyItem> companyItem = new HashSet<CompanyItem>();

    @OneToMany(mappedBy = "company")
    private Set<Store> stores = new HashSet<Store>();

    @OneToMany(fetch = FetchType.EAGER, targetEntity = User.class)
    private Set<User> users = new HashSet<User>();

    @Override
    public int hashCode(){
        return this.getId().intValue();
    }
}
