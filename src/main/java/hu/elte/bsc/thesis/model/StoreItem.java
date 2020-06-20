/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author kjdavid <kjdavid96 at gmail.com>
 */
@Entity
@Data
@Table(name = "StoreItem")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StoreItem extends BaseEntity {

    //@JsonIgnoreProperties({"storeItem"})
    @ManyToOne
    @JoinColumn(name = "STORE_ID", referencedColumnName="id")
    @JsonIgnore
    private Store store;

    //@JsonIgnoreProperties({"storeItem"})
    @ManyToOne
    @JoinColumn(name = "COMPANY_ITEM_ID", referencedColumnName = "id")
    private CompanyItem companyItem;
    
    @Column(nullable=false)
    private Integer sellingPrice;
    
    @Column(nullable=false)
    private Double discount;
    
    @Column(nullable = false)
    private Integer stock;

    @Override
    public int hashCode(){
        return this.getId().intValue();
    }
}
