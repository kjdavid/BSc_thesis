import { Component, OnInit } from '@angular/core';
import { CompanyService } from 'src/app/services/company.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-new',
  templateUrl: './new.component.html',
  styleUrls: ['./new.component.css']
})
export class NewComponent implements OnInit {
  model
  success=false
  constructor(private companyService:CompanyService,private userService:UserService) {
    this.model={
      address:'',
      companyId:userService.company.id,
      storeName:''
    };
  }

  ngOnInit() {
  }
  save(){
    this.companyService.addStoreToCompany(this.model).subscribe(value=>{
      this.userService.company.stores.push(value);
      this.success=true;
    },
    err=> {
      this.success=true
      if(err.error!=undefined){
        alert(err.error);
      }else{
        alert("Unexpected error occured!");
      }
    });
  }
}
