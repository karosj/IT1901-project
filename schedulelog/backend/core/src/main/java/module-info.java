module schedulelog.core {
    exports schedulelog.core;
    exports schedulelog.json;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens schedulelog.core to com.jackson.databind;
}
