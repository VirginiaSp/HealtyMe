/* src/main/webapp/app/modules/home/home.tsx */
import React, { useMemo, useState } from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader, Input, Label, FormGroup, Row, Col } from 'reactstrap';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import elLocale from '@fullcalendar/core/locales/el';
import type { DateSelectArg, EventClickArg, EventContentArg, EventInput } from '@fullcalendar/core';
import { addDays, addMinutes, format, isAfter, isBefore, parseISO } from 'date-fns';
import { Link } from 'react-router-dom';

type MedPayload = {
  type: 'med';
  medName: string;
  startDate: string; // ISO date (yyyy-MM-dd)
  endDate: string; // ISO date (exclusive)
  times: string[]; // HH:mm
  colorIdx: number;
};

type AptPayload = {
  type: 'apt';
  dateTime: string; // ISO
  doctorFree: string;
  specialty?: string;
  address?: string;
  notes?: string;
};

type AnyPayload = MedPayload | AptPayload;

const MED_COLORS = ['#E0F7FA', '#80DEEA', '#4FC3F7', '#29B6F6', '#0288D1', '#01579B'] as const;
const APT_COLOR = '#FF9800';

const toISODate = (d: Date) => format(d, 'yyyy-MM-dd');

const combineDateTime = (dateISO: string, timeHHmm: string) => {
  // dateISO: yyyy-MM-dd, timeHHmm: HH:mm
  return `${dateISO}T${timeHHmm}:00`;
};

const dateRangeDays = (startISO: string, endExclusiveISO: string) => {
  const days: string[] = [];
  let cursor = parseISO(startISO);
  const end = parseISO(endExclusiveISO);
  while (isBefore(cursor, end)) {
    days.push(toISODate(cursor));
    cursor = addDays(cursor, 1);
  }
  return days;
};

