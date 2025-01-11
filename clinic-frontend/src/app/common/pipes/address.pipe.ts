import { Pipe, PipeTransform } from '@angular/core';
import {Address} from "../../models/address";

@Pipe({
  name: 'address'
})
export class AddressPipe implements PipeTransform {
  transform(address: Address, format: 'short' | 'long' = 'long'): string {
    address = Object.assign(new Address('', '', '', '', ''), address);
    return format === 'short' ? address.toSimpleString() : address.toLongString();
  }
}
