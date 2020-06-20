import { Injectable } from '@angular/core';
import { UserService } from './user.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class RegCodeGuard {
  constructor(public auth: UserService, public router: Router) {}
  canActivate(): boolean {
    if (this.auth.isLoggedIn() && this.auth.user.role == 'COMPANY_ADMIN' || this.auth.user.role == 'ADMIN') {
      return true;
    }
    this.router.navigate(['index']);
    return false;
  }
}