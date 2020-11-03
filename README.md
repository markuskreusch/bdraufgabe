# README

Der Code is in zwei Komponenten den REST-Service im Verzeichnis service und das ui im Verzeichnis ui aufgeteilt.

## Service

Um den Service zu Starten entweder `gradle bootRun` ausführen oder über eine IDE der Wahl `Application.kt`. Als Datenbank wird h2 verwendet und unter data/h2.mv.db angelegt. Der Service startet unter `http://localhost:8080/`.

## UI

Das UI ist in Angular implementiert. Daher per `npm install` die Abhängikeiten laden und mit `ng serve --open` starten. Als Backend ist `http://localhost:8080/` in `book.service.ts` fest verdrahtet; Bei Bedarf dort ändern. CORS ist im service so konfiguriert, dass das UI unter `http://locahlost:4200/` darauf zugreifen kann; Bei Bedarf in `Application.kt` ändern.