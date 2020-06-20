package hu.elte.bsc.thesis.controller;

import hu.elte.bsc.thesis.communcation.AddSaleRq;
import hu.elte.bsc.thesis.communcation.GetSalesByStoreRq;
import hu.elte.bsc.thesis.communcation.GetSalesRq;
import hu.elte.bsc.thesis.model.ErrorResponse;
import hu.elte.bsc.thesis.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/sale")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @PostMapping("getsales")
    public ResponseEntity<?>getSales(@RequestBody GetSalesRq getSalesRq, Authentication authentication){
        Object response = saleService.getSalesBetween(getSalesRq, authentication);
        if (saleService.getError() == null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("getsalesbystore")
    public ResponseEntity<?>getSalesByStore(@RequestBody GetSalesByStoreRq getSalesByStoreRq, Authentication authentication){
        Object response = saleService.getSalesByStoreBetween(getSalesByStoreRq, authentication);
        if (saleService.getError() == null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("addsale")
    public ResponseEntity<?> addSale(@RequestBody AddSaleRq addSaleRq, Authentication authentication){
        Object response = saleService.addSale(addSaleRq, authentication);
        if (saleService.getError() == null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("removesale")
    public ResponseEntity<?> removeSale(@RequestBody Long saleId, Authentication authentication){
        Object response = saleService.removeSale(saleId, authentication);
        if (saleService.getError() == null) {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return errorHandling(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> errorHandling(HttpStatus httpStatus) {
        ErrorResponse error = saleService.getError();
        saleService.setError(null);
        return new ResponseEntity<>(error, httpStatus);
    }
}
