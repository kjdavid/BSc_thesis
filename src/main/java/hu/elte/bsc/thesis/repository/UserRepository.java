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

import hu.elte.bsc.thesis.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    @Query(value="SELECT u FROM users u, users_generated_codes ugc, registrationcodes r  WHERE u.id=ugc.user_id AND ugc.generated_codes_id=r.id AND r.code = ?1", nativeQuery = true)
    Optional<User> findUserByGeneratedCode(String code); //Get the user who used the specific registration code
    
    @Query(value="SELECT u1 FROM users u1, users u2, users_generated_codes ugc WHERE u1.registration_code_id = ugc.generated_codes_id AND u2.id = ugc.user_id AND u2.username= ?1", nativeQuery = true)
    Iterable<User> findByInviter(String inviterUsername); //Get all users who used the specific user's registration code

    @Query(value = "Select max(COALESCE(u.id,0)) from User u")
    Optional<Long> findMaxId();

    //User findBySession(String sessionId);
}