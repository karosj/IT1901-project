package schedulelog.rest;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import schedulelog.core.Activity;
import schedulelog.core.Courses;
import schedulelog.core.Subject;
/**
 * Test class for the Rest Controller.
 *
 * This class contains tests for the Controller class, focusing on its REST API endpoints.
 * It uses MockMvc for testing Spring MVC controllers without starting a full HTTP server.
 */
@AutoConfigureMockMvc
@ContextConfiguration(classes = { Controller.class })
@WebMvcTest
public class ControllerTest {

  private static String originalContent;
  private static final Path FILE_PATH = Paths.get("activities.json");

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;
  // Reads the original content of the activities file before all tests.
  @BeforeEach
  public void setup() throws Exception {
    objectMapper = getConfiguredMapper();
  }
  // Restores the original content of the activities file after all tests.
  @BeforeAll
    public static void setUpBeforeClass() throws Exception {
      // Check if the file exists and read its content
      if (Files.exists(FILE_PATH)) {
          originalContent = Files.readString(FILE_PATH);
      }
    }
  // Sets up necessary objects before each test.
  @AfterAll
  public static void tearDownAfterClass() throws Exception {
    // Restore the original content of the file
    if (originalContent != null) {
        Files.writeString(FILE_PATH, originalContent);
    }
  }
  // Tests the GET request to fetch activities.
  @Test
  public void testGetActivities() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/activities")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn();
    try {
      List<Activity> activities = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Activity>>() {});

      System.out.println("activities:");
      System.out.println(activities);

