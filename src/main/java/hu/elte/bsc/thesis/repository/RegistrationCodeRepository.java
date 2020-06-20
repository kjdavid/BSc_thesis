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
import hu.elte.bsc.thesis.model.RegistrationCode;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
public interface RegistrationCodeRepository extends CrudRepository<RegistrationCode, Long> {
    
    Optional<RegistrationCode> findByCode(String code);

    @Query(value = "Select max(COALESCE(rc.id,0)) from RegistrationCode rc")
    Optional<Long> findMaxId();

}
