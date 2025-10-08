package com.hospital.messaging.service;


import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

    public void sendWhatsAppMessage(String toNumber, String body) {
        Message message = Message.creator(
                new PhoneNumber("whatsapp:" + toNumber),
                new PhoneNumber(fromNumber),
                body
        ).create();
        System.out.println("✅ WhatsApp message sent: " + message.getSid());
    }
}
