/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.controller;

/**
 * @author kjdavid <kjdavid96 at gmail.com>
 */

import hu.elte.bsc.thesis.communcation.AddUserToStoreRq;
import hu.elte.bsc.thesis.model.ErrorResponse;
import hu.elte.bsc.thesis.communcation.RegistrationCodeRq;
import hu.elte.bsc.thesis.model.User;
import hu.elte.bsc.thesis.service.UserService;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins="*", maxAge=3600)
@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Object result = userService.register(user);
        if (userService.getError() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return errorHandling(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login") //bekötve
    public ResponseEntity<?> login(@RequestBody User user) {
        User result = userService.login(user);
        if (userService.getError() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return errorHandling(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This method is for avoiding code duplication.
     * Checks if the error was updated and if it was, then call the errorHandling method.
     *
     * Example of calling this method: return beautyful("changePassword", new Class[]{User.class}, new Object[] {user}, HttpStatus.BAD_REQUEST);
     *
     * @param fnName is the callable function name
     * @param typeArr is the function params type in order
     * @param paramArr is the function params in order
     * @param httpStatus is the error case HttpStatus.
     * @return with ResponseEntity which contains the result on success or the errorResponse on failure.
     */
    private ResponseEntity<?> beautyful(String fnName, Class[] typeArr, Object[] paramArr, HttpStatus httpStatus) {
        try {
            Object result = userService.getClass().getDeclaredMethod(fnName, typeArr).invoke(userService, paramArr);
            if (userService.getError() == null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return errorHandling(httpStatus);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return new ResponseEntity<String>("The developer did something wrong please make a contact with him through this e-mail address: kjdavid96@gmail.com", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method is for avoiding code duplication.
     * It resets the error and creating a new ResponseEntity for the client to inform what went wrong.
     *
     * @param httpStatus is the HttpStatus the server is sending to the client with the current error.
     * @return ResponseEntity<ErrorResponse> which will be processed by client side
     */
    private ResponseEntity<?> errorHandling(HttpStatus httpStatus) {
        ErrorResponse error = userService.getError();
        userService.setError(null);
        return new ResponseEntity<ErrorResponse>(error, httpStatus);
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody User user, Authentication authentication) {
        Object result = userService.changePassword(user,authentication);
        if (userService.getError() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return errorHandling(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")//FIXME: maybe we should check if we are attempting to logout that user which is logged in.
    public ResponseEntity<User> logout(Authentication authentication) {
        return ResponseEntity.ok(userService.logout(authentication));
    }

    @PostMapping("/getregistrationcode")
    public ResponseEntity<?> getRegistrationCode(@RequestBody RegistrationCodeRq registrationCodeRq, Authentication authentication) {
        Object result = userService.createRegCode(authentication,registrationCodeRq);
        if (userService.getError() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return errorHandling(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/addusertostore")
    public ResponseEntity<?> addUserToStore(@RequestBody AddUserToStoreRq addUserToStoreRq, Authentication authentication){
        Object result = userService.addUserToStore(authentication,addUserToStoreRq);
        if (userService.getError() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return errorHandling(HttpStatus.FORBIDDEN);
        }
    }
}
