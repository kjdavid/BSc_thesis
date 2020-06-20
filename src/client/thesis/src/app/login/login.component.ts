import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model: User;
  validationMessage: string;

  @ViewChild('form', {static: false}) form;

  constructor(
    private userService: UserService,
    private router: Router,
  ) {
    this.model = {
      "username": '',
      "password": '',
    };
  }

  ngOnInit() {
  }

  onSubmit() {
    if (this.form.valid) {
      this.userService.login(this.model).subscribe(
        user => {
          console.log(user);
          if(user!==undefined){
            this.userService.user=user;
            sessionStorage.setItem('Authorization','Basic ' + btoa(this.model.username + ':' + this.model.password));
            if(user.role!=="ADMIN"){
              this.userService.getCompany();
            }else{
              this.userService.getCompanies();
            }
            this.router.navigate(["index"])
          }
        },
        err=> {
          if(err.error!=undefined){
            alert(err.error)
          }else{
            alert("Unexpected error occured!")
          }
        })
    }
  }

}