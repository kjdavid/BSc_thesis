import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private userService:UserService, private productService:ProductService) { }

  ngOnInit() {
  }

  isLoggedIn():Boolean{
    return this.userService.isLoggedIn();
  }
  logout(){
    this.productService.logOut();
    this.userService.logOut();
  }
  hasAdminRole(){
    return this.userService.user.role==="ADMIN";
  }
  hasCompanyAdminRole(){
    return this.userService.user.role==="COMPANY_ADMIN";
  }
  hasSellerRole(){
    return this.userService.user.role==="SELLER";
  }
}
