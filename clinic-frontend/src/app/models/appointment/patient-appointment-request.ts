export interface PatientAppointmentRequest {
  date: string,
  time: string,
  departmentId: number,
  doctorId?: number,
  anyDoctor: boolean
}
