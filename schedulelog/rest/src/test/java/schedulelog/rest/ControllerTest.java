package schedulelog.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import schedulelog.core.Activity;
import schedulelog.core.Courses;
import schedulelog.core.Subject;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

@AutoConfigureMockMvc
@ContextConfiguration(classes = { Controller.class })
@WebMvcTest
public class ControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() throws Exception {
    objectMapper = getConfiguredMapper();
  }

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

  @Test
public void testAddActivity() throws Exception {
    // Create a sample Activity object
    Subject subject = new Subject("TDT4120", new Courses());
    LocalDateTime startTime = LocalDateTime.of(2023, 10, 31, 12, 0);
    LocalDateTime endTime = LocalDateTime.of(2023, 10, 31, 13, 0);
    Activity activity = new Activity(Arrays.asList(subject), startTime, endTime, "Øving 1");

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


  private ObjectMapper getConfiguredMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
