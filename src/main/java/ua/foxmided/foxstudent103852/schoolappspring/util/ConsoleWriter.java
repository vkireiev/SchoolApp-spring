package ua.foxmided.foxstudent103852.schoolappspring.util;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.stereotype.Component;

@Component
public class ConsoleWriter {
    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    public void printMessage(String message) {
        System.out.println(message);
    }
}
