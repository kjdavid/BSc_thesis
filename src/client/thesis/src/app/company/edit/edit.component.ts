import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { CompanyService } from 'src/app/services/company.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {

  constructor(private userService:UserService, private companyService:CompanyService) { }
  companyName:String
  success=false;
  ngOnInit() {
  }
  setValue(id){
    if(id==-1){
      this.companyName=''
      return;
    }
    for(let i = 0; i < this.userService.companies.length; i++){
      if(this.userService.companies[i].id==id){
        this.companyName=this.userService.companies[i].companyName;
        return;
      }
    }
  }
  save(companyId){
    this.companyService.editCompany({companyName:this.companyName, companyId}).subscribe(value=>{
      for(let i = 0; i < this.userService.companies.length; i++){
        if(this.userService.companies[i].id==companyId){
          this.success=true
          this.userService.companies[i]=value;
          return;
        }
      }
    },err=>{
      this.success=false;
        if(err.error!=undefined){
          alert(err.error);
        }else{
          alert("Unexpected error occured!");
        }
    });
  }
}
