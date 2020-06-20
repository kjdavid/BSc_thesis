import { Component, OnInit,ViewChild } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { ProductService } from 'src/app/services/product.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-add-company-item',
  templateUrl: './add-company-item.component.html',
  styleUrls: ['./add-company-item.component.css']
})
export class AddCompanyItemComponent implements OnInit {
  @ViewChild('form', {static: false}) form;
  model;
  preview;
  success = false;
  constructor(private userService:UserService, private productService: ProductService, private sanitizer:DomSanitizer) {
    this.model={
      itemName:'',
      description:'',
      purchasePrice:'',
      barcode:''
    }
    this.preview='';
   }

  ngOnInit() {
  }

  readUrl(event:any) {
    if (event.target.files && event.target.files[0]) {
      var reader = new FileReader();
  
      reader.onload = (event:any) => {
        let path = event.target.result;
      }
      let base;
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = e => {
        base=reader.result;
        window.IMAGE_RESULT = base;
        this.preview=base;
       };
    }
  }

  save(){
    if(!isNaN(this.model.purchasePrice)){
      this.productService.addCompanyItem({...this.model, base64str:this.preview.substring('data:image/png;base64,'.length)}).subscribe(value=>{
        this.userService.company.companyItem.push(value);
        this.success=true;
      },err=>{
        this.success=false;
        if(err.error!=undefined){
          alert(err.error);
        }else{
          alert("Unexpected error occured!");
        }
      });
    }else{
      alert("Beszerzési ár nem szám!");
    }
  }
  checkChar(e){
    const pattern = /[0-9]/;
    const inputChar = String.fromCharCode(e.charCode);
    if (!pattern.test(inputChar)) {
      e.preventDefault();
    }
  }
  getSafeHTML(html){
    if(html=='data:image/png;base64,'){
      return '';
    }
    return this.sanitizer.bypassSecurityTrustResourceUrl(html)
  }
}
