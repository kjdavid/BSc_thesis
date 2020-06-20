import { User } from './user';
import { Store } from './store';
import { CompanyItem } from './companyItem';

export interface Company {
    id:number;
    companyName:String;
    companyItem?: CompanyItem[];
    stores?:Store[];
    users?:User[];
}