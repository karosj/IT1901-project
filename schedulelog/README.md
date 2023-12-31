# ScheduleLog
This app is designed to provide an overview of your school work. You add the activities including subject you've been working on, and a brief note about your study session, and how many hours you worked. The app adds a timestamp, date and time, to the activity.

# Screenshot from use
![](/assets/release3png/step1.png) 
![](/assets/release3png/step2.png)
![](/assets/release3png/step3.png)

## User Story
I'm a student wondering how much I actually spend on different subjects during the week to plan next week better. Every time after I've worked on a subject, I go into the app and log the activity. Today, I worked on a TDT4160 task. I choose the subject from the selection menu, and enter use the calendar date selections to add the date 11/14/2023 for both start and end, and start time 12, end time 15. Then I write the description "Øving" and click the add activity button. It then appears in list of entries.

## Sequence diagram
The sequence diagram illustrates how a student interacts with a system named "ScheduleLog." The student fills out a logging form, saves the log, and then views previous logs. The "ScheduleLog" system stores and retrieves the necessary information for logging and displaying previous activities. The sequence diagram is located in the same folder with the name "architecture.puml," which can be opened in a preview to convert the code into a diagram.

## Requirements to run
- Java 17.0.8
- JavaFX 17.0.8
- Maven 3.9.4

## Choosing document metaphor vs implicit saving

Choosing between the document metaphor (often associated with desktop apps) and implcit saving(typical for mobile and modern web apps) is an important decision. 

Desktop apps offers
- concepts like "save", "save as" and "open", which many users are accustomed to from earlier generations of software
- user control over when changes are saved, and where they are saved
- feedback when changes are unsaved

However, 
- it might require more actions from the user to save changes
- users might forget to save and potentially lose their work
- new or younger users who have mainly been using mobile or other modern devices might find this less intuitive

Onto mobile apps:
- automatic saving removes the need for the user to remember to save manually
- reduces risk/less chance of users accidentally lose the data
- many users nowadays expect that apps save data for them automatically

On the other hand:
- some users might feel that they lose some degree of control over their data
- without clear feedback, some users might be unsure if their changes have been saved, and therefore it might create confusion
- errors might become permanent. If the user makes a mistake and the system saves automatically, it could be difficult to undo the specific error

In conclusion, a mobile app would suit our idea regarding ScheduleLog. The app is designed for students. We assume that a student would rather use this application on their phone than a desktop. 

To reduce the amount of disadvantages having a mobile app, we will make sure that the feedback is clear. In addition, to improve the error part, we will take the testing part seriously. 
