/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.repository;

/**
 *
 * @author kjdavid <kjdavid96 at gmail.com>
 */
import hu.elte.bsc.thesis.model.Company;
import hu.elte.bsc.thesis.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
public interface CompanyRepository extends CrudRepository<Company, Long> {
    Optional<Company> findById(Long id);
    
    Optional<Company> findByCompanyName(String companyName);

    @Query(value="SELECT c.id, c.company_name, c.version FROM COMPANIES c, USERS u, COMPANIES_USERS cu WHERE cu.USERS_ID = u.id AND c.id = cu.COMPANY_ID AND u.id = ?1 ", nativeQuery = true)
    Optional<Company> findByUserId(Long uId);
    
    @Query(value="SELECT u FROM COMPANIES c, USERS u, COMPANIES_USERS cu WHERE cu.USERS_ID = u.id AND cu.COMPANY_ID = c.id AND c.id = ?1 ", nativeQuery = true)
    Iterable<User> findUsersByCompanyId(Long cId);

    @Query(value = "Select max(COALESCE(c.id,0)) from Company c")
    Optional<Long> findMaxId();
    
    Iterable<Company> findAll();
}

