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

export interface PatientProfile {
  firstName: string,
  lastName: string,
  pesel: string,
  phoneNumber: string,
  email: string,
  address: Address
}

export interface PatientRegistration {
  firstName: string,
  lastName: string,
  pesel: string,
  email: string,
  phoneNumber: string,
  address: Address,
  password?: string,
}
