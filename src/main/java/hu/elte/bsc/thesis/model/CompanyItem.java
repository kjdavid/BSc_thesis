/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author kjdavid <kjdavid96 at gmail.com>
 */
@Entity
@Data
@Table(name = "CompanyItem")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CompanyItem extends BaseEntity {
    @JsonIgnoreProperties({"companyItem","stores","users"})
    @ManyToOne
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID", referencedColumnName = "id")
    private Item item;

    @JsonIgnore
    @OneToMany(mappedBy = "companyItem")
    private Set<StoreItem> storeItem = new HashSet<>();

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private int purchasePrice;

    @Column(columnDefinition="TEXT")
    private String description;
    
    @Column(columnDefinition="TEXT")
    private String base64str;

    @Override
    public int hashCode(){
        return this.getId().intValue();
    }
}
