package Server.Scheduler;

import Server.Modell.DatumUndUhrzeit;
import Server.Modell.Termin;
import Server.Repository.*;
import Server.Services.KalenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
public class KalenderScheduler {
    private final KalenderRepository kalenderRepository;
    private final KalenderService kalenderService;
    private final DatumUhrzeitRepository datumUhrzeitRepository;
    List<Termin> reminder;

    @Autowired
    public KalenderScheduler(KalenderRepository kalenderRepository, KalenderService kalenderService, DatumUhrzeitRepository datumUhrzeitRepository) {
        this.kalenderRepository = kalenderRepository;
        this.kalenderService = kalenderService;
        reminder = kalenderRepository.findAll();

        this.datumUhrzeitRepository = datumUhrzeitRepository;
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {

        List<DatumUndUhrzeit> listdatum = datumUhrzeitRepository.findAll();
        LocalDateTime jetzt;
        if(!listdatum.isEmpty()) {
            LocalDateTime datum = listdatum.get(0).getDatum();
            reminder = kalenderRepository.findAll();
            jetzt = datum;
        }
        else {
            jetzt = LocalDateTime.now();
        }

        for(int i = 0; i < reminder.size(); i++) {
            if (reminder.get(i).getReminderShow().equals("Email")) {
                if (reminder.get(i).getReminderArt().equalsIgnoreCase("Minuten")) {
                    //System.out.println("Index: "+ i + " Reminder Minuten: "+ reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getMinute() + " jetzige Minuten: "+ jetzt.getMinute());
                    if (reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getMinute() == jetzt.getMinute()
                            && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getHour() == jetzt.getHour()
                            && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getDayOfMonth() == jetzt.getDayOfMonth()
                            && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getMonth().equals(jetzt.getMonth())
                            && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getYear() == jetzt.getYear()) {
                        //sendMail
                        kalenderService.sendEmail(reminder.get(i).getNutzerId().getEmail(), "Sie haben einen Termin in " + reminder.get(i).getReminderValue() + " " + reminder.get(i).getReminderArt() + " \n : " + reminder.get(i).getTitel());
                        //Update Reminder Art to Kein
                        Termin t = reminder.get(i);
                        t.setReminderArt("Kein");
                    }
                } else if (reminder.get(i).getReminderArt().equalsIgnoreCase("Stunden")) {
                    if (reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getHour() == jetzt.getHour()
                            && reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getDayOfMonth() == jetzt.getDayOfMonth()
                            && reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getMonth().equals(jetzt.getMonth())
                            && reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getYear() == jetzt.getYear()) {
                        //sendMail
                        kalenderService.sendEmail(reminder.get(i).getNutzerId().getEmail(), "Sie haben einen Termin in " + reminder.get(i).getReminderValue() + " " + reminder.get(i).getReminderArt() + " \n : " + reminder.get(i).getTitel());
                        //Update Reminder Art to Kein
                        Termin t = reminder.get(i);
                        t.setReminderArt("Kein");
                    }
                } else if (reminder.get(i).getReminderArt().equalsIgnoreCase("Tage")) {
                    if (reminder.get(i).getVon().minusDays(reminder.get(i).getReminderValue()).getDayOfMonth() == jetzt.getDayOfMonth()
                            && reminder.get(i).getVon().minusDays(reminder.get(i).getReminderValue()).getMonth().equals(jetzt.getMonth())
                            && reminder.get(i).getVon().minusDays(reminder.get(i).getReminderValue()).getYear() == jetzt.getYear()) {
                        //sendMail
                        kalenderService.sendEmail(reminder.get(i).getNutzerId().getEmail(), "Sie haben einen Termin in " + reminder.get(i).getReminderValue() + " " + reminder.get(i).getReminderArt() + " \n : " + reminder.get(i).getTitel());
                        //Update Reminder Art to Kein
                        Termin t = reminder.get(i);
                        t.setReminderArt("Kein");
                    }
                }
            }
        }
    }








}
