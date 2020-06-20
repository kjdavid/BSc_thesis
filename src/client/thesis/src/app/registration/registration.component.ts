import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import CryptoJS from 'crypto-js';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  model: User;
  validationMessage: string;
  pwConfirm=''
  @ViewChild('form', {static: false}) form;

  constructor(
    private userService: UserService,
    private router: Router,
  ) {
    this.model = {
      "username": '',
      "password": '',
      "email":'',
      "regCode":''
    };
  }

  ngOnInit() {
  }

  onSubmit() {
    if (this.form.valid) {
      this.model.password=CryptoJS.SHA256(this.model.password).toString(CryptoJS.enc.Hex);
      this.userService.registration(this.model).subscribe(
        response => {
          this.router.navigate(['index']);
        },
        err=> {
          if(err.error!=undefined){
            alert(err.error);
          }else{
            alert("Unexpected error occured!");
          }
        })
    }
  }

}