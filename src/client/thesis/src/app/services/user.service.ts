import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { User } from '../models/user';
import { Router } from '@angular/router';
import { Company } from '../models/company';
import { CompanyItem } from '../models/companyItem';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  user: User;
  serverTime: Date;
  company:Company;
  companies;
  constructor(
    private http: HttpClient,
    private router : Router
  ) {}
  public getLoggedInUsername(): String{
    if(this.isLoggedIn()){
      return this.user.username;
    }else{
      return "";
    }
  }
  public isLoggedIn(): boolean{
    return (this.user!==undefined);
  }

  public login(user: User) {
    return this.http.post<User>(environment.serverUrl+':'+environment.serverPort+'/api/user/login',user,{observe: 'response'});
  }
  isUserLoggedIn() {
    let user = sessionStorage.getItem('Authorization')
    return !(user === null)
  }
  logOut() {
    this.user = undefined;
    sessionStorage.removeItem('Authorization');
    this.company=undefined;
    this.companies=undefined;
    this.router.navigate(['index']);
  }
  generateCode(regCodeRq){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/user/getregistrationcode',regCodeRq,{"headers":headers});
    return response$;
  }
  registration(user){
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/user/register',user);
    return response$;
  }
  
  public getCompany(){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.get(environment.serverUrl+':'+environment.serverPort+'/api/company/mycompany',{"headers":headers});
    response$.subscribe((value)=>{
      this.company=value;
      sessionStorage.setItem("companyId",value.id)
    })
  }
  public setUserToStore(user: User){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.post(environment.serverUrl+':'+environment.serverPort+'/api/user/addusertostore',{"userId" :user.id, "storeId":user.store.id },{"headers":headers});
    response$.subscribe((value)=>{
      this.company.users[this.company.users.findIndex(u=>user.id==u.id)]=value;
    },err=> {
      if(err.error!=undefined){
        alert(err.error+" with "+ user.username);
      }else{
        alert("Unexpected error occured with "+ user.username);
      }
    })
  }
  public getCompanies(){
    const headers = new HttpHeaders({ Authorization: sessionStorage.getItem("Authorization")});
    const response$: Observable<any> = this.http.get(environment.serverUrl+':'+environment.serverPort+'/api/company',{"headers":headers});
    response$.subscribe(value=>{
      this.companies=value;
    })
  }
  isCompanyAdmin(){
    return this.user.role=="COMPANY_ADMIN";
  }
  isSeller(){
    return this.user.role=="SELLER";
  }
  isAdmin(){
    return this.user.role=="ADMIN";
  }
}
