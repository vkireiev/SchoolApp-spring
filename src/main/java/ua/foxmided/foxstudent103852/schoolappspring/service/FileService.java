package ua.foxmided.foxstudent103852.schoolappspring.service;

import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ua.foxmided.foxstudent103852.schoolappspring.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolappspring.util.Constants;

@Service
public class FileService {
    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${err.msg.service.file.read.fail}")
    private String fileReadFailMessage;

    public List<String> readFile(String filePath) {
        log.info(Constants.LOG_PATTERN_MESSAGE_3, "Reading file '", filePath, "'");
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (Exception e) {
            log.error(Constants.LOG_PATTERN_3_EXCEPTION,
                    "Failed to read file", filePath, e.getMessage(), e);
            throw new DataProcessingException(String.format(fileReadFailMessage, filePath), e);
        }
    }
}
