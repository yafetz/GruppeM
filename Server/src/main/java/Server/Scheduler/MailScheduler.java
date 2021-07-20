package Server.Scheduler;

import Server.Modell.DatumUndUhrzeit;
import Server.Modell.ErgebnisVersendet;
import Server.Modell.Nutzer;
import Server.Repository.*;
import Server.Services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class MailScheduler {
    public Clock aktuell;
    //private final String

    private final LehrveranstaltungRepository lehrveranstaltungRepository;

    private final QuizRepository quizRepository;
    private final QuizBearbeitetRepository quizBearbeitetRepository;

    private final StudentRepository studentRepository;

    private final DatumUhrzeitRepository datumUhrzeitRepository;
    private final MailService mailService;
    private final ErgebnisVersendetRepository ergebnisVersendetRepository;


    @Autowired
    public MailScheduler(LehrveranstaltungRepository lehrveranstaltungRepository, QuizRepository quizRepository, QuizBearbeitetRepository quizBearbeitetRepository, StudentRepository studentRepository, DatumUhrzeitRepository datumUhrzeitRepository, MailService mailService, ErgebnisVersendetRepository ergebnisVersendetRepository) {
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.quizRepository = quizRepository;
        this.quizBearbeitetRepository = quizBearbeitetRepository;
        this.studentRepository = studentRepository;

        this.datumUhrzeitRepository = datumUhrzeitRepository;
        this.mailService = mailService;
        this.ergebnisVersendetRepository = ergebnisVersendetRepository;
    }


    @Scheduled(fixedRate = 5 * 1000)
    public void sendeBewertung() {
        LocalDateTime datum;
        List<DatumUndUhrzeit> listdatum = datumUhrzeitRepository.findAll();

        if (listdatum.isEmpty()) {
            datum = LocalDateTime.now();
        } else {
            datum = listdatum.get(0).getDatum();
        }


        if (datum.getMonthValue() == 9 && datum.getDayOfMonth() == 30) {

            sendMailPassedOrFailedSommer(datum);
        } else if (datum.getMonthValue() == 3 && datum.getDayOfMonth() == 30) {

            sendMailPassedOrFailedWinter(datum);
        } else {
            //passiert nichts
        }


    }


    public void sendMailPassedOrFailedSommer(LocalDateTime aktuelle) {
        //finde das aktuelle Datum heraus und speicher sie ab
        String jahr = String.valueOf(aktuelle.getYear());

        //Liste mit allen Kursen vom Sommersemester des Jahres in "jahr"
        List<Long> kurse_id = lehrveranstaltungRepository.getAllLehrveranstaltungBySemester("SoSe " + jahr);


        //Liste mit Anzahl an Quizze pro Lehrveranstaltung
        List<Integer> amountQuiz = new ArrayList<>();
        List<Long> neuKurse_id = new ArrayList<>();
        List<Integer> quiz = new ArrayList<>();
        for (int i = 0; i < kurse_id.size(); i++) {
            if (quizRepository.getAllAmountOfQuizOfCourse(kurse_id.get(i)) != 0) {

                amountQuiz.add(quizRepository.getAllAmountOfQuizOfCourse(kurse_id.get(i)));
                neuKurse_id.add(kurse_id.get(i));

            }
        }

        //Liste mit allen IDs von Studenten einer Lehrveranstaltung
        List<Nutzer> idlistvor = new ArrayList<>();


        for (int i = 0; i < neuKurse_id.size(); i++) {
            idlistvor = sortiereLehrendeRaus(studentRepository.getAllIDsFromStudentsFromACourse(neuKurse_id.get(i)));
            quiz = quizRepository.getAllIDsFromQuizFromACourse(neuKurse_id.get(i));


            for (int l = 0; l < idlistvor.size(); l++) {
                int counter = 0;
                for (int m = 0; m < quiz.size(); m++) {

                    if (quizBearbeitetRepository.getBestandenOderNicht(quiz.get(m), idlistvor.get(l).getId()) == null) {
                        counter = counter + 0;

                    } else if (quizBearbeitetRepository.getBestandenOderNicht(quiz.get(m), idlistvor.get(l).getId()) == true) {
                        counter++;

                    }


                }
                int bestehensGrenze;
                if(ergebnisVersendetRepository.findAllNutzerBySemesterCoursePassed(idlistvor.get(l).getId(), neuKurse_id.get(i),lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getSemester()).size()==0) {
                    if (amountQuiz.get(i) % 2 == 1) {
                        bestehensGrenze = (amountQuiz.get(i) / 2) + 1;

                        if (counter >= bestehensGrenze) {
                            mailService.sendEmail(idlistvor.get(l).getEmail(), "Sie haben den Kurs " + lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getTitel() + " bestanden");
                            ErgebnisVersendet ergebnisVersendet = new ErgebnisVersendet();
                            ergebnisVersendet.setNutzer(idlistvor.get(l));
                            ergebnisVersendet.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)));
                            ergebnisVersendet.setSemester(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getSemester());
                            ergebnisVersendet.setBestanden(true);
                            ergebnisVersendetRepository.save(ergebnisVersendet);
                        } else {
                            mailService.sendEmail(idlistvor.get(l).getEmail(), "Sie haben den Kurs " + lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getTitel() + " nicht bestanden");
                            ErgebnisVersendet ergebnisVersendet = new ErgebnisVersendet();
                            ergebnisVersendet.setNutzer(idlistvor.get(l));
                            ergebnisVersendet.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)));
                            ergebnisVersendet.setSemester(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getSemester());
                            ergebnisVersendet.setBestanden(false);
                            ergebnisVersendetRepository.save(ergebnisVersendet);
                        }


                    } else {
                        int bestehensgrenze = amountQuiz.get(i) / 2;
                        if (counter >= bestehensgrenze) {
                            mailService.sendEmail(idlistvor.get(l).getEmail(), "Sie haben den Kurs " + lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getTitel() + " bestanden");
                            ErgebnisVersendet ergebnisVersendet = new ErgebnisVersendet();
                            ergebnisVersendet.setNutzer(idlistvor.get(l));
                            ergebnisVersendet.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)));
                            ergebnisVersendet.setSemester(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getSemester());
                            ergebnisVersendet.setBestanden(true);
                            ergebnisVersendetRepository.save(ergebnisVersendet);
                        } else {
                            mailService.sendEmail(idlistvor.get(l).getEmail(), "Sie haben den Kurs " + lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getTitel() + " nicht bestanden");
                            ErgebnisVersendet ergebnisVersendet = new ErgebnisVersendet();
                            ergebnisVersendet.setNutzer(idlistvor.get(l));
                            ergebnisVersendet.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)));
                            ergebnisVersendet.setSemester(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getSemester());
                            ergebnisVersendet.setBestanden(false);
                            ergebnisVersendetRepository.save(ergebnisVersendet);
                        }

                    }
                }

            }


        }
    }

    public List<Nutzer> sortiereLehrendeRaus(List<Nutzer> listeMitLehrende) {
        List<Nutzer> neueListeOhneLehrende = new ArrayList<>();
        for (int i = 0; i < listeMitLehrende.size(); i++) {
            if (listeMitLehrende.get(i).getRolle().equals("Student")) {
                neueListeOhneLehrende.add(listeMitLehrende.get(i));
            }
        }
        return neueListeOhneLehrende;

    }


    public void sendMailPassedOrFailedWinter(LocalDateTime aktuelle) {
        //finde das aktuelle Datum heraus und speicher sie ab
        String jahr = String.valueOf(aktuelle.getYear());
        int jahrdavor = aktuelle.getYear() - 1;

        //Liste mit allen Kursen vom Sommersemester des Jahres in "jahr"
        List<Long> kurse_id = lehrveranstaltungRepository.getAllLehrveranstaltungBySemester("WiSe " + jahrdavor + "/" + jahr);

        //Liste mit Anzahl an Quizze pro Lehrveranstaltung
        List<Integer> amountQuiz = new ArrayList<>();
        List<Long> neuKurse_id = new ArrayList<>();
        List<Integer> quiz = new ArrayList<>();
        for (int i = 0; i < kurse_id.size(); i++) {
            if (quizRepository.getAllAmountOfQuizOfCourse(kurse_id.get(i)) != 0) {

                amountQuiz.add(quizRepository.getAllAmountOfQuizOfCourse(kurse_id.get(i)));
                neuKurse_id.add(kurse_id.get(i));

            }
        }

        //Liste mit allen IDs von Studenten einer Lehrveranstaltung
        List<Nutzer> idlistvor = new ArrayList<>();


        for (int i = 0; i < neuKurse_id.size(); i++) {
            idlistvor = sortiereLehrendeRaus(studentRepository.getAllIDsFromStudentsFromACourse(neuKurse_id.get(i)));
            quiz = quizRepository.getAllIDsFromQuizFromACourse(neuKurse_id.get(i));


            for (int l = 0; l < idlistvor.size(); l++) {
                int counter = 0;
                for (int m = 0; m < quiz.size(); m++) {

                    if (quizBearbeitetRepository.getBestandenOderNicht(quiz.get(m), idlistvor.get(l).getId()) == null) {
                        counter = counter + 0;

                    } else if (quizBearbeitetRepository.getBestandenOderNicht(quiz.get(m), idlistvor.get(l).getId()) == true) {
                        counter++;

                    }


                }
                int bestehensGrenze;
                if(ergebnisVersendetRepository.findAllNutzerBySemesterCoursePassed(idlistvor.get(l).getId(), neuKurse_id.get(i),lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getSemester()).size()==0) {
                    if (amountQuiz.get(i) % 2 == 1) {
                        bestehensGrenze = (amountQuiz.get(i) / 2) + 1;

                        if (counter >= bestehensGrenze) {
                            mailService.sendEmail(idlistvor.get(l).getEmail(), "Sie haben den Kurs " + lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getTitel() + " bestanden");
                            System.out.println("jemand hat im sommer bestanden");
                            ErgebnisVersendet ergebnisVersendet = new ErgebnisVersendet();
                            ergebnisVersendet.setNutzer(idlistvor.get(l));
                            ergebnisVersendet.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)));
                            ergebnisVersendet.setSemester(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getSemester());
                            ergebnisVersendet.setBestanden(true);
                            ergebnisVersendetRepository.save(ergebnisVersendet);
                        } else {
                            mailService.sendEmail(idlistvor.get(l).getEmail(), "Sie haben den Kurs " + lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getTitel() + " nicht bestanden");
                            System.out.println("jemand hat im sommer  nicht bestanden");
                            ErgebnisVersendet ergebnisVersendet = new ErgebnisVersendet();
                            ergebnisVersendet.setNutzer(idlistvor.get(l));
                            ergebnisVersendet.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)));
                            ergebnisVersendet.setSemester(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getSemester());
                            ergebnisVersendet.setBestanden(false);
                            ergebnisVersendetRepository.save(ergebnisVersendet);
                        }


                    } else {
                        int bestehensgrenze = amountQuiz.get(i) / 2;
                        if (counter >= bestehensgrenze) {
                            mailService.sendEmail(idlistvor.get(l).getEmail(), "Sie haben den Kurs " + lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getTitel() + " bestanden");
                            ErgebnisVersendet ergebnisVersendet = new ErgebnisVersendet();
                            ergebnisVersendet.setNutzer(idlistvor.get(l));
                            ergebnisVersendet.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)));
                            ergebnisVersendet.setSemester(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getSemester());
                            ergebnisVersendet.setBestanden(true);
                            ergebnisVersendetRepository.save(ergebnisVersendet);
                        } else {
                            mailService.sendEmail(idlistvor.get(l).getEmail(), "Sie haben den Kurs " + lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getTitel() + " nicht bestanden");
                            ErgebnisVersendet ergebnisVersendet = new ErgebnisVersendet();
                            ergebnisVersendet.setNutzer(idlistvor.get(l));
                            ergebnisVersendet.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)));
                            ergebnisVersendet.setSemester(lehrveranstaltungRepository.findLehrveranstaltungById(neuKurse_id.get(i)).getSemester());
                            ergebnisVersendet.setBestanden(false);
                            ergebnisVersendetRepository.save(ergebnisVersendet);
                        }

                    }
                }

            }


        }
    }
}
