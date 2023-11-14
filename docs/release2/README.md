# "ScheduleLog" release 2

For this exercise/release, we have reviewed the plan posted on Blackboard.

* Paragraph related to work habits, workflow, and code quality
    - We mostly work in groups, meeting to discuss strategies, plans, and code together. We review each other's code and 'approve' it on GitLab. All changes are linked to 'issues,' and each 'issue' is assigned its own 'branch' to keep it organized for everyone. Each "issue" creates a draft for a merge request, which is later merged into the master branch once the "issue" is resolved. This is then linked to a 'Milestone,' which is seen as the goal for a submission. We actively use 'Co-Authored by...' to make collaboration easier. The workflow is good, and every time we encounter a common problem, it is quickly resolved with everyone contributing to the solution. We have a system on GitLab called "issue boards" that helps us track the status of each problem. We use labels such as "open," "in progress," "to verify," and "closed." However, this system hasn't been utilized as actively due to our frequent in-person collaboration.
    
* We have focused on updating/adding JUnit tests for the 'Activity' and 'Subject' classes, aiming to achieve as high test coverage as possible. We've used JSON with the Jackson library. The main reason for choosing Jackson is that one of the group members had prior experience with it. The advantages are numerous, but we found that Jackson offers straightforward integration, making it easy to implement in Java applications. It's a robust library with ample online resources and documentation, which has been very helpful. Another benefit is that Jackson supports Java types, including classes and object structures.

* Another aspect we worked on is Eclipse Che, which was set up and documented in the README. We added a 'devfile.yaml' that allows for opening it in Eclipse Che. Checkstyle and SpotBugs were added with two XML files, but currently, only one of the files is actively used (SpotBugs is in use, while Checkstyle has built-in tests). We plan to retain Checkstyle XML file for now, but we may consider removing it in the future if necessary.

* In terms of modularization, based on the folder structure, we have two modules: 'ui' and 'core.' Within 'core,' we have the 'json' module, which focuses on the program's functionality. 'Json' currently includes a 'FileStorage.' On the other hand, 'ui' is responsible for the application's user interface.

* Additionally, the sequence diagram explains how the application currently functions, but it will be updated when significant changes are made in the future. Furthermore, we've updated the documentation in the form of various README files.
