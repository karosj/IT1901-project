# todo-list-prosjektet

Dette prosjektet er et utviklingsprosjekt tilsvarende det en skal gjennom i IT1901. Det er ment å være til et nyttig eksempel på flere måter:
- Det viser hvordan prosjektet kan deles opp i mange fokuserte og håndterbare trinn
- Prosjektoppsett og kodingsteknikker
- Det er utgangspunktet for en videoserie som også viser hvordan det jobbes praktisk med utviklingsverktøy

Ikke alt vi gjør blir tatt opp på video, noe arbeid vil bli gjort som forarbeid eller mellom episodene, videoene skal gi en oversikt over alt arbeidet. 

## Plan over arbeidet/episodene

Her følger oversikt over arbeidet/episodene. Først er dette en plan, men etterhvert som arbeidet blir utført så blir det en oversikt over faktisk gjennomfør arbeid.

### Gitpodifisering av repoet

Planen er å primært bruke gitpod til utvikling (selv om det kanskje ville vært mest praktisk for meg å bare bruke Eclipse).
Første trinn blir å "gitpodifisere" repoet, dvs. gjøre at gitpod kan startes opp rett fra repo-sida på gitlab.

### Oppsett av første trinn av utviklingsprosjektet

I første omgang skal vi sette opp et enkelt JavaFX-prosjekt med maven som bygge-system. Vi setter det opp fra scratch, vha. en enkel mal for
maven-prosjekter og så justerer vi litt på det ved å kopiere elementer fra malen som ligger i *javafx-maven*-grenen i
[gitpod-templates-repoet](https://gitlab.stud.idi.ntnu.no/it1901/gitpod-templates). Jeg gjør det på denne måten, fordi det da er enklere
å forklare hvert element i pom.xml-fila, som inneholder oppsettet. Merk at prosjektoppsettet er enklere enn det vil måtte bli, men
vi gjør det sånn for å komme raskere i gang. Så bygger vi ut etterhvert og omstrukturerer når det trengs.

### Vår første utviklingsoppgave: Et enkelt API for todo-lister m/tester

### Et enkelt JavaFX-GUI for todo-lista
