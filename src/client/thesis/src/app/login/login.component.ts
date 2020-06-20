import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import CryptoJS from 'crypto-js';

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
      let loginData = {"username":this.model.username,"password": CryptoJS.SHA256(this.model.password).toString(CryptoJS.enc.Hex)}
      this.userService.login(loginData).subscribe(
        response => {
          if(response!==undefined){
            if(response.body.username)
            this.userService.user=response.body;
            sessionStorage.setItem('Authorization','Basic ' + btoa(loginData.username + ':' + loginData.password));
            if(this.userService.user.role!=="ADMIN"){
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