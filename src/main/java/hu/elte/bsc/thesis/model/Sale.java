/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.model;

import java.sql.Date;
import javax.persistence.*;

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
@Table(name = "SALES")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Sale extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STORE_ITEM_ID", referencedColumnName = "id")
    private StoreItem storeItem;
    
    @Column(nullable=false)
    private int count;
    
    @Column(nullable=false)
    private Long sellingPrice; // 1 piece
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SELLER_ID", referencedColumnName = "id")
    @JsonIgnoreProperties({"email", "regCode", "store", "sales"})
    private User seller;
    
    @Column(nullable=false)
    private Date dateOfSale;
}
