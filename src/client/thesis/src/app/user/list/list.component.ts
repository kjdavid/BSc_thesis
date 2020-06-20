import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user';
import { Store } from 'src/app/models/store';
import { TouchSequence } from 'selenium-webdriver';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {

  constructor(private userService : UserService) {
  }
  users:User[]
  stores:Store[]
  ngOnInit() {
    this.users = [];
    this.userService.company.users.map(val => this.users.push(Object.assign({},val)));
    this.stores = [];
    this.userService.company.stores.map(val => this.stores.push(Object.assign({},val)));
    for( let i = 0; i < this.stores.length; i++){
      this.stores[i].storeItem=undefined;
    }
  }
  save(){
    for(let i = 0; i < this.users.length; i++){
      if(this.users[i].store!=this.userService.company.users[i].store && this.users[i].store!=null){
        this.userService.setUserToStore(this.users[i]);
      }
    }
  }
  reset(){
    this.users = [];
    this.userService.company.users.map(val => this.users.push(Object.assign({},val)));
  }
  setValue(userId,value){
    for(let i = 0; i < this.users.length; i++){
      if(userId === this.users[i].id){
        for(let j = 0; j < this.stores.length; j++){
          if(this.stores[j].id == value){
            this.users[i].store=this.stores[j];
            return;
          }
        }
      }
    }
  }
}
