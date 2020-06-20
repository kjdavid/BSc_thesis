import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserService } from './services/user.service';
import { ProductService } from './services/product.service';
import { AdminGuard } from './services/admin-guard.service';
import { CompanyAdminGuard } from './services/company-admin-guard.service';
import { SellerGuard } from './services/seller-guard.service';
import { CompanyService } from './services/company.service';
import { SaleService } from './services/sale.service';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [UserService, ProductService, AdminGuard, CompanyAdminGuard, SellerGuard, CompanyService, SaleService],
  bootstrap: [AppComponent]
})
export class AppModule { }
