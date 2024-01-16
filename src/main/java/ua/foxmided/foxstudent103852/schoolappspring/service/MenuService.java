package ua.foxmided.foxstudent103852.schoolappspring.service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ua.foxmided.foxstudent103852.schoolappspring.exception.ConstraintViolationException;
import ua.foxmided.foxstudent103852.schoolappspring.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConsoleReader;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConsoleWriter;
import ua.foxmided.foxstudent103852.schoolappspring.util.Constants;

@Service
public class MenuService {
    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${inf.msg.service.menu.exception.message}")
    private String menuServiceExceptionMessage;

    @Value("${inf.msg.menu.console.get.menu.response.message}")
    private String menuGetResponseMenuMessage;

    @Value("${inf.msg.menu.console.get.menu.correct.response.message}")
    private String menuGetCorrectResponseMenuMessage;

    @Value("${inf.msg.menu.console.get.return.response.message}")
    private String menuResponseReturnMessage;

    private final ConsoleReader consoleReader;
    private final ConsoleWriter consoleWriter;
    private final MenuTransactionService menuTransactionService;

    @Autowired
    public MenuService(ConsoleReader consoleReader, ConsoleWriter consoleWriter,
            MenuTransactionService menuTransactionService) {
        this.consoleReader = consoleReader;
        this.consoleWriter = consoleWriter;
        this.menuTransactionService = menuTransactionService;
    }

    public void openMenu() {
        log.info("Opening the main menu");
        try {
            String response;
            while (!(response = getResponseInMainMenu()).equalsIgnoreCase(Constants.MENU_EXIT_VALUE)) {
                runMenu(response);
            }
            log.info("Exit from main menu");
            consoleWriter.printMessage(Constants.MENU_EXIT_MESSAGE);
        } catch (IOException e) {
            log.error(Constants.LOG_PATTERN_2_EXCEPTION, "Error in the main menu", e.getMessage(), e);
            throw new DataProcessingException(e.getMessage(), e);
        }
        log.info("Main menu closed");
    }

    private void runMenu(String response) throws IOException {
        try {
            switch (response) {
            case Constants.MENU_ITEM_1_VALUE:
                menuTransactionService.printGroupsWithFewerStudents();
                break;

            case Constants.MENU_ITEM_2_VALUE:
                menuTransactionService.printStudentsWithCourse();
                break;

            case Constants.MENU_ITEM_3_VALUE:
                menuTransactionService.addNewStudent();
                break;

            case Constants.MENU_ITEM_4_VALUE:
                menuTransactionService.deleteStudentById();
                break;

            case Constants.MENU_ITEM_5_VALUE:
                menuTransactionService.addCourseForStudent();
                break;

            case Constants.MENU_ITEM_6_VALUE:
                menuTransactionService.removeCourseForStudent();
                break;
            }
            consoleReader.getResponseToReturnInMainMenu(menuResponseReturnMessage);
        } catch (DataProcessingException e) {
            log.info(Constants.LOG_PATTERN_2_EXCEPTION, "Error while processing menu item", e.getMessage(), e);
            consoleWriter.printMessage(e.getMessage());
            consoleWriter.printMessage(menuServiceExceptionMessage);
            consoleWriter.printMessage(Constants.EMPTY_STRING);
        } catch (ConstraintViolationException e) {
            log.info(Constants.LOG_PATTERN_2_EXCEPTION, "Entity validation exception", e.getMessage(), e);
            consoleWriter.printMessage(e.getMessage());
        }
    }

    private String getResponseInMainMenu() throws IOException {
        Set<String> itemResponses = new HashSet<>(Arrays.asList(
                Constants.MENU_ITEM_1_VALUE,
                Constants.MENU_ITEM_2_VALUE,
                Constants.MENU_ITEM_3_VALUE,
                Constants.MENU_ITEM_4_VALUE,
                Constants.MENU_ITEM_5_VALUE,
                Constants.MENU_ITEM_6_VALUE));
        Set<String> menuResponses = new HashSet<>(itemResponses);
        menuResponses.add(Constants.MENU_EXIT_VALUE);
        while (true) {
            consoleWriter.printMessage(Constants.MENU_TITLE_MESSAGE);
            consoleWriter.printMessage(Constants.MENU_ITEM_1_MESSAGE);
            consoleWriter.printMessage(Constants.MENU_ITEM_2_MESSAGE);
            consoleWriter.printMessage(Constants.MENU_ITEM_3_MESSAGE);
            consoleWriter.printMessage(Constants.MENU_ITEM_4_MESSAGE);
            consoleWriter.printMessage(Constants.MENU_ITEM_5_MESSAGE);
            consoleWriter.printMessage(Constants.MENU_ITEM_6_MESSAGE);
            String response = consoleReader
                    .readString(String.format(menuGetResponseMenuMessage, Constants.MENU_EXIT_VALUE)).toLowerCase();
            if (menuResponses.contains(response)) {
                log.info(Constants.LOG_PATTERN_MESSAGE_3, "Main menu. Item '", response, "' selected");
                return response;
            }
            consoleWriter.printMessage(
                    String.format(menuGetCorrectResponseMenuMessage, itemResponses, Constants.MENU_EXIT_VALUE));
        }
    }
}
