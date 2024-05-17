import {Address} from "./address";

export interface Patient {
  firstName: string,
  lastName: string,
  pesel: string,
  phoneNumber: string,
  email: string,
  gender: string,
  address: Address
}
