package hu.elte.bsc.thesis.service;

import hu.elte.bsc.thesis.communcation.AddSaleRq;
import hu.elte.bsc.thesis.communcation.GetSalesByStoreRq;
import hu.elte.bsc.thesis.communcation.GetSalesRq;
import hu.elte.bsc.thesis.model.*;
import hu.elte.bsc.thesis.repository.SaleRepository;
import hu.elte.bsc.thesis.repository.StoreItemRepository;
import hu.elte.bsc.thesis.repository.StoreRepository;
import javafx.animation.PauseTransition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.sql.Date;
import java.util.List;

@Service
@SessionScope
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SaleService {
    @Setter
    private ErrorResponse error;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreItemRepository storeItemRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    public StoreItem addSale(AddSaleRq addSaleRq, Authentication authentication) {
        StoreItem storeItem = storeItemRepository.findById(addSaleRq.getStoreItemId()).orElse(null);
        if (storeItem == null) {
            error = ErrorResponse.NO_ENTITY_WITH_THE_GIVEN_ID;
            return null;
        }
        if (storeItem.getStock() < addSaleRq.getAmount()) {
            error = ErrorResponse.NOT_ENOUGH_ITEM;
            return null;
        }
        if (!companyService.getLoggedInUserCompany(authentication).getStores().contains(storeItem.getStore())) {
            error = ErrorResponse.PERMISSION_DENIED;
            return null;
        }
        Sale sale = new Sale();
        sale.setId(saleRepository.findMaxId().orElse(0L)+1);
        sale.setCount(addSaleRq.getAmount());
        sale.setDateOfSale(new Date(System.currentTimeMillis()));
        sale.setSeller(userService.getLoggedInUser(authentication));
        sale.setSellingPrice(Math.round(storeItem.getSellingPrice()-storeItem.getSellingPrice()*storeItem.getDiscount()/100));
        sale.setStoreItem(storeItem);
        saleRepository.save(sale);
        storeItem.setStock(storeItem.getStock()-addSaleRq.getAmount());
        storeItemRepository.save(storeItem);
        return storeItem;
    }
    public StoreItem removeSale(Long saleId, Authentication authentication){
        Sale sale = saleRepository.findById(saleId).orElse(null);
        if(sale == null){
            error = ErrorResponse.NO_ENTITY_WITH_THE_GIVEN_ID;
            return null;
        }
        if (!companyService.getLoggedInUserCompany(authentication).getStores().contains(sale.getStoreItem().getStore())) {
            error = ErrorResponse.PERMISSION_DENIED;
            return null;
        }
        sale.getStoreItem().setStock(sale.getStoreItem().getStock()+sale.getCount());
        storeItemRepository.save(sale.getStoreItem());
        saleRepository.delete(sale);
        return sale.getStoreItem();
    }
    public Iterable<Sale> getSalesBetween(GetSalesRq getSalesRq, Authentication authentication){
        List<Sale> sales= (List<Sale>) saleRepository.findAllBetweenAtCompany(getSalesRq.getFrom(),getSalesRq.getTo(),companyService.getLoggedInUserCompany(authentication).getId());
        removeDescriptionAndBase64Str(sales);
        return sales;
    }
    public Iterable<Sale> getSalesByStoreBetween(GetSalesByStoreRq getSalesByStoreRq, Authentication authentication){
        Company company = companyService.getLoggedInUserCompany(authentication);
        for (Store store : company.getStores()) {
            if(store.getId()==getSalesByStoreRq.getStoreId()){
                List<Sale> sales = (List<Sale>) saleRepository.findAllBetweenAtStore(getSalesByStoreRq.getFrom(),getSalesByStoreRq.getTo(),store.getId());
                removeDescriptionAndBase64Str(sales);
                return sales;
            }
        }
        if(storeItemRepository.findById(getSalesByStoreRq.getStoreId()).isPresent()){
            error=ErrorResponse.PERMISSION_DENIED;
        }else{
            error=ErrorResponse.NO_ENTITY_WITH_THE_GIVEN_ID;
        }
        return null;
    }
    private void removeDescriptionAndBase64Str(List<Sale> sales){
        for(Sale sale : sales){
            sale.getStoreItem().getCompanyItem().setBase64str("");
            sale.getStoreItem().getCompanyItem().setDescription("");
        }
    }
}
