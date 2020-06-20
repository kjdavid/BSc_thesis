import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { CompanyItem } from 'src/app/models/companyItem';
import { DomSanitizer } from '@angular/platform-browser';
import { ProductService } from 'src/app/services/product.service';
import { Router } from '@angular/router';
declare global {
  interface Window {
    IMAGE_RESULT?: string;
  }
}
@Component({
  selector: 'app-list',
  templateUrl: './company-product-list.component.html',
  styleUrls: ['./company-product-list.component.css']
})
export class CompanyProductListComponent implements OnInit {
  filter=''
  editables = [];
  companyItem:CompanyItem[];
  preview="";
  p=1;
  itemsPerPage=3;
  availableStores;
  selectedStores=new Map();
  constructor(private userService:UserService, private sanitizer:DomSanitizer, private productService: ProductService, private router:Router) {
    this.companyItem = userService.company.companyItem;
    this.resetAvailableStores();
  }
  model:{
    descCB:boolean,
    purchasePriceCB:boolean,
    barcodeCB:boolean,
    imgCB:boolean,
  }={
    descCB:false,
    purchasePriceCB:false,
    barcodeCB:false,
    imgCB:false
  }
  ngOnInit() {
  }
  getSafeHTML(html){
    if(html=='data:image/png;base64,'){
      return '';
    }
    return this.sanitizer.bypassSecurityTrustResourceUrl(html)
  }
  edit(id){
    this.model.barcodeCB=true;
    this.model.descCB=true;
    this.model.purchasePriceCB=true;
    this.model.imgCB=true;
    this.editables.push(id);
  }
  isEditable(id){
    return this.editables.includes(id);
  }
  save(id){
    let index;
    for(let i = 0; i < this.companyItem.length; i++){
      if(this.companyItem[i].id==id){
        index=i;
      }
    }
    let editableItem = {
      id:this.companyItem[index].id,
      itemName:(document.getElementById(id+"_name") as any).value,
      description: (document.getElementById(id+"_desc") as any).value,
      purchasePrice: (document.getElementById(id+"_purchasePrice") as any).value,
      base64str:this.preview!=""?this.preview.substring("data:image/png;base64,".length):this.companyItem[index].base64str
    };
    this.productService.editCompanyItem(editableItem).subscribe((value)=>{
      if(value.id==editableItem.id){
        if(value.base64str.length!=0){
          value.base64str=value.base64str
        }
        this.companyItem[index]=value;
        this.cancel(id)
        console.log(this.companyItem);
      }
    },err=>{
      console.log(err);
        if(err.error!=undefined){
          alert(err.error);
        }else{
          alert("Unexpected error occured!");
        }
    });
  }
  cancel(id){
    for( let i = 0; i < this.editables.length; i++){ 
      if ( this.editables[i] === id) {
        this.editables.splice(i, 1); 
      }
   }
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
  resetAvailableStores(){
    if(this.userService.isCompanyAdmin()){
      this.availableStores=new Map();
      for(let i = 0; i < this.companyItem.length;i++){
        this.availableStores.set(this.companyItem[i].id,this.getAvailableStoresForItem(this.companyItem[i]));
      }
    }
  }
  getAvailableStoresForItem(item){
    let stores=[];
    for(let i = 0; i < this.userService.company.stores.length; i++){
      let addable=true;
      for(let j = 0; j < this.userService.company.stores[i].storeItem.length && addable; j++){
        if(this.userService.company.stores[i].storeItem[j].companyItem.id==item.id){
          addable=false;
        }
      }
      if(addable){
        stores.push({id: this.userService.company.stores[i].id, storeName: this.userService.company.stores[i].storeName})
      }
    }
    return stores;
  }
  setSelectedStore(id, storeId){
    let avStores= this.availableStores.get(id)
    for(let i =0; i < avStores.length; i++){
      if(avStores[i].id==storeId){
        this.selectedStores.set(id,avStores[i]);
        console.log(this.selectedStores);
        return;
      }
    }
    this.selectedStores.set(id,undefined);
  }
  resetSelectedStores(){
    this.selectedStores= new Map();
    for(let i = 0; i < this.companyItem.length;i++){
      this.selectedStores.set(this.companyItem[i].id,-1)
    }
  }
  myStoreAvailable(cid){
    if(this.userService.user.store!=null){
      for(let j = 0; j < this.userService.company.stores.length; j++){
        if(this.userService.company.stores[j].id==this.userService.user.store.id){
          for(let i = 0;i<this.userService.company.stores[j].storeItem.length;i++){
            if(this.userService.company.stores[j].storeItem[i].companyItem.id==cid){
              return false;
            }
          }
          return true;
        }
      }
    }
    return false;
  }
  addToStore(item,store){
    console.log(item);
    console.log(store);
    if(store!=undefined){
      this.productService.companyItem=item;
      this.productService.store=store;
      this.router.navigate(['product/add-store-item']);
    }
  }
  checkChar(e){
    const pattern = /[0-9]/;
    const inputChar = String.fromCharCode(e.charCode);
    if (!pattern.test(inputChar)) {    
      // invalid character, prevent input
      e.preventDefault();
    }
  }
}
