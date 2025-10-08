package com.hospital.messaging.controller;



import com.hospital.messaging.model.MedicineSchedule;
import com.hospital.messaging.repository.MedicineScheduleRepository;
import com.hospital.messaging.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/medicine-schedule")
public class MedicineScheduleController {

    @Autowired
    private MedicineScheduleRepository medicineScheduleRepository;

    @Autowired
    private WhatsAppService whatsAppService;

    @PostMapping("/add")
    public String addMedicineSchedule(@RequestBody MedicineSchedule schedule) {
        medicineScheduleRepository.save(schedule);

        // Notify patient via WhatsApp
        StringBuilder msg = new StringBuilder("📋 Hi " + schedule.getPatientName() + ", your medicine schedule has been created:\n");

        for (MedicineSchedule.MedicineEntry entry : schedule.getMedicines()) {
            msg.append("💊 *").append(entry.getMedicineName()).append("* - ")
                    .append(entry.getDosage()).append("\n🕒 Times:\n");

            for (var time : entry.getScheduleTimes()) {
                msg.append("  - ").append(time.toString()).append("\n");
            }
        }

        whatsAppService.sendWhatsAppMessage(schedule.getPatientPhone(), msg.toString());

        return "✅ Schedule created & WhatsApp notification sent!";
    }

    @GetMapping("/all")
    public List<MedicineSchedule> getAllSchedules() {
        return medicineScheduleRepository.findAll();
    }
}
