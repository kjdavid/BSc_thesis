import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { UserService } from './user.service';
@Injectable()
export class SellerGuard implements CanActivate {
  constructor(public auth: UserService, public router: Router) {}
  canActivate(): boolean {
    if (this.auth.isLoggedIn() && (this.auth.user.role == 'SELLER' || this.auth.user.role == 'COMPANY_ADMIN') ) {
      return true;
    }
    this.router.navigate(['index']);
    return false;
  }
}