import {Address} from "./address";
import {Employee} from "./employee";

export interface Department {
  id: number,
  name: string,
  specialization: string,
  address: Address
}

export interface DepartmentDetails extends Department {
  autoConfirmAppointment: boolean,
  employees: Employee[]
}

export interface DepartmentRequest {
  name: string,
  specialization: string,
  autoConfirmAppointment: boolean,
  address: Address
}
