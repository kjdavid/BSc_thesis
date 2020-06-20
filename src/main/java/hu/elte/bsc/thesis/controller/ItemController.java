package hu.elte.bsc.thesis.controller;

import hu.elte.bsc.thesis.communcation.*;
import hu.elte.bsc.thesis.model.ErrorResponse;
import hu.elte.bsc.thesis.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping("additemtocompany")
    public ResponseEntity<?> addItemToCompany(@RequestBody AddItemToCompanyRq addItemToCompanyRq, Authentication authentication) {
        Object response = itemService.addItemToCompany(addItemToCompanyRq, authentication);
        if (itemService.getError() == null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("additemtostore")
    public ResponseEntity<?> addItemToStore(@RequestBody AddItemToStoreRq addItemTostoreRq, Authentication authentication) {
        Object response = itemService.addItemToStore(addItemTostoreRq, authentication);
        if (itemService.getError() == null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("getitemstocompany")
    public ResponseEntity<?> getItemsToCompany(@RequestBody Long companyId, Authentication authentication) {
        Object response = itemService.getItemsToCompany(companyId, authentication);
        if (itemService.getError() == null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.FORBIDDEN);
    }

    @PostMapping("getitemstostore")
    public ResponseEntity<?> getItemsToStore(@RequestBody Long storeId, Authentication authentication){
        Object response = itemService.getItemsToStore(storeId, authentication);
        if(itemService.getError() == null){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.FORBIDDEN);
    }

    @PostMapping("editcompanyitem")
    public ResponseEntity<?> editCompanyItem(@RequestBody EditCompanyItemRq editCompanyItemRq, Authentication authentication){
        Object response = itemService.editCompanyItem(editCompanyItemRq, authentication);
        if(itemService.getError() == null){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.FORBIDDEN);
    }

    @PostMapping("editstoreitem")
    public ResponseEntity<?> editStoreItem(@RequestBody EditStoreItemRq editStoreItemRq, Authentication authentication){
        Object response = itemService.editStoreItem(editStoreItemRq, authentication);
        if(itemService.getError() == null){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.FORBIDDEN);
    }

    @PostMapping("orderitem")
    public ResponseEntity<?> orderItem(@RequestBody OrderItemRq orderItemRq, Authentication authentication){
        Object response = itemService.orderItem(orderItemRq, authentication);
        if(itemService.getError() == null){
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<?> errorHandling(HttpStatus httpStatus) {
        ErrorResponse error = itemService.getError();
        itemService.setError(null);
        return new ResponseEntity<>(error, httpStatus);
    }
}
