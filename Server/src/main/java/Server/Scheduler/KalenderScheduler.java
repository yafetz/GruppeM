package Server.Scheduler;

import Server.Modell.Termin;
import Server.Repository.KalenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class KalenderScheduler {
    private final KalenderRepository kalenderRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    public KalenderScheduler(KalenderRepository kalenderRepository) {
        this.kalenderRepository = kalenderRepository;
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        List<Termin> reminder = kalenderRepository.findAll();

        LocalDateTime jetzt = LocalDateTime.now();
        for(int i = 0; i < reminder.size(); i++){
            if(reminder.get(i).getReminderArt().equalsIgnoreCase("Minuten")) {
                if (reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getMinute() == jetzt.getMinute()
                        && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getHour() == jetzt.getHour()
                        && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getDayOfMonth() == jetzt.getDayOfMonth()
                        && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getMonth().equals(jetzt.getMonth())
                        && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getYear() == jetzt.getYear()) {
                    //Update Reminder Art to Kein
                }
            }else if(reminder.get(i).getReminderArt().equalsIgnoreCase("Stunden")){
                if (reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getHour() == jetzt.getHour()
                        && reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getDayOfMonth() == jetzt.getDayOfMonth()
                        && reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getMonth().equals(jetzt.getMonth())
                        && reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getYear() == jetzt.getYear()) {
                }
            }else if(reminder.get(i).getReminderArt().equalsIgnoreCase("Tage")){
                if (reminder.get(i).getVon().minusDays(reminder.get(i).getReminderValue()).getDayOfMonth() == jetzt.getDayOfMonth()
                        && reminder.get(i).getVon().minusDays(reminder.get(i).getReminderValue()).getMonth().equals(jetzt.getMonth())
                        && reminder.get(i).getVon().minusDays(reminder.get(i).getReminderValue()).getYear() == jetzt.getYear()) {

                }
            }
        }
    }
}
