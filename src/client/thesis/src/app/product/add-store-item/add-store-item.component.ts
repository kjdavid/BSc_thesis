import { Component, OnInit } from '@angular/core';
import { ProductService } from 'src/app/services/product.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-add-store-item',
  templateUrl: './add-store-item.component.html',
  styleUrls: ['./add-store-item.component.css']
})
export class AddStoreItemComponent implements OnInit {

  model={
    sellingPrice:0,
    stock:0,
    discount:0
  }
  success=false;
  constructor(private productService: ProductService, private userService: UserService) { }

  ngOnInit() {
  }
  save(){
    if(this.model.discount.toString().split(".").length>1){
      alert("Wrong format of discount")!
      return;
    }
    if(!isNaN(this.model.sellingPrice) || !isNaN(this.model.discount) || !isNaN(this.model.stock)){
      this.productService.addStoreItem(this.model).subscribe(value=>{
        for(let i = 0; i < this.userService.company.stores.length; i++){
          if(this.userService.company.stores[i].id==this.productService.store.id){
            this.userService.company.stores[i].storeItem.push(value);
            this.success=true
          };
        }
      },err=>{
        this.success=false;
        if(err.error!=undefined){
          alert(err.error);
        }else{
          alert("Unexpected error occured!");
        }
      });
    }else{
      alert("Az inputmező(k)ben lévő adat(ok) nem szám(ok)!");
    }
  }
  checkChar(e){
    const pattern = /[0-9]/;
    const inputChar = String.fromCharCode(e.charCode);
    if (!pattern.test(inputChar)) {
      e.preventDefault();
    }
  }
  checkDouble(e){
    const pattern = /[0-9.]/;
    const inputChar = String.fromCharCode(e.charCode);
    if (!pattern.test(inputChar)) {
      e.preventDefault();
    }
  }
}
