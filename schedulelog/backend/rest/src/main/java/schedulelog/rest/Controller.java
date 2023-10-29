package schedulelog.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import schedulelog.core.Courses;

@RestController
public class Controller {
    @GetMapping("/getActivities")
    public String sayHello() {
        Courses courses = new Courses();
        return courses.toString();
    }
}
