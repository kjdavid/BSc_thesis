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

import javax.persistence.*;

@Entity
@Table(name = "STORES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity {
    @Column(nullable = false)
    private String storeName;
    
    @Column(nullable = false)
    private String address;

    //@JsonIgnore
    @JsonIgnoreProperties({"store"})
    @OneToMany(mappedBy = "store")
    private Set<StoreItem> storeItem = new HashSet<StoreItem>();

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "id")
    @JsonIgnore
    private Company company;

    @JsonIgnore
    @OneToMany(mappedBy = "store")
    private Set<User> users = new HashSet<>();

    @Override
    public int hashCode(){
        return this.getId().intValue();
    }
}
