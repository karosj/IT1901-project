module todolist.springboot.rest {
    requires com.fasterxml.jackson.databind;

    requires spring.web;
    requires spring.beans;
    requires spring.boot;
    requires spring.context;
    requires spring.boot.autoconfigure;

    requires todolist.core;

    opens todolist.springboot.restserver to spring.beans, spring.context, spring.web;
}
