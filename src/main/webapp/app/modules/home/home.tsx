import React, { useMemo, useState } from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader, Input, Label, FormGroup, Row, Col } from 'reactstrap';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import elLocale from '@fullcalendar/core/locales/el';
import type { DateSelectArg, EventClickArg, EventContentArg, EventInput } from '@fullcalendar/core';
import { addDays, addMinutes, format, isAfter, isBefore, parseISO } from 'date-fns';
import { Link, useNavigate } from 'react-router-dom';
import './home.scss';

type MedPayload = {
  type: 'med';
  medName: string;
  startDate: string;
  endDate: string;
  times: string[];
  colorIdx: number;
};

type AptPayload = {
  type: 'apt';
  dateTime: string;
  doctorFree: string;
  specialty?: string;
  address?: string;
};

const Home: React.FC = () => {
  const navigate = useNavigate();
  const [events] = useState<EventInput[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedDate, setSelectedDate] = useState('');

  const handleDateSelect = (selectInfo: DateSelectArg) => {
    setSelectedDate(selectInfo.startStr);
    setIsModalOpen(true);
  };

  const handleEventClick = (clickInfo: EventClickArg) => {
    // Handle event click
  };

  const renderEventContent = (eventInfo: EventContentArg) => {
    return (
      <div>
        <b>{eventInfo.timeText}</b>
        <i>{eventInfo.event.title}</i>
      </div>
    );
  };

  // Navigation functions for feature cards
  const navigateToExaminations = () => {
    navigate('/examination-record');
  };

  const navigateToMedications = () => {
    navigate('/medication');
  };

  const navigateToDoctors = () => {
    navigate('/doctor');
  };

  // Modal button handlers
  const handleAddMedication = () => {
    setIsModalOpen(false);
    navigate('/medication-plan/new');
  };

  const handleAddAppointment = () => {
    setIsModalOpen(false);
    navigate('/appointment/new');
  };

  return (
    <div style={{ padding: '20px', background: 'linear-gradient(135deg, #e8f5f3 0%, #d1e7dd 100%)', minHeight: '100vh' }}>
      {/* Header Section */}
      <div style={{ marginBottom: '30px', textAlign: 'center' }}>
        <h1 style={{ color: '#4a9b8f', marginBottom: '10px', fontSize: '2.5rem', fontWeight: '600' }}>HealthyMe</h1>
        <p style={{ color: '#6c757d', fontSize: '1.1rem' }}>Το σύστημα διαχείρισης της υγείας σας</p>
      </div>

      {/* Calendar Section */}
      <div
        style={{
          background: 'linear-gradient(135deg, #ffffff 0%, #f8fffe 100%)',
          borderRadius: '12px',
          padding: '30px',
          boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
          marginBottom: '40px',
        }}
      >
        <FullCalendar
          plugins={[dayGridPlugin, interactionPlugin]}
          headerToolbar={{
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth',
          }}
          initialView="dayGridMonth"
          locale={elLocale}
          editable={true}
          selectable={true}
          selectMirror={true}
          dayMaxEvents={true}
          weekends={true}
          events={events}
          select={handleDateSelect}
          eventContent={renderEventContent}
          eventClick={handleEventClick}
          height="auto"
          buttonText={{
            today: 'Σήμερα',
            month: 'Μήνας',
            week: 'Εβδομάδα',
            day: 'Ημέρα',
          }}
        />
      </div>

      {/* Feature Cards */}
      <Row className="g-4">
        <Col md="4">
          <div
            style={{
              background: 'white',
              borderRadius: '12px',
              padding: '30px',
              textAlign: 'center',
              boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
              transition: 'all 0.3s ease',
              cursor: 'pointer',
              height: '220px',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              border: '1px solid transparent',
            }}
            onClick={navigateToExaminations}
            onMouseEnter={e => {
              e.currentTarget.style.transform = 'translateY(-5px)';
              e.currentTarget.style.boxShadow = '0 8px 30px rgba(74, 155, 143, 0.2)';
              e.currentTarget.style.borderColor = 'rgba(74, 155, 143, 0.3)';
            }}
            onMouseLeave={e => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 4px 20px rgba(0,0,0,0.1)';
              e.currentTarget.style.borderColor = 'transparent';
            }}
          >
            <div style={{ fontSize: '4rem', marginBottom: '15px', color: '#4a9b8f' }}>📋</div>
            <h3 style={{ color: '#4a9b8f', marginBottom: '15px', fontSize: '1.3rem', fontWeight: '600' }}>Ιστορικό Εξετάσεων</h3>
            <p style={{ color: '#6c757d', fontSize: '0.95rem', lineHeight: '1.5', margin: 0 }}>
              Παρακολουθήστε και διαχειριστείτε το ιστορικό των ιατρικών σας εξετάσεων
            </p>
          </div>
        </Col>

        <Col md="4">
          <div
            style={{
              background: 'white',
              borderRadius: '12px',
              padding: '30px',
              textAlign: 'center',
              boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
              transition: 'all 0.3s ease',
              cursor: 'pointer',
              height: '220px',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              border: '1px solid transparent',
            }}
            onClick={navigateToMedications}
            onMouseEnter={e => {
              e.currentTarget.style.transform = 'translateY(-5px)';
              e.currentTarget.style.boxShadow = '0 8px 30px rgba(74, 155, 143, 0.2)';
              e.currentTarget.style.borderColor = 'rgba(74, 155, 143, 0.3)';
            }}
            onMouseLeave={e => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 4px 20px rgba(0,0,0,0.1)';
              e.currentTarget.style.borderColor = 'transparent';
            }}
          >
            <div style={{ fontSize: '4rem', marginBottom: '15px', color: '#4a9b8f' }}>💊</div>
            <h3 style={{ color: '#4a9b8f', marginBottom: '15px', fontSize: '1.3rem', fontWeight: '600' }}>Φάρμακα</h3>
            <p style={{ color: '#6c757d', fontSize: '0.95rem', lineHeight: '1.5', margin: 0 }}>
              Οργανώστε τη φαρμακευτική σας αγωγή και λάβετε υπενθυμίσεις
            </p>
          </div>
        </Col>

        <Col md="4">
          <div
            style={{
              background: 'white',
              borderRadius: '12px',
              padding: '30px',
              textAlign: 'center',
              boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
              transition: 'all 0.3s ease',
              cursor: 'pointer',
              height: '220px',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              border: '1px solid transparent',
            }}
            onClick={navigateToDoctors}
            onMouseEnter={e => {
              e.currentTarget.style.transform = 'translateY(-5px)';
              e.currentTarget.style.boxShadow = '0 8px 30px rgba(74, 155, 143, 0.2)';
              e.currentTarget.style.borderColor = 'rgba(74, 155, 143, 0.3)';
            }}
            onMouseLeave={e => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 4px 20px rgba(0,0,0,0.1)';
              e.currentTarget.style.borderColor = 'transparent';
            }}
          >
            <div style={{ fontSize: '4rem', marginBottom: '15px', color: '#4a9b8f' }}>👨‍⚕️</div>
            <h3 style={{ color: '#4a9b8f', marginBottom: '15px', fontSize: '1.3rem', fontWeight: '600' }}>Γιατροί</h3>
            <p style={{ color: '#6c757d', fontSize: '0.95rem', lineHeight: '1.5', margin: 0 }}>
              Διαχειριστείτε τις επαφές με τους γιατρούς σας και τα ραντεβού σας
            </p>
          </div>
        </Col>
      </Row>

      {/* Modal for adding appointments/medications */}
      <Modal isOpen={isModalOpen} toggle={() => setIsModalOpen(false)} size="md">
        <ModalHeader
          toggle={() => setIsModalOpen(false)}
          style={{
            background: '#4a9b8f',
            color: 'white',
            borderRadius: '12px 12px 0 0',
            borderBottom: 'none',
          }}
        >
          Τι θέλεις να προσθέσεις;
        </ModalHeader>
        <ModalBody style={{ padding: '30px', textAlign: 'center' }}>
          <p style={{ marginBottom: '25px', fontSize: '1.1rem', color: '#495057' }}>
            Ημερομηνία: <strong>{selectedDate}</strong>
          </p>
          <div style={{ display: 'flex', justifyContent: 'center', gap: '20px', flexWrap: 'wrap' }}>
            <Button
              color="primary"
              size="lg"
              style={{
                background: '#007bff',
                borderColor: '#007bff',
                padding: '12px 30px',
                fontSize: '1.1rem',
                borderRadius: '8px',
              }}
              onClick={handleAddMedication}
            >
              Προσθήκη Αγωγής
            </Button>
            <Button
              color="warning"
              size="lg"
              style={{
                background: '#ffc107',
                borderColor: '#ffc107',
                color: '#212529',
                padding: '12px 30px',
                fontSize: '1.1rem',
                borderRadius: '8px',
              }}
              onClick={handleAddAppointment}
            >
              Προσθήκη Ραντεβού
            </Button>
          </div>
        </ModalBody>
      </Modal>
    </div>
  );
};

export default Home;
