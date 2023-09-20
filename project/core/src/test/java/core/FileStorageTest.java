package core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileStorageTest {

    @Test
    public void testFileStorage() {
        FileStorage first = new FileStorage();
        first.setTextFile("Yo!");
        FileStorage second = new FileStorage();
        Assertions.assertEquals("Yo!", second.getTextFromFile());
    }

    
}
