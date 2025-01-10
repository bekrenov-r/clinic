import { Pipe, PipeTransform } from '@angular/core';
import {Department} from "../../models/department";
import {Address} from "../../models/address";

@Pipe({
  name: 'department'
})
export class DepartmentPipe implements PipeTransform {
  transform(department: Department): string {
    if(!department) {
      return '';
    }
    const address: Address = Object.assign(new Address('', '', '', '', ''), department.address);
    return `${department.name}, ${address.toSimpleString()}`;
  }
}
