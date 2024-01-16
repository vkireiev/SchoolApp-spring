package ua.foxmided.foxstudent103852.schoolappspring.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = { ConsoleReader.class })
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = { "classpath:error-message.properties", "classpath:info-message.properties" })
class ConsoleReaderTest {

    @MockBean
    private BufferedReader readerMock;
    @MockBean
    private ConsoleWriter consoleWriterMock;

    @Autowired
    private ConsoleReader consoleReader;

    @Captor
    ArgumentCaptor<String> writerStringCaptor = ArgumentCaptor.forClass(String.class);

    @Test
    void readString_TryInputEmptyString_ThenRepeatWhileInputEmptyString() throws IOException {
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);

        Mockito.when(readerMock.readLine())
                .thenReturn(
                        Constants.EMPTY_STRING,
                        Constants.EMPTY_STRING,
                        Constants.MENU_EXIT_VALUE);

        consoleReader.readString(ConstantsTest.MOCKITO_STRING_STUB_1);
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();

        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertIterableEquals(expectedResultList, captoredResultList);

        Mockito.verify(readerMock, Mockito.times(3)).readLine();
    }

    @Test
    void readString_InputNotEmptyString_ThenNoRepeat() throws IOException {
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);

        Mockito.when(readerMock.readLine())
                .thenReturn(Constants.MENU_EXIT_VALUE);

        consoleReader.readString(ConstantsTest.MOCKITO_STRING_STUB_1);
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();

        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertIterableEquals(expectedResultList, captoredResultList);

        Mockito.verify(readerMock, Mockito.times(1)).readLine();
    }

    @Test
    void readYesNo_TryInputEmptyStringOrNotValidValue_ThenRepeatWhileInputNotValidValue() throws IOException {
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);

        Mockito.when(readerMock.readLine())
                .thenReturn(
                        Constants.EMPTY_STRING,
                        Constants.MENU_EXIT_VALUE.toUpperCase(),
                        Constants.EMPTY_STRING,
                        Constants.MENU_ITEM_1_VALUE,
                        Constants.MENU_ITEM_6_VALUE.toLowerCase(),
                        Constants.MENU_ITEM_Y_VALUE.toUpperCase());

        consoleReader.readYesNo(ConstantsTest.MOCKITO_STRING_STUB_1);
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();

        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertIterableEquals(expectedResultList, captoredResultList);

        Mockito.verify(readerMock, Mockito.times(6)).readLine();
    }

    @Test
    void readYesNo_InputValidValue_ThenNoRepeat() throws IOException {
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);

        Mockito.when(readerMock.readLine())
                .thenReturn(Constants.MENU_ITEM_N_VALUE.toUpperCase());

        consoleReader.readString(ConstantsTest.MOCKITO_STRING_STUB_1);
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();

        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertIterableEquals(expectedResultList, captoredResultList);

        Mockito.verify(readerMock, Mockito.times(1)).readLine();
    }

    @Test
    void readNumber_TryInputEmptyStringOrNotValidValue_ThenRepeatWhileInputNotValidValue() throws IOException {
        int testValue = 11;
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_2);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_2);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_2);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);

        Mockito.when(readerMock.readLine())
                .thenReturn(
                        Constants.EMPTY_STRING,
                        Constants.MENU_EXIT_VALUE.toUpperCase(),
                        Constants.MENU_ITEM_6_VALUE.toLowerCase(),
                        String.valueOf(testValue));

        consoleReader.readNumber(ConstantsTest.MOCKITO_STRING_STUB_1, ConstantsTest.MOCKITO_STRING_STUB_2);
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();

        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertIterableEquals(expectedResultList, captoredResultList);

        Mockito.verify(readerMock, Mockito.times(4)).readLine();
    }

    @Test
    void readNumber_InputValidValue_ThenNoRepeat() throws IOException {
        int testValue = 111;
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);

        Mockito.when(readerMock.readLine())
                .thenReturn(String.valueOf(testValue));

        consoleReader.readNumber(ConstantsTest.MOCKITO_STRING_STUB_1, ConstantsTest.MOCKITO_STRING_STUB_2);
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();

        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertIterableEquals(expectedResultList, captoredResultList);

        Mockito.verify(readerMock, Mockito.times(1)).readLine();
    }

    @Test
    void readNumberFromSet_TryInputEmptyStringOrNotValidValue_ThenRepeatWhileInputNotValidValue() throws IOException {
        List<Long> validValues = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        List<Long> invalidValues = new ArrayList<>(Arrays.asList(6L, 7L, 8L, 9L, 0L));
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_2);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_3);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_2);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_3);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_2);
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);

        Mockito.when(readerMock.readLine())
                .thenReturn(
                        Constants.EMPTY_STRING,
                        String.valueOf(invalidValues.get((int) (Math.random() * invalidValues.size()))),
                        Constants.MENU_EXIT_VALUE.toUpperCase(),
                        String.valueOf(invalidValues.get((int) (Math.random() * invalidValues.size()))),
                        Constants.MENU_ITEM_6_VALUE.toLowerCase(),
                        String.valueOf(validValues.get((int) (Math.random() * validValues.size()))));

        consoleReader.readNumberFromSet(ConstantsTest.MOCKITO_STRING_STUB_1,
                validValues.stream().collect(Collectors.toSet()),
                ConstantsTest.MOCKITO_STRING_STUB_2,
                ConstantsTest.MOCKITO_STRING_STUB_3);
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();

        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertIterableEquals(expectedResultList, captoredResultList);

        Mockito.verify(readerMock, Mockito.times(6)).readLine();
    }

    @Test
    void readNumberFromSet_InputValidValue_ThenNoRepeat() throws IOException {
        List<Long> validValues = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(ConstantsTest.MOCKITO_STRING_STUB_1);

        Mockito.when(readerMock.readLine())
                .thenReturn(String.valueOf(validValues.get((int) (Math.random() * validValues.size()))));

        consoleReader.readNumberFromSet(ConstantsTest.MOCKITO_STRING_STUB_1,
                validValues.stream().collect(Collectors.toSet()),
                ConstantsTest.MOCKITO_STRING_STUB_2,
                ConstantsTest.MOCKITO_STRING_STUB_3);
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();

        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertIterableEquals(expectedResultList, captoredResultList);

        Mockito.verify(readerMock, Mockito.times(1)).readLine();
    }
}
