import { Component, OnInit } from '@angular/core';
import { CompanyService } from 'src/app/services/company.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-new',
  templateUrl: './new.component.html',
  styleUrls: ['./new.component.css']
})
export class NewComponent implements OnInit {
  companyName:String;
  success=false;
  constructor(private userService:UserService,private companyService:CompanyService) { this.companyName=""}

  ngOnInit() {
  }
  save(){
    this.companyService.addCompany(this.companyName).subscribe(value=>{
      this.success=true;
      this.userService.companies.push(value);
    },err=>{
      this.success=false;
      if(err.error!=undefined){
        alert(err.error);
      }else{
        alert("Unexpected error occured");
      }
    });
  }
}
