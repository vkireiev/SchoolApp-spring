package ua.foxmided.foxstudent103852.schoolappspring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import ua.foxmided.foxstudent103852.schoolappspring.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConstantsTest;

@SpringBootTest(classes = { FileService.class })
@TestPropertySource(locations = { "classpath:error-message.properties", "classpath:info-message.properties" })
class FileServiceTest {

    @Autowired
    FileService fileService;

    @Test
    void readFile_WhenCalledAndFilePathIsNull_ThenException() {
        assertThrows(DataProcessingException.class, () -> fileService.readFile(null),
                "DataProcessingException was expected");
    }

    @Test
    void readFile_WhenCalledAndFilePathIsEmpty_ThenException() {
        assertThrows(DataProcessingException.class, () -> fileService.readFile(ConstantsTest.EMPTY_STRING),
                "DataProcessingException was expected");
    }

    @Test
    void readFile_WhenCalledAndFilePathIsInvalid_ThenException() {
        assertThrows(DataProcessingException.class, () -> fileService.readFile(ConstantsTest.FILE_NOT_EXISTS),
                "DataProcessingException was expected");
    }

    @Test
    void readFile_WhenCalledAndValid_ThenReturnListStrings() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("ARCHITECTURE");
        expectedResult.add("COMPUTER SCIENCE");
        expectedResult.add("Liam");
        expectedResult.add("Noah");
        expectedResult.add("Smith");
        expectedResult.add("Johnson");
        List<String> returnedResult = fileService.readFile(ConstantsTest.FILE_VALID);
        assertEquals(expectedResult.size(), returnedResult.size());
        assertTrue(expectedResult.containsAll(returnedResult));
    }
}
