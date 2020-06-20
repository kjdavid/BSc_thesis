/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.repository;

import hu.elte.bsc.thesis.model.CompanyItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author kjdavid <kjdavid96 at gmail.com>
 */
public interface CompanyItemRepository extends CrudRepository<CompanyItem, Long>  {
    Iterable<CompanyItem> findAll();
    
    @Query(value="SELECT i.ID, i.BARCODE, ci.COMPANY_ID, ci.ITEM_NAME, ci.PURCHASE_PRICE, ci.DESCRIPTION, ci.BASE64STR FROM COMPANY_ITEM ci, ITEMS i WHERE i.ID = ci.ITEM_ID AND COMPANY_ID = ?1;", nativeQuery = true)
    Iterable<CompanyItem> findByCompanyId(Long companyId);

    @Query(value = "Select max(COALESCE(ci.id,0)) from CompanyItem ci")
    Optional<Long> findMaxId();
}
