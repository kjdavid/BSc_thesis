/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.service;

import hu.elte.bsc.thesis.communcation.AddStoreRq;
import hu.elte.bsc.thesis.communcation.EditCompanyRq;
import hu.elte.bsc.thesis.communcation.EditStoreRq;
import hu.elte.bsc.thesis.model.*;
import hu.elte.bsc.thesis.repository.CompanyRepository;
import hu.elte.bsc.thesis.repository.StoreRepository;
import hu.elte.bsc.thesis.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
public class CompanyService {
    @Setter
    private ErrorResponse error;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StoreRepository storeRepository;

    public List<Company> getCompanies() {
        ArrayList<Company> companies = (ArrayList<Company>) companyRepository.findAll();
        for(Company company : companies){
            company.setCompanyItem(new HashSet<>());
            company.setStores(new HashSet<>());
        }
        return companies;
    }

    private Long getLoggedInUserCompanyId(Authentication authentication) {
        Long userId = this.userRepository.findByUsername(authentication.getName()).get().getId();
        return companyRepository.findByUserId(userId).get().getId();
    }

    public Company getLoggedInUserCompany(Authentication authentication) {
        return companyRepository.findById(getLoggedInUserCompanyId(authentication)).get();
    }

    public List<User> getUsersToCompany(Authentication authentication,int cId) {
        return (List<User>) companyRepository.findUsersByCompanyId(getLoggedInUserCompanyId(authentication));
    }

    public Company addCompany(String companyName) {
        if (!companyRepository.findByCompanyName(companyName).isPresent()) {
            Company company = new Company();
            company.setId(companyRepository.findMaxId().orElse(0L) + 1);
            company.setCompanyName(companyName);
            companyRepository.save(company);
            return company;
        }
        error = ErrorResponse.NAME_TAKEN;
        return null;
    }

    public Company editCompany(EditCompanyRq editCompanyRq) {
        Company company = companyRepository.findById(editCompanyRq.getCompanyId()).orElse(null);
        if (company != null) {
            if (!companyRepository.findByCompanyName(editCompanyRq.getCompanyName()).isPresent()) {
                company.setCompanyName(editCompanyRq.getCompanyName());
                companyRepository.save(company);
                company.setStores(new HashSet<>());
                company.setCompanyItem(new HashSet<>());
                return company;
            }
            error = ErrorResponse.NAME_TAKEN;
            return null;
        }
        error = ErrorResponse.COMPANY_NOT_EXISTS;
        return null;
    }

    public Store addStore(AddStoreRq addStoreRq) {
        Company company = companyRepository.findById(addStoreRq.getCompanyId()).orElse(null);
        if (company != null) {
            Store store = new Store();
            store.setAddress(addStoreRq.getAddress());
            store.setStoreName(addStoreRq.getStoreName());
            store.setId(storeRepository.findMaxId().orElse(0L) + 1);
            store.setCompany(company);
            storeRepository.save(store);
            return store;
        }
        error = ErrorResponse.COMPANY_NOT_EXISTS;
        return null;
    }

    public Store editStore(EditStoreRq editStoreRq) {
        Store store = storeRepository.findById(editStoreRq.getStoreId()).orElse(null);
        if (store != null) {
            store.setAddress(editStoreRq.getAddress());
            store.setStoreName(editStoreRq.getStoreName());
            storeRepository.save(store);
            return store;
        }
        error = ErrorResponse.COMPANY_NOT_EXISTS;
        return null;
    }
}
