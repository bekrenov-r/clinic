import {Department} from "../department";
import {Patient} from "../patient";
import {PersonDto} from "../person-dto";

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
