import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { UserService } from './user.service';
@Injectable()
export class AdminGuard implements CanActivate {
  constructor(public auth: UserService, public router: Router) {}
  canActivate(): boolean {
    if (this.auth.isLoggedIn() && this.auth.user.role == 'ADMIN') {
      return true;
    }
    this.router.navigate(['index']);
    return false;
  }
}