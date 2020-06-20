import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { SaleService } from 'src/app/services/sale.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {
  sales=[]
  sumProfit=0;
  model={from:'',to:''}
  selectValue=-1;
  constructor(private userService:UserService, private saleService: SaleService) { }

  ngOnInit() {
  }
  setValue(val){
    this.selectValue=val;
  }
  send(){
    if(this.selectValue==-1){
      this.saleService.getSales(this.model).subscribe(value=>{
        this.sales=value;
        this.calculateSumProfit();
      },
      err=> {
        if(err.error!=undefined){
          alert(err.error);
        }else{
          alert("Unexpected error occured!");
        }
      })
    }else{
      this.saleService.getSalesByShop({...this.model, storeId: this.selectValue}).subscribe(value=>{
        this.sales=value;
        this.calculateSumProfit();
      },
      err=> {
        if(err.error!=undefined){
          alert(err.error);
        }else{
          alert("Unexpected error occured!");
        }
      })
    }
  }
  calculateSumProfit(){
    for(let i = 0; i < this.sales.length; i++){
      this.sumProfit+=(this.sales[i].sellingPrice - this.sales[i].storeItem.companyItem.purchasePrice) * this.sales[i].count
    }
  }
}
