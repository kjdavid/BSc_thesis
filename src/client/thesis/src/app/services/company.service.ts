import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  constructor(private http:HttpClient) { }
  addCompany(companyName:String){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/company/addcompany',companyName,{"headers":headers});
    return response$;
  }
  editCompany(editCompany){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/company/editcompany',editCompany,{"headers":headers});
    return response$;
  }
  addStoreToCompany(addStore){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/company/addstore',addStore,{"headers":headers});
    return response$;
  }
  editStore(editStore){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/company/editstore',editStore,{"headers":headers});
    return response$;
  }
}
