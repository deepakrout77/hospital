package com.hospital.messaging.scheduler;

import com.hospital.messaging.model.MedicineSchedule;
import com.hospital.messaging.repository.MedicineScheduleRepository;
import com.hospital.messaging.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MedicineReminderScheduler {

    @Autowired
    private MedicineScheduleRepository medicineScheduleRepository;

    @Autowired
    private WhatsAppService whatsAppService;

    // Runs every minute
    @Scheduled(fixedRate = 60000)
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

        for (MedicineSchedule schedule : medicineScheduleRepository.findAll()) {
            for (MedicineSchedule.MedicineEntry entry : schedule.getMedicines()) {
                for (LocalDateTime time : entry.getScheduleTimes()) {
                    if (time.withSecond(0).withNano(0).equals(now)) {
                        String msg = "💊 Reminder: Please take *" + entry.getMedicineName() +
                                "* (" + entry.getDosage() + ") now.";
                        whatsAppService.sendWhatsAppMessage(schedule.getPatientPhone(), msg);
                    }
                }
            }
        }
    }
}
