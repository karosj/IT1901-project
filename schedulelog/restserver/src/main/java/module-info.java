module schedulelog.rest {
    exports schedulelog.rest; // Exports the main package of the rest module

    // Add any required dependencies for the rest module
    requires spring.boot; // Assuming you're using Spring Boot
    requires spring.boot.autoconfigure; // Assuming you're using Spring Boot auto-configuration
    requires spring.web; // For Spring Web functionalities

}
