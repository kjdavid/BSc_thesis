/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.service;

/**
 * @author kjdavid <kjdavid96 at gmail.com>
 */

import hu.elte.bsc.thesis.communcation.AddUserToStoreRq;
import hu.elte.bsc.thesis.communcation.RegistrationCodeRq;
import hu.elte.bsc.thesis.model.*;
import hu.elte.bsc.thesis.model.User.Role;
import hu.elte.bsc.thesis.repository.RegistrationCodeRepository;
import hu.elte.bsc.thesis.repository.UserRepository;

import java.util.NoSuchElementException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserService {

    @Setter
    private ErrorResponse error;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationCodeRepository registrationCodeRepository;

    @Autowired
    private CompanyService companyService;

    public User register(@NonNull User user) {
        Role role = null;
        RegistrationCode regCode = null;
        try {
            regCode = registrationCodeRepository.findByCode(user.getRegCode()).get();
            if (!regCode.isUsed()) {
                role = registrationCodeRepository.findByCode(user.getRegCode()).get().getCreatedAccountType();
                user.setRole(role);
            } else {
                error = ErrorResponse.REGISTRATION_CODE_HAS_ALREADY_USED;
                return null;
            }
        } catch (NoSuchElementException nsee) {
            role = null;
            error = ErrorResponse.REGISTRATION_CODE_NOT_VALID;
        }
        if (role != null) {
            if (isRegistrationDataValid(user)) {
                user.setId(userRepository.findMaxId().orElse(0L) + 1);
                regCode.setUserId(user.getId());
                regCode.setUsed(true);
                registrationCodeRepository.save(regCode);
                userRepository.save(user);
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private boolean isRegistrationDataValid(User user) {
        if (!checkEmail(user.getEmail())) {
            return false;
        }
        if (isUsernameExist(user.getUsername())) {
            return false;
        }
        return true;
    }

    private boolean isUsernameExist(String username) { //for registration
        boolean exist = userRepository.findByUsername(username).isPresent();
        if (exist) {
            error = ErrorResponse.NAME_TAKEN;
        }
        return exist;
    }

    private boolean checkEmail(String email) { //for registration
        boolean valid = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ae) {
            valid = false;
            error = ErrorResponse.EMAIL_NOT_VALID;
        }
        if (userRepository.findByEmail(email).isPresent()) {
            valid = false;
            error = ErrorResponse.EMAIL_TAKEN;
        }
        return valid;
    }

    public User login(User user) {
        if (isLoginDataValid(user)) {
            return userRepository.findByUsername(user.getUsername()).get();
        } else {
            return null;
        }
    }

    private boolean isLoginDataValid(User user) { //for login
        boolean valid = userRepository.findByUsername(user.getUsername()).isPresent();
        if(!valid){
            this.error = ErrorResponse.USER_NOT_EXISTS;
            return false;
        }
        valid = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()).isPresent();
        if (!valid) {
            this.error = ErrorResponse.WRONG_PASSWORD;
        }
        return valid;
    }

    public User getLoggedInUser(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        if (user!=null) {
            return user;
        } else {
            error = ErrorResponse.UNAUTHORIZED;
            return null;
        }
    }

    public ErrorResponse getError() {
        return this.error;
    }

    public User logout(Authentication authentication) {
        authentication.setAuthenticated(false);
        return null;
    }

    public String changePassword(User user, Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication);
        if (loggedInUser.getUsername().equals(user.getUsername())) {
            User u = this.userRepository.findByUsername(user.getUsername()).get();
            u.setPassword(user.getPassword());
            this.userRepository.save(u);
            return "SUCCESS";
        } else {
            this.error = ErrorResponse.CHANGE_PASSWORD_ERROR;
            return "FAIL"; // It will be unused.
        }
    }

    public RegistrationCode createRegCode(Authentication authentication, RegistrationCodeRq registrationCodeRq) {
        boolean alreadyInDatabase = true;
        String generatedString = null;
        while (alreadyInDatabase) {
            int length = (int) Math.floor(Math.random() * 10 + 8);
            generatedString = RandomStringUtils.random(length, true, true);
            alreadyInDatabase = registrationCodeRepository.findByCode(generatedString).isPresent();
        }
        RegistrationCode regCode = new RegistrationCode();
        regCode.setUsed(false);
        regCode.setCode(generatedString);
        User user =getLoggedInUser(authentication);
        if (!user.getRole().equals(Role.ADMIN)) {
            if (companyService.getLoggedInUserCompany(authentication).getId() != registrationCodeRq.getCompanyId()) {
                error = ErrorResponse.PERMISSION_DENIED;
            }
        }
        regCode.setCompanyId(registrationCodeRq.getCompanyId());
        regCode.setCreatedAccountType(registrationCodeRq.getRole());
        registrationCodeRepository.save(regCode);
        user.getGeneratedCodes().add(regCode);
        userRepository.save(user);
        return regCode;
    }
    public User addUserToStore(Authentication authentication, AddUserToStoreRq addUserToStoreRq){
        User loggedInUser = getLoggedInUser(authentication);
        if(loggedInUser==null){
            return null;
        }
        if(loggedInUser.getRole()!=Role.COMPANY_ADMIN){
            error=ErrorResponse.PERMISSION_DENIED;
            return null;
        }
        for (Store store : companyService.getLoggedInUserCompany(authentication).getStores()) {
            if(store.getId()==addUserToStoreRq.getStoreId()){
                for (User user : companyService.getLoggedInUserCompany(authentication).getUsers()) {
                    if(user.getId()==addUserToStoreRq.getUserId()){
                        user.setStore(store);
                        userRepository.save(user);
                        return user;
                    }
                }
            }
        }
        error=ErrorResponse.NO_ENTITY_WITH_THE_GIVEN_ID;
        return null;
    }
}