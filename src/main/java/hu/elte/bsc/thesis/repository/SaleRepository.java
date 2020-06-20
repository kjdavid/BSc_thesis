package hu.elte.bsc.thesis.repository;

import hu.elte.bsc.thesis.model.Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.Optional;

public interface SaleRepository extends CrudRepository<Sale, Long> {
    @Query(value = "Select max(COALESCE(s.id,0)) from Sale s")
    Optional<Long> findMaxId();

    @Override
    Optional<Sale> findById(Long saleId);

    Optional<Sale> findBySellerId(Long sellerId);

    @Query(value = "SELECT s.id,s.version,s.count,s.selling_price,s.date_of_sale,s.store_item_id,s.seller_id FROM SALES s, STORE_ITEM si, STORES where s.STORE_ITEM_ID = si.ID AND si.STORE_ID=STORES.ID AND s.DATE_OF_SALE >=?1 AND s.DATE_OF_SALE <= ?2 AND STORES.COMPANY_ID =?3", nativeQuery = true)
    Iterable<Sale> findAllBetweenAtCompany(Date from, Date to, Long companyId);

    @Query(value = "SELECT s.id,s.version,s.count,s.selling_price,s.date_of_sale,s.store_item_id,s.seller_id FROM SALES s, STORE_ITEM si where s.STORE_ITEM_ID = si.ID AND si.STORE_ID=?3 AND s.DATE_OF_SALE >=?1 AND s.DATE_OF_SALE <= ?2", nativeQuery = true)
    Iterable<Sale> findAllBetweenAtStore(Date from, Date to, Long storeId);
}
