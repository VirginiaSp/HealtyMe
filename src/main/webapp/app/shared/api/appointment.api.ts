import axios from 'axios';

export interface AppointmentDTO {
  id: number;
  startDateTime: string; // ISO από backend
  durationMinutes?: number; // μπορεί να λείπει
  notes?: string;
}

export const fetchAppointments = async (): Promise<AppointmentDTO[]> => {
  const res = await axios.get<AppointmentDTO[]>('/api/appointments', {
    params: { sort: 'startDateTime,asc' },
  });
  return res.data;
};

// μικρό helper για +minutes
export const addMinutes = (iso: string, minutes = 30) => {
  const d = new Date(iso);
  d.setMinutes(d.getMinutes() + minutes);
  return d.toISOString();
};
