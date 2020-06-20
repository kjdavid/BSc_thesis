import { Company } from './company';
import { Item } from './item';

export interface CompanyItem {
    id?:number;
    itemName?:String;
    item?:Item;
    pruchasePrice?: number;
    description?:number;
    company?:Company;
    base64str?:String;
}