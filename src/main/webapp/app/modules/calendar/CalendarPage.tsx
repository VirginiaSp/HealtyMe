import React from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

const CalendarPage: React.FC = () => {
  return (
    <div className="container mt-4">
      <h2>Calendar</h2>
      <FullCalendar
        plugins={[dayGridPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        height="auto"
        events={[{ title: 'Check up', date: new Date().toISOString().slice(0, 10) }]}
        dateClick={() => {
          // Πάτα σε μέρα -> άνοιξε καινούριο ραντεβού
          window.location.href = '/appointment/new';
        }}
      />
    </div>
  );
};

export default CalendarPage;
