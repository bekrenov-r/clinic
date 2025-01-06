import {PatientRegistration} from "../patient";

export interface PatientAppointmentRequest {
  date: string,
  time: string,
  departmentId: number,
  doctorId?: number,
  anyDoctor: boolean
}

export interface UnregisteredPatientAppointmentRequest extends PatientAppointmentRequest{
  patient: PatientRegistration
}
