import {Address} from "./address";

export interface Employee {
  id: number,
  firstName: string,
  lastName: string,
  occupation: string
}

export interface EmployeeRequest {
  firstName: string,
  lastName: string,
  email: string,
  pesel: string,
  phoneNumber: string,
  address: Address,
  occupation: string
}
