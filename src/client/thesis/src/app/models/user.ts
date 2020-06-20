import { RegistrationCode } from './registrationCode';
import { Store } from './store';

export interface User {
    id?: number;
    //version?: number;
    username: string;
    password?: string;
    email?: string;
    role?: string;
    regCode?: string;
    generatedCodes?: RegistrationCode[];
    companyId?: number;
    store?:Store;
  }