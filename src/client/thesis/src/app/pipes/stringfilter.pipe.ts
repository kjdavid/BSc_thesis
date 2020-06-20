import {Pipe} from "@angular/core";
import { StoreItem } from '../models/storeItem';
import { CompanyItem } from '../models/companyItem';

/**
 * A simple string filter, since Angular does not yet have a filter pipe built in.
 */
@Pipe({
    name: 'stringFilter'
})
export class StringfilterPipe {

  transform(value: any[], q: string) {
    if(value.length!=0 && value[0].itemName!=undefined){
      if (!q || q === '') {
        return value;
      }
      return value.filter(item => -1 < item.itemName.toLowerCase().indexOf(q.toLowerCase()));
    }
      if (!q || q === '') {
        return value;
      }
      return value.filter(item => -1 < item.companyItem.itemName.toLowerCase().indexOf(q.toLowerCase()));
  }
}