/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.service;

import hu.elte.bsc.thesis.communcation.*;
import hu.elte.bsc.thesis.model.*;
import hu.elte.bsc.thesis.repository.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

/**
 * @author kjdavid <kjdavid96 at gmail.com>
 */

@Service
@SessionScope
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemService {
    @Setter
    private ErrorResponse error;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CompanyItemRepository companyItemRepository;

    @Autowired
    private StoreItemRepository storeItemRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyItem addItemToCompany(AddItemToCompanyRq addItemToCompanyRq, Authentication authentication) {//Fixme Nezd at!!! Validáció, kellene!!!
        Optional<Item> optItem = itemRepository.findByBarcode(addItemToCompanyRq.getBarcode());
        Company company = companyService.getLoggedInUserCompany(authentication);
        Item item;
        if (optItem.isPresent()) {
            item = optItem.get();
            for (CompanyItem ci : item.getCompanyItem()) {
                if (ci.getCompany().getId() == company.getId()) {
                    error = ErrorResponse.ITEM_ALREADY_ADDED;
                    return null;
                }
            }
        } else {
            Set<CompanyItem> companyItemSet = new HashSet<>();
            item = new Item();
            item.setId(itemRepository.findMaxId().orElse(0L) + 1);
            item.setBarcode(addItemToCompanyRq.getBarcode()); //FIXME
            item.setCompanyItem(companyItemSet);
        }
        CompanyItem companyItem = new CompanyItem();
        companyItem.setDescription(addItemToCompanyRq.getDescription()); //FIXME
        companyItem.setItemName(addItemToCompanyRq.getItemName()); //FIXME
        companyItem.setStoreItem(new HashSet<>());
        companyItem.setPurchasePrice(addItemToCompanyRq.getPurchasePrice()); //FIXME
        companyItem.setCompany(company);
        companyItem.setBase64str(addItemToCompanyRq.getBase64str());
        companyItem.setId(companyItemRepository.findMaxId().orElse(0L) + 1);
        itemRepository.save(item);
        companyItem.setItem(item);
        companyItemRepository.save(companyItem);
        item.getCompanyItem().add(companyItem);
        itemRepository.save(item);
        return companyItem;
    }

    public StoreItem addItemToStore(AddItemToStoreRq addItemToStoreRq, Authentication authentication) {
        CompanyItem companyItem = companyItemRepository.findById(addItemToStoreRq.getCompanyItemId()).orElse(null);
        if (companyItem == null) {
            error = ErrorResponse.NO_ENTITY_WITH_THE_GIVEN_ID;
            return null;
        }
        Store store = storeRepository.findById(addItemToStoreRq.getStoreId()).orElse(null);
        if (store == null) {
            error = ErrorResponse.NO_ENTITY_WITH_THE_GIVEN_ID;
            return null;
        }
        Company company = companyService.getLoggedInUserCompany(authentication);
        if (!companyItem.getCompany().equals(company)) {
            error = ErrorResponse.PERMISSION_DENIED;
            return null;
        }
        User user = userService.getLoggedInUser(authentication);
        if (userService.getLoggedInUser(authentication).getRole().equals(User.Role.SELLER) && !user.getStore().equals(store)) {
            error = ErrorResponse.PERMISSION_DENIED;
            return null;
        }
        for (StoreItem si : companyItem.getStoreItem()) {
            if (si.getStore().getId() == addItemToStoreRq.getStoreId()) {
                error = ErrorResponse.ITEM_ALREADY_ADDED;
                return null;
            }
        }
        StoreItem storeItem = new StoreItem();
        storeItem.setId(storeItemRepository.findMaxId().orElse(0L)+1);
        storeItem.setStock(addItemToStoreRq.getStock());
        storeItem.setSellingPrice(addItemToStoreRq.getSellingPrice());
        storeItem.setDiscount(addItemToStoreRq.getDiscount());
        storeItem.setCompanyItem(companyItem);
        storeItem.setStore(store);
        storeItemRepository.save(storeItem);
        return storeItem;
    }

    public Set<CompanyItem> getItemsToCompany(Long companyId, Authentication authentication) {
        Company company = companyService.getLoggedInUserCompany(authentication);
        if (company.getId() != companyId) {
            error = ErrorResponse.PERMISSION_DENIED;
            return null;
        }
        return company.getCompanyItem();
    }

    public Set<StoreItem> getItemsToStore(Long storeId, Authentication authentication) {
        Company company = companyService.getLoggedInUserCompany(authentication);
        for (Store store : company.getStores()) {
            if (store.getId() == storeId) {
                return store.getStoreItem();
            }
        }
        error = ErrorResponse.PERMISSION_DENIED;
        return null;
    }

    public CompanyItem editCompanyItem(EditCompanyItemRq editCompanyItemRq, Authentication authentication) {
        CompanyItem companyItem = companyItemRepository.findById(editCompanyItemRq.getId()).orElse(null);
        if (companyItem == null) {
            error = ErrorResponse.NO_ENTITY_WITH_THE_GIVEN_ID;
            return null;
        }
        Company company = companyService.getLoggedInUserCompany(authentication);
        if (!companyItem.getCompany().equals(company)) {
            error = ErrorResponse.PERMISSION_DENIED;
            return null;
        }
        companyItem.setItemName(editCompanyItemRq.getItemName());
        companyItem.setDescription(editCompanyItemRq.getDescription());
        companyItem.setBase64str(editCompanyItemRq.getBase64str());
        companyItem.setPurchasePrice(editCompanyItemRq.getPurchasePrice());
        companyItemRepository.save(companyItem);
        return companyItem;
    }

    public StoreItem editStoreItem(EditStoreItemRq editStoreItemRq, Authentication authentication) {
        StoreItem storeItem = storeItemRepository.findById(editStoreItemRq.getId()).orElse(null);
        if (storeItem == null) {
            error = ErrorResponse.NO_ENTITY_WITH_THE_GIVEN_ID;
            return null;
        }
        Company company = companyService.getLoggedInUserCompany(authentication);
        if (!storeItem.getStore().getCompany().equals(company)) {
            error = ErrorResponse.PERMISSION_DENIED;
            return null;
        }
        User user = userService.getLoggedInUser(authentication);
        if (userService.getLoggedInUser(authentication).getRole().equals(User.Role.SELLER) && !user.getStore().equals(storeItem.getStore())) {
            error = ErrorResponse.PERMISSION_DENIED;
            return null;
        }
        storeItem.setDiscount(editStoreItemRq.getDiscount());
        storeItem.setSellingPrice(editStoreItemRq.getSellingPrice());
        storeItem.setStock(editStoreItemRq.getStock());
        storeItemRepository.save(storeItem);
        return storeItem;
    }
    public StoreItem orderItem(OrderItemRq orderItemRq, Authentication authentication){
        StoreItem storeItem = storeItemRepository.findById(orderItemRq.getStoreItemId()).orElse(null);
        if (storeItem == null) {
            error = ErrorResponse.NO_ENTITY_WITH_THE_GIVEN_ID;
            return null;
        }
        Company company = companyService.getLoggedInUserCompany(authentication);
        if (!storeItem.getStore().getCompany().equals(company)) {
            error = ErrorResponse.PERMISSION_DENIED;
            return null;
        }
        User user = userService.getLoggedInUser(authentication);
        if (userService.getLoggedInUser(authentication).getRole().equals(User.Role.SELLER) && !user.getStore().equals(storeItem.getStore())) {
            error = ErrorResponse.PERMISSION_DENIED;
            return null;
        }
        storeItem.setStock(storeItem.getStock()+orderItemRq.getAmount());
        storeItemRepository.save(storeItem);
        return storeItem;
    }
}
