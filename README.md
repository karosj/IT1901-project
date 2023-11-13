# ScheduleLog

## Eclipse Che
https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2319/gr2319/

## Contents
The code for ScheduleLog app is located in the schedulelog folder.

The logic for the app ScheduleLog is located in the core folder within the schedulelog folder. It consists of a core and json folders.

The code for the UI of the app is located in the ui folder

```
Root:.
├───assets
│   └───release1png
├───docs
│   ├───diagrams
│   ├───release1
│   └───release2
└───schedulelog
    ├───.vscode
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
    │   │           ├───core
    │   │           └───schedulelog
    │   │               ├───core
    │   │               └───json
    │   └───target
    │       ├───classes
    │       │   └───schedulelog
    │       │       ├───core
    │       │       └───json
    │       ├───generated-sources
    │       │   └───annotations
    │       ├───generated-test-sources
    │       │   └───test-annotations
    │       ├───maven-status
    │       │   └───maven-compiler-plugin
    │       │       ├───compile
    │       │       │   └───default-compile
    │       │       └───testCompile
    │       │           └───default-testCompile
    │       ├───surefire-reports
    │       └───test-classes
    │           ├───META-INF
    │           └───schedulelog
    │               ├───core
    │               └───json
    ├───rest
    │   ├───src
    │   │   ├───main
    │   │   │   └───java
    │   │   │       └───schedulelog
    │   │   │           └───rest
    │   │   └───test
    │   │       ├───java
    │   │       │   └───schedulelog
    │   │       │       ├───rest
    │   │       │       └───ui
    │   │       └───resources
    │   └───target
    │       ├───classes
    │       │   └───schedulelog
    │       │       └───rest
    │       ├───generated-sources
    │       │   └───annotations
    │       ├───generated-test-sources
    │       │   └───test-annotations
    │       ├───maven-archiver
    │       ├───maven-status
    │       │   └───maven-compiler-plugin
    │       │       ├───compile
    │       │       │   └───default-compile
    │       │       └───testCompile
    │       │           └───default-testCompile
    │       ├───site
    │       │   └───jacoco
    │       │       ├───jacoco-resources
    │       │       └───schedulelog.rest
    │       ├───surefire-reports
    │       └───test-classes
    │           ├───META-INF
    │           └───schedulelog
    │               └───rest
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
            ├───classes
            │   └───ui
            ├───generated-sources
            │   └───annotations
            ├───generated-test-sources
            │   └───test-annotations
            ├───maven-status
            │   └───maven-compiler-plugin
            │       ├───compile
            │       │   └───default-compile
            │       └───testCompile
            │           └───default-testCompile
            └───test-classes
                └───META-INF
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







