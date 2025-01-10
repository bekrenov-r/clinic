import {Address} from "./address";
import {Department} from "./department";

export interface DoctorProfile {
  id: number,
  firstName: string,
  lastName: string,
  phoneNumber: string,
  email: string,
  pesel: string,
  occupation: string,
  address: Address,
  department: Department
}

export interface DoctorPublicProfile {
  id: string,
  firstName: string,
  lastName: string,
  occupation: string
}
