/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.controller;

import hu.elte.bsc.thesis.communcation.AddStoreRq;
import hu.elte.bsc.thesis.communcation.EditCompanyRq;
import hu.elte.bsc.thesis.communcation.EditStoreRq;
import hu.elte.bsc.thesis.model.*;
import hu.elte.bsc.thesis.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



/**
 * @author kjdavid <kjdavid96 at gmail.com>
 */

//TODO *addCompany checked* *addShop checked* *editCompany checked* *editShop checked* //ItemControllerBasics
// NICE_TO_HAVE: addUserToShop
@CrossOrigin(origins="*", maxAge=3600)
@RequestMapping("/api/company")
@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("")
    public ResponseEntity<?> getCompanies() {
        return new ResponseEntity<>(companyService.getCompanies(), HttpStatus.OK);
    }

    //TODO rewrite to PostMapping after testing
    @GetMapping("mycompany")
    public ResponseEntity<?> getLoggedInUserCompany(Authentication authentication) {
       return new ResponseEntity<>(companyService.getLoggedInUserCompany(authentication), HttpStatus.OK);
    }

    //TODO rewrite to PostMapping after testing
    @GetMapping("users")//unused
    public ResponseEntity<?> getUsersToCompany(@RequestBody int cid, Authentication authentication) {
        return new ResponseEntity<>(companyService.getUsersToCompany(authentication,cid), HttpStatus.OK);
    }

    @PostMapping("addcompany")//TODO Question: maybe only the admin should create it?
    public ResponseEntity<?> addCompany(@RequestBody String companyName) {
        Object response = companyService.addCompany(companyName);
        if (companyService.getError() == null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.CONFLICT);
    }

    @PostMapping("editcompany")//TODO Question: maybe only the admin should create it?
    public ResponseEntity<?> editCompany(@RequestBody EditCompanyRq editCompanyRq) {
        Object response = companyService.editCompany(editCompanyRq);
        if (companyService.getError() == null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("addstore")
    public ResponseEntity<?> addShop(@RequestBody AddStoreRq addStoreRq){
        Object response = companyService.addStore(addStoreRq);
        if (companyService.getError() == null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("editstore")
    public ResponseEntity<?> editStore(@RequestBody EditStoreRq editStoreRq){
        Object response = companyService.editStore(editStoreRq);
        if (companyService.getError() == null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> errorHandling(HttpStatus httpStatus) {
        ErrorResponse error = companyService.getError();
        companyService.setError(null);
        return new ResponseEntity<>(error, httpStatus);
    }
}
