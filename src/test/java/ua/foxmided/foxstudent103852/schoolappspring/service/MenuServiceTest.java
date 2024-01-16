package ua.foxmided.foxstudent103852.schoolappspring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import ua.foxmided.foxstudent103852.schoolappspring.util.ConsoleReader;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConsoleWriter;
import ua.foxmided.foxstudent103852.schoolappspring.util.Constants;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConstantsTest;

@SpringBootTest(classes = { MenuService.class })
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = { "classpath:error-message.properties", "classpath:info-message.properties" })
class MenuServiceTest {

    @Value("${inf.msg.menu.console.get.menu.correct.response.message}")
    private String menuGetCorrectResponseMenuMessage;

    private static final List<String> menuItems = new ArrayList<>(Arrays.asList(
            Constants.MENU_TITLE_MESSAGE,
            Constants.MENU_ITEM_1_MESSAGE,
            Constants.MENU_ITEM_2_MESSAGE,
            Constants.MENU_ITEM_3_MESSAGE,
            Constants.MENU_ITEM_4_MESSAGE,
            Constants.MENU_ITEM_5_MESSAGE,
            Constants.MENU_ITEM_6_MESSAGE));
    private static final Set<String> itemResponses = new HashSet<>(Arrays.asList(
            Constants.MENU_ITEM_1_VALUE,
            Constants.MENU_ITEM_2_VALUE,
            Constants.MENU_ITEM_3_VALUE,
            Constants.MENU_ITEM_4_VALUE,
            Constants.MENU_ITEM_5_VALUE,
            Constants.MENU_ITEM_6_VALUE));

    @MockBean
    private ConsoleReader consoleReaderMock;
    @MockBean
    private ConsoleWriter consoleWriterMock;
    @MockBean
    private MenuTransactionService menuTransactionServiceMock;

    @Autowired
    private MenuService menuService;

    @Captor
    ArgumentCaptor<String> writerStringCaptor = ArgumentCaptor.forClass(String.class);

    @Test
    void openMenu_WhenCallAndTryInputEmptyStringAsItem_ThenRepeatWhileInputEmptyString() throws IOException {
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.addAll(menuItems);
        expectedResultList
                .add(String.format(menuGetCorrectResponseMenuMessage, itemResponses, Constants.MENU_EXIT_VALUE));
        expectedResultList.addAll(menuItems);
        expectedResultList
                .add(String.format(menuGetCorrectResponseMenuMessage, itemResponses, Constants.MENU_EXIT_VALUE));
        expectedResultList.addAll(menuItems);
        expectedResultList.add(Constants.MENU_EXIT_MESSAGE);

        Mockito.when(consoleReaderMock.readString(Mockito.anyString()))
                .thenReturn(
                        Constants.EMPTY_STRING,
                        Constants.EMPTY_STRING,
                        Constants.MENU_EXIT_VALUE);

        menuService.openMenu();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void openMenu_WhenCallAndTryInputNotValidItem_ThenRepeatWhileNotValidItem() throws IOException {
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.addAll(menuItems);
        expectedResultList
                .add(String.format(menuGetCorrectResponseMenuMessage, itemResponses, Constants.MENU_EXIT_VALUE));
        expectedResultList.addAll(menuItems);
        expectedResultList
                .add(String.format(menuGetCorrectResponseMenuMessage, itemResponses, Constants.MENU_EXIT_VALUE));
        expectedResultList.addAll(menuItems);
        expectedResultList.add(Constants.MENU_EXIT_MESSAGE);

        Mockito.when(consoleReaderMock.readString(Mockito.anyString()))
                .thenReturn(
                        ConstantsTest.MENU_ITEM_INVALID_VALUE,
                        ConstantsTest.MENU_ITEM_INVALID_VALUE,
                        Constants.MENU_EXIT_VALUE);

        menuService.openMenu();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void openMenu_WhenCallAndInputValidItem_ThenRunMethodAndReturnToMainMenu() throws IOException {
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.addAll(menuItems);
        expectedResultList.addAll(menuItems);
        expectedResultList.add(Constants.MENU_EXIT_MESSAGE);

        Mockito.when(consoleReaderMock.readString(Mockito.anyString()))
                .thenReturn(
                        Constants.MENU_ITEM_1_VALUE,
                        Constants.MENU_EXIT_VALUE);

        menuService.openMenu();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }
}
