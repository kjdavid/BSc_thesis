import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SaleService {

  constructor(private http:HttpClient) { }
  getSales(getSales){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/sale/getsales',getSales,{"headers":headers});
    return response$;
  }
  getSalesByShop(getSalesByStore){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/sale/getsalesbystore',getSalesByStore,{"headers":headers});
    return response$;
  }
}
