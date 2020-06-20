import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ProductService } from 'src/app/services/product.service';
import { Store } from 'src/app/models/store';

declare global {
  interface Window {
    IMAGE_RESULT?: string;
  }
}
@Component({
  selector: 'app-order',
  templateUrl: './store-product-list.component.html',
  styleUrls: ['./store-product-list.component.css']
})
export class StoreProductListComponent implements OnInit {
  filter=''
  editables = [];
  store:Store;
  emptyStore={id:-1,address:'',storeName:'',storeItem:[]}
  preview="";
  p=1;
  itemsPerPage=3;
  model:{
    descCB:boolean,
    purchasePriceCB:boolean,
    barcodeCB:boolean,
    imgCB:boolean,
    stockCB:boolean,
    sellingPriceCB:boolean,
    discountCB:boolean
  }={
    descCB:false,
    purchasePriceCB:false,
    barcodeCB:false,
    imgCB:false,
    stockCB:true,
    sellingPriceCB:true,
    discountCB:true
  }
  constructor(private userService:UserService, private sanitizer: DomSanitizer, private productService: ProductService) {
    if(userService.isSeller()){
      if(this.userService.user.store!=null){
        for(let j = 0; j < this.userService.company.stores.length; j++){
          if(this.userService.company.stores[j].id==this.userService.user.store.id){
            this.store=this.userService.company.stores[j];
          }
        }
      }else{
        this.store=this.emptyStore;
      }
    }else{
      this.store=this.emptyStore;
    }
  }

  ngOnInit() {
  }
  getSafeHTML(html){
    if(html=='data:image/png;base64,'){
      return '';
    }
    return this.sanitizer.bypassSecurityTrustResourceUrl(html)
  }
  setSelectedStore(storeId){
    for(let i = 0; i<this.userService.company.stores.length; i++){
      if(storeId==this.userService.company.stores[i].id){
        this.store = this.userService.company.stores[i]
      }
    }
  }
  edit(id){
    this.model.sellingPriceCB=true;
    this.model.stockCB=true;
    this.model.discountCB=true;
    this.editables.push(id);
  }
  isEditable(id){
    return this.editables.includes(id);
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
  resetEdit(){
    this.editables = [];
  }
  cancel(id){
    for( let i = 0; i < this.editables.length; i++){ 
      if ( this.editables[i] === id) {
        this.editables.splice(i, 1); 
      }
   }
  }
  save(id){
    let index;
    for(let i = 0; i < this.store.storeItem.length; i++){
      if(this.store.storeItem[i].id==id){
        index=i;
      }
    }
    let editableItem = {
      id:this.store.storeItem[index].id,
      stock:(document.getElementById(id+"_stock") as any).value,
      discount:(document.getElementById(id+"_discount") as any).value,
      sellingPrice:(document.getElementById(id+"_sellingPrice") as any).value
    }
    console.log(editableItem);
    if(editableItem.discount.toString()===""){
      alert("Wrong format of discount")!
      return;
    }
    this.productService.editStoreItem(editableItem).subscribe(value=>{
      console.log(value);
      this.store.storeItem[index]=value;  
      this.cancel(id);
    }, err=>{
        if(err.error!=undefined){
          alert(err.error);
        }else{
          alert("Unexpected error occured!");
        }
    });
  }
  order(id){
    let index;
    for(let i = 0; i < this.store.storeItem.length; i++){
      if(this.store.storeItem[i].id==id){
        index=i;
      }
    }
    this.productService.order({storeItemId:this.store.storeItem[index].id,amount:
      (document.getElementById(this.store.storeItem[index].id+'_order')as any).value}).subscribe(value=>{
      console.log(value);
      this.store.storeItem[index]=value;
    },
    err=>{
        if(err.error!=undefined){
          alert(err.error);
        }else{
          alert("Unexpected error occured!");
        }
    })
  }
  sell(id){
    let index;
    for(let i = 0; i < this.store.storeItem.length; i++){
      if(this.store.storeItem[i].id==id){
        index=i;
      }
    }
    console.log(index);
    this.productService.sell({storeItemId:this.store.storeItem[index].id,amount:
      (document.getElementById(this.store.storeItem[index].id+'_sell')as any).value}).subscribe(value=>{
      console.log(value);
      this.store.storeItem[index]=value;
    },
    err=>{
      if(err.error!=undefined){
        alert(err.error);
      }else{
        alert("Unexpected error occured!");
      }
    })
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
