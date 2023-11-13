package schedulelog.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import schedulelog.core.Activity;
import schedulelog.core.Subject;
import schedulelog.json.FileStorage;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class Controller {

  private FileStorage fileStorage;

  public Controller() {
    this.fileStorage = new FileStorage();
  }

  // Use FileStorage to run getActivitiesJSON, which returns an activities json
  @GetMapping("/activities")
  public String getActivities() {
    return this.fileStorage.getActivitiesJSON();
  }

  @PostMapping("/addActivity")
  public ResponseEntity<String> addActivity(@RequestBody Activity activity) {
    StringBuilder errorMessage = new StringBuilder();

    // Validate fields of the Activity object
    if (activity.getSubjects() == null || activity.getSubjects().isEmpty()) {
      errorMessage.append("Subjects are missing. ");
    } else {
      for (Subject subject : activity.getSubjects()) {
        if (subject.getCode() == null || subject.getCode().trim().isEmpty()) {
          errorMessage.append("One or more subjects have a missing or empty code. ");
          break; // Exit the loop once you find an invalid subject to avoid redundant checks
        }
        subject.resetName();
        if (subject.getName() == null) {
          errorMessage.append("One or more subjects have an invalid code. ");
          break; // Exit the loop once you find an invalid subject to avoid redundant checks
        }
      }
    }

    if (activity.getStartTime() == null) {
      errorMessage.append("Start time is missing. ");
    }
    if (activity.getEndTime() == null) {
      errorMessage.append("End time is missing. ");
    }
    if (activity.getDescription() == null || activity.getDescription().trim().isEmpty()) {
      errorMessage.append("Description is missing. ");
    }

    // If there are any validation errors, return them
    if (errorMessage.length() > 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
    }

    // If validation passed, try to add the activity
    try {
      fileStorage.addActivity(activity);
      return ResponseEntity.ok("Activity added successfully");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding activity");
    }
  }
}