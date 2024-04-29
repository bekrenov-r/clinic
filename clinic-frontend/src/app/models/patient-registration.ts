import { Address } from "./address"

export interface PatientRegistration {
    firstName: string,
    lastName: string,
    pesel: string,
    email: string,
    phoneNumber: string,
    address: Address,
    password?: string,
}