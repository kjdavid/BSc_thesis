import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Store } from '../models/store';
import { CompanyItem } from '../models/companyItem';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  companyItem:CompanyItem;
  store:Store;
  constructor(
    private http: HttpClient
    ) {
    }

  editCompanyItem(editCompanyItemRq){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/item/editcompanyitem',editCompanyItemRq,{"headers":headers});
    return response$;
  }
  addCompanyItem(addCompanyItemRq){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/item/additemtocompany',addCompanyItemRq,{"headers":headers});
    return response$;
  }
  editStoreItem(editStoreItem){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/item/editstoreitem',editStoreItem,{"headers":headers});
    return response$;
  }
  addStoreItem(addStoreItem){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/item/additemtostore',{...addStoreItem, storeId:this.store.id, companyItemId:this.companyItem.id},{"headers":headers});
    return response$;
  }
  order(orderItem){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/item/orderitem',orderItem,{"headers":headers});
    return response$;
  }
  sell(sellItem){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/sale/addsale',sellItem,{"headers":headers});
    return response$;
  }
  logOut(){
    this.companyItem=undefined;
    this.store=undefined;
  }
}
