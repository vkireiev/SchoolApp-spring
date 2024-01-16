package ua.foxmided.foxstudent103852.schoolappspring.service;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ua.foxmided.foxstudent103852.schoolappspring.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConsoleWriter;
import ua.foxmided.foxstudent103852.schoolappspring.util.Constants;

@Service
public class RootService {
    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${inf.msg.service.root.exception.message}")
    private String rootServiceExceptionMessage;

    @Value("${err.msg.service.root.database.not.fill}")
    private String rootDataMissingCloseAppMessage;

    private final DatabaseInitService databaseInitService;
    private final DataGeneratorService dataGeneratorService;
    private final ConsoleWriter consoleWriter;
    private final MenuService menuService;

    @Autowired
    public RootService(DatabaseInitService databaseInitService,
            DataGeneratorService dataGeneratorService, ConsoleWriter consoleWriter, MenuService menuService) {
        this.databaseInitService = databaseInitService;
        this.dataGeneratorService = dataGeneratorService;
        this.consoleWriter = consoleWriter;
        this.menuService = menuService;
    }

    public void main() {
        log.info("Starting the application");
        try {
            if (!databaseInitService.isDatabaseFilled() && databaseInitService.clearDatabase()) {
                dataGeneratorService.fillDatabase();
            }
            if (databaseInitService.isDatabaseFilled()) {
                menuService.openMenu();
            } else {
                log.info("Unable to continue running the application due to missing data in the database");
                consoleWriter.printMessage(rootDataMissingCloseAppMessage);
            }
        } catch (DataProcessingException e) {
            log.error(Constants.LOG_PATTERN_2_EXCEPTION, "Something went wrong...", e.getMessage(), e);
            consoleWriter.printMessage(rootServiceExceptionMessage);
        }
        log.info("Closing the application");
    }
}
