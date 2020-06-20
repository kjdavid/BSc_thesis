import { Component, OnInit } from '@angular/core';
import { CompanyService } from 'src/app/services/company.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {
  model
  success=false;
  constructor(private companyService:CompanyService,private userService:UserService) {
    this.model={
      address:'',
      storeId:-1,
      storeName:''
    };
  }

  ngOnInit() {
  }
  setValue(val){
    this.model.storeId=val;
    if(val==-1){
      this.model.address='';
      this.model.storeName='';
    }else{
      for(let i = 0; i < this.userService.company.stores.length; i++){
        if(this.userService.company.stores[i].id==val){
          this.model.address=this.userService.company.stores[i].address;
          this.model.storeName=this.userService.company.stores[i].storeName;
          return;
        }
      }
    }
  }
  save(){
    if(this.model.storeId!=-1){
      this.companyService.editStore(this.model).subscribe(value=>{
        for(let i = 0; i < this.userService.company.stores.length; i++){
          if(this.userService.company.stores[i].id==this.model.storeId){
            this.userService.company.stores[i]=value;
            this.success=true;
          }
        }
      }),err=>{
        this.success=false;
        if(err.error!=undefined){
          alert(err.error);
        }else{
          alert("Unexpected error occured");
        }
      }
    }
  }
}
