import { StoreItem } from './storeItem';

export interface Store {
    id:number;
    address:String;
    storeItem?:StoreItem[];
    storeName:String;
}