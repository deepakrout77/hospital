package com.hospital.messaging.controller;

import com.hospital.messaging.model.Appointment;
import com.hospital.messaging.model.DoctorAvailability;
import com.hospital.messaging.repository.AppointmentRepository;
import com.hospital.messaging.repository.DoctorAvailabilityRepository;
import com.hospital.messaging.service.WhatsAppService;
import com.hospital.messaging.service.ZoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private ZoomService zoomService;

    @PostMapping("/book")
    public String bookAppointment(@RequestBody Appointment appointment) {
        DoctorAvailability availability = availabilityRepository.findByDoctorPhone(appointment.getDoctorPhone());

        if (availability == null) {
            return " Doctor not found!";
        }

        String dayOfWeek = appointment.getAppointmentTime().getDayOfWeek().toString();
        if (!availability.getAvailableDays().contains(dayOfWeek)) {
            return "Doctor is not available on " + dayOfWeek;
        }

        LocalTime appointmentTime = appointment.getAppointmentTime().toLocalTime();
        if (appointmentTime.isBefore(availability.getStartTime()) || appointmentTime.isAfter(availability.getEndTime())) {
            return "Appointment time is outside doctor’s working hours.";
        }

        LocalDate appointmentDate = appointment.getAppointmentTime().toLocalDate();
        LocalDateTime startOfDay = appointmentDate.atStartOfDay();
        LocalDateTime endOfDay = appointmentDate.atTime(LocalTime.MAX);

        List<Appointment> todaysAppointments = appointmentRepository.findByDoctorPhoneAndAppointmentTimeBetween(
                appointment.getDoctorPhone(), startOfDay, endOfDay
        );

        if (todaysAppointments.size() >= availability.getMaxPatientsPerDay()) {
            return "Doctor has reached max appointments for the day.";
        }

        appointment.setDoctorName(availability.getDoctorName());

        // If VIRTUAL, create Zoom meeting
        if ("VIRTUAL".equalsIgnoreCase(appointment.getConsultationType())) {
            String zoomStartTime = appointment.getAppointmentTime().atZone(ZoneId.of("Asia/Kolkata"))
                    .withZoneSameInstant(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

            String zoomLink = zoomService.createZoomMeeting("Consultation with " + appointment.getPatientName(), zoomStartTime);
            appointment.setZoomLink(zoomLink); // You must have zoomLink field in Appointment model
        }

        appointmentRepository.save(appointment);

        // WhatsApp message
        String message = buildConfirmationMessage(appointment);
        whatsAppService.sendWhatsAppMessage(appointment.getPatientPhone(), message);

        if ("VIRTUAL".equalsIgnoreCase(appointment.getConsultationType())) {
            whatsAppService.sendWhatsAppMessage(
                    appointment.getDoctorPhone(),
                    "📅 New virtual consultation booked with " + appointment.getPatientName() +
                            " at " + appointment.getAppointmentTime() + "\n🔗 Zoom Link: " + appointment.getZoomLink()
            );
        }

        return "Appointment booked successfully.";
    }

    private String buildConfirmationMessage(Appointment appointment) {
        String message = "✅ Appointment Confirmed!\n"
                + "Doctor: " + appointment.getDoctorName() + "\n"
                + "Type: " + appointment.getConsultationType() + "\n"
                + "Time: " + appointment.getAppointmentTime();

        if ("VIRTUAL".equalsIgnoreCase(appointment.getConsultationType())) {
            message += "\n🔗 Zoom Link: " + appointment.getZoomLink();
        }

        message += "\nWe look forward to seeing you.";
        return message;
    }
}
