package ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import schedulelog.core.Activity;
import schedulelog.core.Courses;
import schedulelog.core.Subject;
import schedulelog.json.RestConsumer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import javafx.application.Platform;



/**
 * TestFX App test
 */
public class AppTest extends ApplicationTest {

    private AppController controller;
    private App app;
    private Parent root;
    private RestConsumer mockRestConsumer;

    private List<Activity> mockActivities = Arrays.asList(
        new Activity(Collections.singletonList(new Subject("TDT4120", new Courses())), 
            LocalDateTime.now(), 
            LocalDateTime.now().plusHours(1), 
            "Study AppTest session")
    );

    @Override
    public void start(Stage stage) throws IOException {
        // Create and configure the mock RestConsumer
        mockRestConsumer = mock(RestConsumer.class);
        when(mockRestConsumer.getActivities()).thenReturn(mockActivities);
        when(mockRestConsumer.addActivity(any(Activity.class))).thenReturn("Mocked server response");

        // Load the FXML and set the controller
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("App.fxml"));
        root = fxmlLoader.load();
        controller = fxmlLoader.getController();

        // Set the mocked RestConsumer in the controller
        controller.setRestConsumer(mockRestConsumer);

        // Now initialize the controller
        controller.initialize();

        // Set up the stage
        stage.setScene(new Scene(root));
        stage.show();
        app = new App();
    }

    public Parent getRootNode() {
        return root;
    }

    @Test
    public void testControllerInitial() {
        assertNotNull(this.controller);
    }

    @Test
    public void testAppInitial() {
        assertNotNull(this.app);
    }

    @Test
    public void testInitialActivitiesList() {
        TableView<Activity> activitiesTableView = lookup("#activitiesTableView").query();
        assertNotNull(activitiesTableView);
        assert activitiesTableView.getItems().size() == 1;
    }

    @Test
    public void testNoConnectedServer() {
        System.out.println("testNoConnectedServer start.");

        // Mock the RestConsumer to return null for getActivities
        when(mockRestConsumer.getActivities()).thenReturn(null);

        Platform.runLater(() -> {
            // Initialize the controller with the mocked RestConsumer
            controller.setRestConsumer(mockRestConsumer);
            controller.initialize();
        });

        WaitForAsyncUtils.waitForFxEvents();
            
        // Assert that the activities list is empty or null
        verifyThat(".alert .content", hasText("An error occurred while retrieving the activities."));

        // Reset the mock to its original state
        when(mockRestConsumer.getActivities()).thenReturn(mockActivities);
        
    }
    
    @Test
    public void testAddActivityClick() {
        // Assuming you have a button with fx:id "addActivityButton" in your FXML
        String addActivityButtonId = "#addActivityButton";

        // Simulate user input
        clickOn("#descriptionInput").write("New test Session");
        clickOn("#startDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#startTimeInput").write("10:00");
        clickOn("#endDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#endTimeInput").write("12:00");

        // Assuming you have a ListView for subjects with fx:id "subjectSelector"
        // Select a subject from the list
        clickOn("#subjectSelector");
        type(KeyCode.DOWN); // Navigate in the list
        type(KeyCode.ENTER); // Select an item

        // Click the button to add the activity
        clickOn(addActivityButtonId);

        TableView<Activity> activitiesTableView = lookup("#activitiesTableView").query();
        ObservableList<Activity> activities = activitiesTableView.getItems(); 
        assertNotNull(activities);
        
        // Because the getActivites is mocked, the new activity will not be added to the list
        // But the fact that no error is shown means that the activity was added successfully

        // And that the inputs are cleared:
        assertEquals("", lookup("#descriptionInput").queryTextInputControl().getText());
    }

    @Test
    public void testBadActivity() {
        // Assuming you have a button with fx:id "addActivityButton" in your FXML
        String addActivityButtonId = "#addActivityButton";

        // Simulate user input
        clickOn("#startDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#startTimeInput").write("10:00");
        clickOn("#endDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#endTimeInput").write("12:00");

        // Click the button to add the activity
        clickOn(addActivityButtonId);
        
        verifyThat(".alert .content", hasText("Please check the activity details and try again."));
        assertEquals("10:00", lookup("#startTimeInput").queryTextInputControl().getText());
    }

    @Test
    public void testStrangeDates() {
        // Assuming you have a button with fx:id "addActivityButton" in your FXML
        String addActivityButtonId = "#addActivityButton";

        // Simulate user input
        clickOn("#descriptionInput").write("Strange dates");
        clickOn("#startDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#startTimeInput").write("10:00");
        clickOn("#endDateInput").write("10/25/2023");
        type(KeyCode.ENTER);
        clickOn("#endTimeInput").write("12:00");

        // Assuming you have a ListView for subjects with fx:id "subjectSelector"
        // Select a subject from the list
        clickOn("#subjectSelector");
        type(KeyCode.DOWN); // Navigate in the list
        type(KeyCode.ENTER); // Select an item

        // Click the button to add the activity
        clickOn(addActivityButtonId);

        verifyThat(".alert .content", hasText("Start time cannot be after end time."));
        assertEquals("Strange dates", lookup("#descriptionInput").queryTextInputControl().getText());
    }

    @Test
    public void testWrongTimeFormat() {
        // Assuming you have a button with fx:id "addActivityButton" in your FXML
        String addActivityButtonId = "#addActivityButton";

        // Simulate user input
        clickOn("#descriptionInput").write("Strange times");
        clickOn("#startDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#startTimeInput").write("wrong");
        clickOn("#endDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#endTimeInput").write("wrong");

        // Assuming you have a ListView for subjects with fx:id "subjectSelector"
        // Select a subject from the list
        clickOn("#subjectSelector");
        type(KeyCode.DOWN); // Navigate in the list
        type(KeyCode.ENTER); // Select an item

        // Click the button to add the activity
        clickOn(addActivityButtonId);

        verifyThat(".alert .content", hasText("Please enter the time in HH:mm format."));
        assertEquals("Strange times", lookup("#descriptionInput").queryTextInputControl().getText());
    }

    @Test
    public void testNoSubjects() {
        // Assuming you have a button with fx:id "addActivityButton" in your FXML
        String addActivityButtonId = "#addActivityButton";

        // Simulate user input
        clickOn("#descriptionInput").write("No subjects");
        clickOn("#startDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#startTimeInput").write("10:00");
        clickOn("#endDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#endTimeInput").write("12:00");

        // Click the button to add the activity
        clickOn(addActivityButtonId);
        
        verifyThat(".alert .content", hasText("An error occurred: List of subjects cannot be null or empty."));
        assertEquals("No subjects", lookup("#descriptionInput").queryTextInputControl().getText());
    }

}
