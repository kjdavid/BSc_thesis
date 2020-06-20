import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {NgxPaginationModule} from 'ngx-pagination';

import { IndexComponent } from './index/index.component';
import { HeaderComponent } from './header/header.component';
import { LoginComponent } from './login/login.component';
import { RegCodeGenComponent } from './reg-code-gen/reg-code-gen.component';
import { CompanyProductListComponent} from './product/company-product-list/company-product-list.component';
import { AddCompanyItemComponent } from './product/add-company-item/add-company-item.component';
import { AddStoreItemComponent } from './product/add-store-item/add-store-item.component';
import { StoreProductListComponent } from './product/store-product-list/store-product-list.component';
import { ListComponent as UserListComponent } from './user/list/list.component';
import { NewComponent as NewStoreComponent } from './store/new/new.component';
import { NewComponent as NewCompanyComponent } from './company/new/new.component';
import { EditComponent as EditStoreComponent } from './store/edit/edit.component';
import { EditComponent as EditCompanyComponent } from './company/edit/edit.component';
import { CompanyAdminGuard } from './services/company-admin-guard.service';
import { SellerGuard } from './services/seller-guard.service';
import { AdminGuard } from './services/admin-guard.service';
import { ListComponent as SalesListComponent } from './sales/list/list.component';
import { StringfilterPipe } from './pipes/stringfilter.pipe';
import { RegCodeGuard } from './services/reg-code-guard.service';
import { RegistrationComponent } from './registration/registration.component';



const routes: Routes = [
  {path: 'index', component: IndexComponent},
  {path: 'login', component: LoginComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'product/company-product-list', component: CompanyProductListComponent, canActivate:[SellerGuard]},
  {path: 'product/new-company-item', component: AddCompanyItemComponent, canActivate:[SellerGuard]},
  {path: 'product/add-store-item', component: AddStoreItemComponent, canActivate:[SellerGuard]},
  {path: 'product/store-product-list', component: StoreProductListComponent},
  {path: 'user/list', component: UserListComponent, canActivate:[CompanyAdminGuard]},
  {path: 'company/new', component: NewCompanyComponent, canActivate:[AdminGuard]},
  {path: 'company/edit', component: EditCompanyComponent, canActivate:[AdminGuard]},
  {path: 'store/new', component: NewStoreComponent, canActivate:[CompanyAdminGuard]},
  {path: 'store/edit', component: EditStoreComponent, canActivate:[CompanyAdminGuard]},
  {path: 'sales', component:SalesListComponent, canActivate:[CompanyAdminGuard]},
  {path: 'user/regcodegen', component: RegCodeGenComponent, canActivate:[RegCodeGuard]},//regisztracio az authentikacio miatt nem lehetseges
  {path: '**', redirectTo: 'index'}
];

@NgModule({
  declarations:[
    IndexComponent,
    HeaderComponent,
    LoginComponent,
    RegCodeGenComponent,
    CompanyProductListComponent,
    AddCompanyItemComponent,
    AddStoreItemComponent,
    StoreProductListComponent,
    UserListComponent,
    NewStoreComponent,
    EditStoreComponent,
    UserListComponent,
    NewCompanyComponent,
    EditCompanyComponent,
    SalesListComponent,
    StringfilterPipe,
    RegistrationComponent,

  ],
  imports: [
    CommonModule,
    FormsModule,
    NgxPaginationModule,
    RouterModule.forRoot(routes, {enableTracing: true})
  ],
  exports: [RouterModule, CommonModule, FormsModule]
})
export class AppRoutingModule { }
