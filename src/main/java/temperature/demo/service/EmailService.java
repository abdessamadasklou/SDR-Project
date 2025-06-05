package temperature.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String fromEmail = "norn71716@gmail.com"; // Replace with your sender email
    private final String toEmail = "dds3157f@gmail.com"; // Replace with recipient email

    public void sendAlertEmail(String capteurName, double temperature, double humidity) throws MessagingException {
        String subject = "Alert: Sensor " + capteurName + " triggered an alert";
        String htmlMsg = "<html><body>" +
                "<h2 style='color:red;'>Sensor Alert</h2>" +
                "<p><strong>Sensor Name:</strong> <span style='color:blue;'>" + capteurName + "</span></p>" +
                "<p><strong>Temperature:</strong> <span style='color:orange;'>" + temperature + " °C</span></p>" +
                "<p><strong>Humidity:</strong> <span style='color:green;'>" + humidity + " %</span></p>" +
                "<p>Please check the sensor immediately.</p>" +
                "</body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlMsg, true); // true indicates HTML

        mailSender.send(message);
    }
}
