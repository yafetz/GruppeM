Anleitung zum Set up:
1. Xampp herunterladen
2. Projekt bevorzugt mit IntelliJ clonen
3. unter dem Ordner Datenbank die Datei sep.sql , auf den Desktop schiebn.
4. Xampp starten und dort auf apache und mysql auf starten drücken.
5. im Browser localhost/phpmyadmin eingeben 
6. dort die Datenbank importieren. Falls es nicht so einfach geht , dann neue Datenbank erstellen mit dem Namen sep und dorr deaufdrücken und dann sieht man importieren und dort vom desktop die datei sep.sql auswählen
7. in intellij , run configuration gehen dort ein neue Application hinzufügen dort einmal als Namen Seever eingeben mit dem Sdk java 16 und man class als Server.SEP_Server dann auf ok drücken.
8. nochmal auf edit configuration gehen und dann dort als Namen Client eingeben und als Main Class Client.Launch