      assertNotNull(activities);

    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    }
  }
  // Tests adding a new activity with a POST request.
  @Test
  public void testAddActivity() throws Exception {
    // Create a sample Activity object
    Subject subject = new Subject("TDT4120", new Courses());
    LocalDateTime startTime = LocalDateTime.of(2023, 10, 31, 12, 0);
    LocalDateTime endTime = LocalDateTime.of(2023, 10, 31, 13, 0);
    Activity activity = new Activity(Arrays.asList(subject), startTime, endTime, "Test Øving 1");

    // Convert the Activity object to JSON string
    String activityJson = objectMapper.writeValueAsString(activity);

    // Perform POST request
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addActivity")
      .contentType(MediaType.APPLICATION_JSON)
      .content(activityJson))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn();

    // Assert the response
    String response = result.getResponse().getContentAsString();

    assertEquals("Activity added successfully", response);
  }
  // Tests adding an activity with empty subjects list, expecting a BAD_REQUEST response.
  @Test
  public void testAddActivityNoSubjects() throws Exception {

    String activityJson = "{\n" + //
        "    \"description\": \"ggdssdwdG\",\n" + //
        "    \"subjects\": [],\n" + //
        "    \"startTime\": \"2010-02-22T20:10\",\n" + //
        "    \"endTime\": \"2010-02-22T20:50\"\n" + //
        "}";

    // Perform POST request
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addActivity")
      .contentType(MediaType.APPLICATION_JSON)
      .content(activityJson))
      .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting BAD_REQUEST status
      .andReturn();

    // Assert the response
    String response = result.getResponse().getContentAsString();

    assertEquals("Subjects are missing. ", response);
  }
  //  Tests adding an activity with invalid subject codes, expecting a BAD_REQUEST response.
  @Test
  public void testAddActivityWrongSubjects() throws Exception {

    String activityJson = "{\n" + //
      "    \"description\": \"ggdssdwdG\",\n" + //
      "    \"subjects\": [\n" + //
      "        {\n" + //
      "            \"code\": \"Lol\"\n" + //
      "        }\n" + //
      "    ],\n" + //
      "    \"startTime\": \"2010-02-22T20:10\",\n" + //
      "    \"endTime\": \"2010-02-22T20:50\"\n" + //
      "}";

    // Perform POST request
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addActivity")
      .contentType(MediaType.APPLICATION_JSON)
      .content(activityJson))
      .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting BAD_REQUEST status
      .andReturn();

    // Assert the response
    String response = result.getResponse().getContentAsString();

    assertEquals("One or more subjects have an invalid code. ", response);
  }
  // Tests adding an activity with no subject code provided, expecting a BAD_REQUEST response.
  @Test
  public void testAddActivityNoSubjectCode() throws Exception {

    String activityJson = "{\n" + //
      "    \"description\": \"ggdssdwdG\",\n" + //
      "    \"subjects\": [\n" + //
      "        {}\n" + //
      "    ],\n" + //
      "    \"startTime\": \"2010-02-22T20:10\",\n" + //
      "    \"endTime\": \"2010-02-22T20:50\"\n" + //
      "}";

    // Perform POST request
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addActivity")
      .contentType(MediaType.APPLICATION_JSON)
      .content(activityJson))
      .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting BAD_REQUEST status
      .andReturn();

    // Assert the response
    String response = result.getResponse().getContentAsString();

    assertEquals("One or more subjects have a missing or empty code. ", response);
  }
  // Tests adding an activity without a start time, expecting a BAD_REQUEST response.
  @Test
  public void testAddActivityNoStartTime() throws Exception {

    String activityJson = "{\n" + //
      "    \"description\": \"ggdssdwdG\",\n" + //
      "    \"subjects\": [\n" + //
      "        {\n" + //
      "            \"code\": \"TDT4120\"\n" + //
      "        }\n" + //
      "    ],\n" + //
      "    \"endTime\": \"2010-02-22T20:50\"\n" + //
      "}";

    // Perform POST request
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addActivity")
      .contentType(MediaType.APPLICATION_JSON)
      .content(activityJson))
      .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting BAD_REQUEST status
      .andReturn();

    // Assert the response
    String response = result.getResponse().getContentAsString();

    assertEquals("Start time is missing. ", response);
  }
  // Tests adding an activity without an end time, expecting a BAD_REQUEST response.
  @Test
  public void testAddActivityNoEndTime() throws Exception {

    String activityJson = "{\n" + //
      "    \"description\": \"ggdssdwdG\",\n" + //
      "    \"subjects\": [\n" + //
      "        {\n" + //
      "            \"code\": \"TDT4120\"\n" + //
      "        }\n" + //
      "    ],\n" + //
      "    \"startTime\": \"2010-02-22T20:50\"\n" + //
      "}";

    // Perform POST request
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addActivity")
      .contentType(MediaType.APPLICATION_JSON)
      .content(activityJson))
      .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting BAD_REQUEST status
      .andReturn();

    // Assert the response
    String response = result.getResponse().getContentAsString();

    assertEquals("End time is missing. ", response);
  }
  // Tests adding an activity without a description, expecting a BAD_REQUEST response.
  @Test
  public void testAddActivityNoDesc() throws Exception {

    String activityJson = "{\n" + //
      "    \"subjects\": [\n" + //
      "        {\n" + //
      "            \"code\": \"TDT4160\",\n" + //
      "            \"name\": \"TDT4160: Datamaskiner og digitalteknikk\"\n" + //
      "        },\n" + //
      "        {\n" + //
      "            \"code\": \"TMA4240\",\n" + //
      "            \"name\": \"TMA4240: Statistikk\"\n" + //
      "        }\n" + //
      "    ],\n" + //
      "    \"startTime\": \"2010-02-22T20:10\",\n" + //
      "    \"endTime\": \"2010-02-22T20:50\"\n" + //
      "}";

    // Perform POST request
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addActivity")
      .contentType(MediaType.APPLICATION_JSON)
      .content(activityJson))
      .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting BAD_REQUEST status
      .andReturn();

    // Assert the response
    String response = result.getResponse().getContentAsString();

    assertEquals("Description is missing. ", response);
  }
  //
  private ObjectMapper getConfiguredMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }
}
