# team3-pt-2022-23

## Υποστήριξη Ραδιοφωνικού Παραγωγού

Το σύστημα θα υποστηρίζει μια υπηρεσία διαδικτυακού μουσικού ραδιοφώνου.

[Εκφώνηση Εργασίας](docs/markdown/ekfonisi.md)

[Περιγραφή της λειτουργικότητας του λογισμικού](docs/markdown/leitourgies.md)

[Μοντέλο πεδίου](docs/images/uml_project.png)

[REST API Εργασίας](docs/markdown/rest-api-endpoints.md)

[Παραδοχές](docs/markdown/paradoxes.md)

[Επικοινωνίες microservices](docs/markdown/Functionality_Microservices.md)

[Sequence Diagram](docs/markdown/sequence_diagram_v1.png)


## Χρήσιμες εντολές

Η διαχείριση της οικοδόμησης του έργου μπορεί να γίνει με μια σειρά βασικών εντολών:
- ```mvn``` : εκτελεί τον προκαθορισμένο κύκλο οικοδόμησης
- ```mvn clean``` : διαγράφει τον φάκελο *target* στον οποίο το Maven δημιουργεί συνήθως το project.
- ```mvn install``` : δημιουργεί το project που περιγράφεται από το αρχείο Maven POM (pom.xml) και εγκαθιστά το προκύπτον artifact (JAR) στο τοπικό αποθετήριο Maven (*target* φάκελο)
- ```mvn test``` : εκτελεί τα unit tests του project
- ```mvn site``` : παράγει την τεκμηρίωση του project σε μορφή HTML. Τα παραγόμενα αρχεία είναι διαθέσιμα στην τοποθεσία *target/site/*
- ```mvn "-Dumlet.targetDir=src/site/markdown/uml" umlet:convert``` : παράγει αρχεία εικόνας png για όλα τα διαγράμματα που βρίσκονται στην τοποθεσία *src/site/markdown/uml*. 
- ```mvn quarkus:dev``` : εκκίνηση εφαρμογής σε development mode
- ```mvn quarkus:test``` : εκκίνηση εφαρμογής σε test mode