# ScheduleLog

## Eclipse Che
https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2319/gr2319/

## Contents
The code for ScheduleLog app is located in the schedulelog folder.

The logic for the app ScheduleLog is located in the core folder within the schedulelog folder. It consists of a core and json folders.

The code for the UI of the app is located in the ui folder

```
.:
├───assets
├───docs
│   ├───release1
│   └───release2
└───schedulelog
    ├───config
    │   ├───checkstyle
    │   ├───eclipse
    │   └───spotbugs
    ├───core
    │   ├───src
    │   │   ├───main
    │   │   │   └───java
    │   │   │       └───schedulelog
    │   │   │           ├───core
    │   │   │           └───json
    │   │   └───test
    │   │       └───java
    │   │   │       └───schedulelog
    │   │   │           ├───core
    │   │   │           └───json
    │   └───target
    └───ui
        ├───src
        │   ├───main
        │   │   ├───java
        │   │   │   └───ui
        │   │   └───resources
        │   │       └───ui
        │   └───test
        │       └───java
        │           └───ui
        └───target
```

## Requirements to run
- Java 17.0.8

- JavaFX 17.0.8

- Maven 3.9.4

## How to run the application
### Install
1. Open terminal.
2. ```cd schedulelog```
3. ```mvn clean install```

### Test application: 
```
mvn test
mvn spotbugs:check
mvn checkstyle:check
```

### Run application: 
1. ```cd ui```
2. ```mvn javafx:run```







