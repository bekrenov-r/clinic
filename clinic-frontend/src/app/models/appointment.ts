import {Department} from "./department";
import {Patient, PatientRegistration} from "./patient";
import {PersonDto} from "./person-dto";

export interface Appointment {
  id: number,
  date: string,
  startTime: string,
  endTime: string,
  status: string,
  prescription: string,
  details: string,
  department: Department,
  departmentAddress?: string,
  patient: Patient,
  doctor: PersonDto
}

export interface PatientAppointmentRequest {
  date: string,
  time: string,
  departmentId: number,
  doctorId?: number,
  anyDoctor: boolean
}

export interface UnregisteredPatientAppointmentRequest extends PatientAppointmentRequest {
  patient: PatientRegistration
}

export interface DoctorAppointmentRequest {
  date: string,
  time: string,
  patientId?: number,
  patient?: PatientRegistration
}
