import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-reg-code-gen',
  templateUrl: './reg-code-gen.component.html',
  styleUrls: ['./reg-code-gen.component.css']
})
export class RegCodeGenComponent implements OnInit {
  regCode='';
  model={role:'-1', companyId:-1}
  constructor(private userService: UserService) {
    if(userService.isCompanyAdmin()){
      this.model.companyId=userService.company.id;
    }
  }

  ngOnInit() {
  }
  setCompany(value){
    if(this.userService.isAdmin()){
      this.model.companyId=value;
    }
  }
  setRole(value){
    this.model.role=value;
  }
  copy(copyText){
    copyText.focus();
    copyText.setSelectionRange(0,20);
    document.execCommand("copy");
  }
  generate(){
    if(this.model.role!='-1'){
      if(this.model.role=='ADMIN'){
        this.model.companyId=-1;        
        this.userService.generateCode(this.model).subscribe(value=>{
          this.regCode=value.code;
        },err=>{
          if(err.error!=undefined){
            alert(err.error);
          }else{
            alert("Unexpected error occured!");
          }
        });
      }else if(this.model.companyId!=-1){
        this.userService.generateCode(this.model).subscribe(value=>{
          this.regCode=value.code;
        },err=>{
          if(err.error!=undefined){
            alert(err.error);
          }else{
            alert("Unexpected error occured!");
          }
        });
      }
    }
  }
}
