import { StoreItem } from './storeItem';
import { User } from './user';

export interface Sale {
    id: number;
    storeItem: StoreItem;
    count:number;
    sellingPrice:number;
    seller: User;
    dateOfSale: Date;
}