import { CompanyItem } from './companyItem';

export interface StoreItem {
    id:number;
    companyItem:CompanyItem;
    sellingPrice: number;
    discount:number;
    stock:number;
}