const Home: React.FC = () => {
  const [events, setEvents] = useState<EventInput[]>([]);
  const [choiceOpen, setChoiceOpen] = useState(false);
  const [medOpen, setMedOpen] = useState(false);
  const [aptOpen, setAptOpen] = useState(false);
  const [selectedDateISO, setSelectedDateISO] = useState<string | null>(null);

  // Edit context (όταν ανοίγουμε από eventClick)
  const [editingEventId, setEditingEventId] = useState<string | null>(null);
  const [editingPayload, setEditingPayload] = useState<AnyPayload | null>(null);

  // --- Medication form state ---
  const [medName, setMedName] = useState('');
  const [medStartDate, setMedStartDate] = useState<string>('');
  const [medEndDate, setMedEndDate] = useState<string>(''); // exclusive
  const [medTimes, setMedTimes] = useState<string[]>(['09:00']);
  const [medColorIdx, setMedColorIdx] = useState<number>(0);

  // --- Appointment form state ---
  const [aptDateTime, setAptDateTime] = useState<string>(''); // ISO
  const [aptDoctorFree, setAptDoctorFree] = useState('');
  const [aptSpecialty, setAptSpecialty] = useState('');
  const [aptAddress, setAptAddress] = useState('');
  const [aptNotes, setAptNotes] = useState('');

  const resetForms = () => {
    setEditingEventId(null);
    setEditingPayload(null);
    setMedName('');
    setMedTimes(['09:00']);
    setMedColorIdx(0);
    setAptDoctorFree('');
    setAptSpecialty('');
    setAptAddress('');
    setAptNotes('');
    setAptDateTime('');
  };

  // ---------- Calendar handlers ----------
  const onDateSelect = (arg: DateSelectArg) => {
    // Πατάει μέρα → ανοίγει επιλογή "Αγωγή" ή "Ραντεβού"
    const dayISO = toISODate(arg.start);
    setSelectedDateISO(dayISO);
    // initial values για φόρμες
    setMedStartDate(dayISO);
    setMedEndDate(toISODate(addDays(arg.start, 1))); // default 1 ημέρα
    setAptDateTime(`${dayISO}T09:00:00`);
    setChoiceOpen(true);
  };

  const openMedicationForEdit = (evtId: string, payload: MedPayload) => {
    setEditingEventId(evtId);
    setEditingPayload(payload);
    setMedName(payload.medName);
    setMedStartDate(payload.startDate);
    setMedEndDate(payload.endDate);
    setMedTimes(payload.times);
    setMedColorIdx(payload.colorIdx);
    setChoiceOpen(false);
    setMedOpen(true);
  };

  const openAppointmentForEdit = (evtId: string, payload: AptPayload) => {
    setEditingEventId(evtId);
    setEditingPayload(payload);
    setAptDateTime(payload.dateTime);
    setAptDoctorFree(payload.doctorFree ?? '');
    setAptSpecialty(payload.specialty ?? '');
    setAptAddress(payload.address ?? '');
    setAptNotes(payload.notes ?? '');
    setChoiceOpen(false);
    setAptOpen(true);
  };

  const onEventClick = (arg: EventClickArg) => {
    const evt = arg.event;
    const p = evt.extendedProps as AnyPayload;
    if ((p as MedPayload).type === 'med') {
      openMedicationForEdit(evt.id, p as MedPayload);
    } else {
      openAppointmentForEdit(evt.id, p as AptPayload);
    }
  };

  // ---------- Build events ----------
  const buildMedicationEvents = (payload: MedPayload): EventInput[] => {
    const days = dateRangeDays(payload.startDate, payload.endDate);
    const bg = MED_COLORS[payload.colorIdx % MED_COLORS.length];
    const rows: EventInput[] = [];
    days.forEach(d => {
      payload.times.forEach(t => {
        const startISO = combineDateTime(d, t);
        rows.push({
          id: `med_${payload.medName}_${d}_${t}`,
          title: `${format(parseISO(startISO), 'HH:mm')} ${payload.medName}`,
          start: startISO,
          allDay: false,
          backgroundColor: bg,
          borderColor: bg,
          textColor: '#0d0d0d',
          display: 'block',
          extendedProps: payload,
        });
      });
    });
    return rows;
  };

  const buildAppointmentEvent = (payload: AptPayload): EventInput => {
    const start = parseISO(payload.dateTime);
    return {
      id: `apt_${payload.dateTime}`,
      title: `${format(start, 'HH:mm')} ${payload.doctorFree || 'Ραντεβού'}`,
      start: payload.dateTime,
      backgroundColor: APT_COLOR,
      borderColor: APT_COLOR,
      textColor: '#0d0d0d',
      extendedProps: payload,
    };
  };

  // ---------- Save / Delete ----------
  const saveMedication = () => {
    const payload: MedPayload = {
      type: 'med',
      medName,
      startDate: medStartDate,
      endDate: medEndDate,
      times: medTimes,
      colorIdx: medColorIdx,
    };
    const newEvents = buildMedicationEvents(payload);

    setEvents(prev => {
      // αν κάνουμε edit, καθάρισε παλιά instances της ίδιας αγωγής (με βάση id prefix)
      if (editingEventId && editingPayload && editingPayload.type === 'med') {
        const prefix = `med_${editingPayload.medName}_`;
        return [...prev.filter(e => typeof e.id !== 'string' || !String(e.id).startsWith(prefix)), ...newEvents];
      }
      return [...prev, ...newEvents];
    });
    resetForms();
    setMedOpen(false);
  };

  const deleteMedication = () => {
    if (editingPayload && editingPayload.type === 'med') {
      const prefix = `med_${editingPayload.medName}_`;
      setEvents(prev => prev.filter(e => typeof e.id !== 'string' || !String(e.id).startsWith(prefix)));
    }
    resetForms();
    setMedOpen(false);
  };

  const saveAppointment = () => {
    const payload: AptPayload = {
      type: 'apt',
      dateTime: aptDateTime,
      doctorFree: aptDoctorFree,
      specialty: aptSpecialty || undefined,
      address: aptAddress || undefined,
      notes: aptNotes || undefined,
    };
    const evt = buildAppointmentEvent(payload);

    setEvents(prev => {
      if (editingEventId && editingPayload && editingPayload.type === 'apt') {
        // αντικατάσταση
        const others = prev.filter(e => e.id !== editingEventId);
        return [...others, evt];
      }
      return [...prev, evt];
    });

    resetForms();
    setAptOpen(false);
  };

  const deleteAppointment = () => {
    if (editingEventId) {
      setEvents(prev => prev.filter(e => e.id !== editingEventId));
    }
    resetForms();
    setAptOpen(false);
  };

  // ---------- Renderers ----------
  const renderEventContent = (arg: EventContentArg) => {
    return (
      <div>
        <span>{arg.event.title}</span>
      </div>
    );
  };

  // ---------- UI ----------
  const onChooseMedication = () => {
    setChoiceOpen(false);
    setMedOpen(true);
  };
  const onChooseAppointment = () => {
    setChoiceOpen(false);
    setAptOpen(true);
  };

  const canSaveMed =
    medName.trim().length > 0 &&
    medStartDate &&
    medEndDate &&
    isAfter(parseISO(medEndDate), parseISO(medStartDate)) &&
    medTimes.every(t => /^\d{2}:\d{2}$/.test(t));

  return (
    <div className="container-fluid mt-3">
      <FullCalendar
        plugins={[dayGridPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        locales={[elLocale]}
        locale="el"
        selectable
        selectMirror
        dayMaxEventRows
        height="auto"
        displayEventTime={false}
        eventTimeFormat={{ hour: '2-digit', minute: '2-digit', hour12: true }}
        events={events}
        select={onDateSelect}
        eventClick={onEventClick}
        eventContent={renderEventContent}
      />
      <Row className="mt-4 g-3">
        <Col md="4">
          <Link to="/examination-record" className="text-decoration-none">
            <div className="p-4 border rounded-4 h-100 d-flex align-items-center justify-content-center bg-light hover-shadow">
              <span className="fw-bold">Ιστορικό Εξετάσεων</span>
            </div>
          </Link>
        </Col>

        <Col md="4">
          <Link to="/medication" className="text-decoration-none">
            <div className="p-4 border rounded-4 h-100 d-flex align-items-center justify-content-center bg-light hover-shadow">
              <span className="fw-bold">Φάρμακα</span>
            </div>
          </Link>
        </Col>

        <Col md="4">
          <Link to="/doctor" className="text-decoration-none">
            <div className="p-4 border rounded-4 h-100 d-flex align-items-center justify-content-center bg-light hover-shadow">
              <span className="fw-bold">Γιατροί</span>
            </div>
          </Link>
        </Col>
      </Row>

      {/* Επιλογή τύπου */}
      <Modal isOpen={choiceOpen} toggle={() => setChoiceOpen(false)}>
        <ModalHeader toggle={() => setChoiceOpen(false)}>Τι θέλεις να προσθέσεις;</ModalHeader>
        <ModalBody>
          <p>Ημερομηνία: {selectedDateISO ? format(parseISO(`${selectedDateISO}T00:00:00`), 'dd/MM/yyyy') : ''}</p>
          <Row>
            <Col>
              <Button color="primary" onClick={onChooseMedication} className="w-100">
                Προσθήκη Αγωγής
              </Button>
            </Col>
            <Col>
              <Button color="warning" onClick={onChooseAppointment} className="w-100">
                Προσθήκη Ραντεβού
              </Button>
            </Col>
          </Row>
        </ModalBody>
      </Modal>

      {/* Modal Αγωγής */}
      <Modal isOpen={medOpen} toggle={() => setMedOpen(false)}>
        <ModalHeader toggle={() => setMedOpen(false)}>
          {editingPayload?.type === 'med' ? 'Επεξεργασία Αγωγής' : 'Προσθήκη Αγωγής'}
        </ModalHeader>
        <ModalBody>
          <Row className="mb-2">
            <Col md="6">
              <FormGroup>
                <Label>Από</Label>
                <Input type="date" value={medStartDate} onChange={e => setMedStartDate(e.target.value)} />
              </FormGroup>
            </Col>
            <Col md="6">
              <FormGroup>
                <Label>Έως (επόμενη μέρα από την τελευταία)</Label>
                <Input type="date" value={medEndDate} onChange={e => setMedEndDate(e.target.value)} />
              </FormGroup>
            </Col>
          </Row>

          <FormGroup>
            <Label>Φάρμακο</Label>
            <Input value={medName} onChange={e => setMedName(e.target.value)} placeholder="π.χ. amoxil" />
          </FormGroup>

          <FormGroup>
            <Label>Ώρες λήψης</Label>
            {medTimes.map((t, idx) => (
              <Row key={`time-${idx}`} className="g-2 mb-2">
                <Col>
                  <Input
                    type="time"
                    value={t}
                    onChange={e => setMedTimes(prev => prev.map((val, i) => (i === idx ? e.target.value : val)))}
                  />
                </Col>
                <Col xs="auto">
                  <Button
                    color="light"
                    onClick={() => setMedTimes(prev => prev.filter((_, i) => i !== idx))}
                    disabled={medTimes.length === 1}
                    title="Αφαίρεση ώρας"
                  >
                    −
                  </Button>
                </Col>
                {idx === medTimes.length - 1 && (
                  <Col xs="auto">
                    <Button color="primary" onClick={() => setMedTimes(prev => [...prev, '09:00'])} title="Προσθήκη ώρας">
                      +
                    </Button>
                  </Col>
                )}
              </Row>
            ))}
          </FormGroup>

          <FormGroup>
            <Label>Χρώμα (αυτόματη εναλλαγή)</Label>
            <Row className="g-2">
              {MED_COLORS.map((c, i) => (
                <Col key={c} xs="2">
                  <Button
                    className="w-100"
                    style={{ backgroundColor: c, borderColor: c }}
                    active={i === medColorIdx}
                    onClick={() => setMedColorIdx(i)}
                  >
                    &nbsp;
                  </Button>
                </Col>
              ))}
            </Row>
          </FormGroup>
        </ModalBody>
        <ModalFooter>
          {editingPayload?.type === 'med' && (
            <Button color="danger" onClick={deleteMedication}>
              Διαγραφή
            </Button>
          )}
          <Button color="secondary" onClick={() => setMedOpen(false)}>
            Άκυρο
          </Button>
          <Button color="primary" onClick={saveMedication} disabled={!canSaveMed}>
            Αποθήκευση
          </Button>
        </ModalFooter>
      </Modal>

      {/* Modal Ραντεβού */}
      <Modal isOpen={aptOpen} toggle={() => setAptOpen(false)}>
        <ModalHeader toggle={() => setAptOpen(false)}>
          {editingPayload?.type === 'apt' ? 'Επεξεργασία Ραντεβού' : 'Προσθήκη Ραντεβού'}
        </ModalHeader>
        <ModalBody>
          <FormGroup>
            <Label>Ημερομηνία & Ώρα</Label>
            <Input type="datetime-local" value={aptDateTime.slice(0, 16)} onChange={e => setAptDateTime(`${e.target.value}:00`)} />
          </FormGroup>

          <Row className="mb-2">
            <Col md="6">
              <FormGroup>
                <Label>Ιατρός (ελεύθερο κείμενο)</Label>
                <Input value={aptDoctorFree} onChange={e => setAptDoctorFree(e.target.value)} placeholder="π.χ. Παπαδόπουλος" />
              </FormGroup>
            </Col>
            <Col md="6">
              <FormGroup>
                <Label>Ειδικότητα</Label>
                <Input value={aptSpecialty} onChange={e => setAptSpecialty(e.target.value)} placeholder="π.χ. Νευρολόγος" />
              </FormGroup>
            </Col>
          </Row>

          <FormGroup>
            <Label>Διεύθυνση</Label>
            <Input value={aptAddress} onChange={e => setAptAddress(e.target.value)} placeholder="π.χ. Ελ. Βενιζέλου 10" />
          </FormGroup>

          <FormGroup>
            <Label>Παρατηρήσεις</Label>
            <Input type="textarea" value={aptNotes} onChange={e => setAptNotes(e.target.value)} placeholder="π.χ. Μη φας 1 ώρα πριν" />
          </FormGroup>
        </ModalBody>
        <ModalFooter>
          {editingPayload?.type === 'apt' && (
            <Button color="danger" onClick={deleteAppointment}>
              Διαγραφή
            </Button>
          )}
          <Button color="secondary" onClick={() => setAptOpen(false)}>
            Άκυρο
          </Button>
          <Button
            color="primary"
            onClick={saveAppointment}
            disabled={!aptDateTime || !/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}$/.test(aptDateTime)}
          >
            Αποθήκευση
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

export default Home;
