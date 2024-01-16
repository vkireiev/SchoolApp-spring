package ua.foxmided.foxstudent103852.schoolappspring.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ConsoleReader {
    private final BufferedReader reader;
    private final ConsoleWriter consoleWriter;

    @Autowired
    public ConsoleReader(@Lazy BufferedReader reader, ConsoleWriter consoleWriter) {
        this.reader = reader;
        this.consoleWriter = consoleWriter;
    }

    public void getResponseToReturnInMainMenu(String message) throws IOException {
        consoleWriter.printMessage(message);
        String response = reader.readLine();
    }

    public String readString(String message) throws IOException {
        String result = Constants.EMPTY_STRING;
        while (result.isEmpty()) {
            consoleWriter.printMessage(message);
            result = reader.readLine().trim();
        }
        return result;
    }

    public boolean readYesNo(String message) throws IOException {
        String result = Constants.EMPTY_STRING;
        Set<String> responsesYesNo = new HashSet<>(Arrays.asList(Constants.MENU_ITEM_Y_VALUE,
                Constants.MENU_ITEM_N_VALUE,
                Constants.MENU_ITEM_YES_VALUE,
                Constants.MENU_ITEM_NO_VALUE));
        Set<String> responsesYes = new HashSet<>(Arrays.asList(Constants.MENU_ITEM_Y_VALUE,
                Constants.MENU_ITEM_YES_VALUE));
        while (!responsesYesNo.contains(result)) {
            consoleWriter.printMessage(message);
            result = reader.readLine().trim().toLowerCase();
        }
        return responsesYes.contains(result);
    }

    public long readNumber(String message, String failMessage) throws IOException {
        long result;
        while (true) {
            try {
                consoleWriter.printMessage(message);
                result = Long.parseLong(reader.readLine().trim());
                return result;
            } catch (NumberFormatException e) {
                consoleWriter.printMessage(failMessage);
            }
        }
    }

    public long readNumberFromSet(String message, Set<Long> values, String failMessage, String correctResponseMessage)
            throws IOException {
        if (!values.isEmpty()) {
            while (true) {
                Long result = readNumber(message, failMessage);
                if (values.contains(result)) {
                    return result;
                } else {
                    consoleWriter.printMessage(String.format(correctResponseMessage, values));
                }
            }
        }
        return readNumber(message, failMessage);
    }
}
