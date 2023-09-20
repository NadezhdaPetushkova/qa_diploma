package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.SQLHelper.cleanDatabase;

public class CreditTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:8080");
    }

    @AfterEach
    public void teardrop() {
        cleanDatabase();
    }

    @Test
    void shouldSucceedIfApprovedCardAndValidData() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidApprovedCard();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkSuccessNotification();
        SQLHelper.expectedCreditStatus("APPROVED");
    }

    @Test
    void shouldBeRejectedIfDeclinedCardAndValidData() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidDeclinedCard();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotification();
        SQLHelper.expectedCreditStatus("DECLINED");
    }

    @Test
    void shouldSucceedIfCurrentMonthAndCurrentYear() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidCardWithCurrentMonthAndCurrentYear();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkSuccessNotification();
        SQLHelper.expectedCreditStatus("APPROVED");
    }

    @Test
    void shouldSucceedIfExpirationDateIs4YearsFromCurrent() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidCardWithPlus4YearsFromCurrent();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkSuccessNotification();
        SQLHelper.expectedCreditStatus("APPROVED");
    }

    @Test
    void shouldSucceedIfExpirationDateIs5YearsFromCurrent() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidCardWithPlus5YearsFromCurrent();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkSuccessNotification();
        SQLHelper.expectedCreditStatus("APPROVED");
    }

    @Test
    void shouldGetErrorNotifyIfExpirationDateIs6YearsFromCurrent() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getCardWithPlus6YearsFromCurrent();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidYear();
    }

    @Test
    void shouldSucceedIfHyphenatedHolderName() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidCardWithHyphenatedName();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkSuccessNotification();
        SQLHelper.expectedCreditStatus("APPROVED");
    }

    @Test
    void shouldGetErrorIfShortHolderName() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidCardWithShortName();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidHolder();
    }

    @Test
    void shouldGetErrorNotifyIfEmptyNumber() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInfoIfEmptyCardNumber();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfEmptyNumber();
    }

    @Test
    void shouldGetErrorNotifyIfNumberContains15Numbers() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWith15Numbers();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidNumber();
    }

    @Test
    void shouldGetErrorNotifyIfNumberContains17Numbers() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWith17Numbers();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidNumber();
    }

    @Test
    void shouldGetErrorNotifyIfNumberContains16RandomDigits() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWith16RandomDigits();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotification();
    }

    @Test
    void shouldGetErrorNotifyIfNumberContainsLetters() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithLettersInNumber();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidNumber();
    }


    @Test
    void shouldGetErrorNotifyIfEmptyMonth() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInfoIfEmptyCardMonth();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfEmptyMonth();
    }

    @Test
    void shouldGetErrorNotifyIfMonthValueIf00() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithMonthIf00();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidMonth();
    }

    @Test
    void shouldGetErrorNotifyIfMonthValueIf13() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithMonthIf13();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidMonth();
    }

    @Test
    void shouldGetErrorNotifyIfExpiredMonth() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithExpiredMonth();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfExpiredMonth();
    }

    @Test
    void shouldGetErrorNotifyIfEmptyYear() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInfoIfEmptyCardYear();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfEmptyYear();
    }

    @Test
    void shouldGetErrorNotifyIfExpiredYear() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithExpiredYear();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfExpiredYear();
    }

    @Test
    void shouldGetErrorNotifyIfEmptyHolder() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInfoIfEmptyCardHolder();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfEmptyHolder();
    }

    @Test
    void shouldGetErrorNotifyIfEmptyCVC() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInfoIfEmptyCardCVC();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfEmptyCVC();
    }

    @Test
    void shouldGetErrorNotifyIfCyrillicHolderName() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithCyrillicName();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidHolder();
    }

    @Test
    void shouldGetErrorIfHolderNameContainsDigits() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithNumbersInName();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidHolder();
    }

    @Test
    void shouldGetErrorIfHolderNameContainsSpecialSymbols() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithSpecialSymbolsInName();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidHolder();
    }

    @Test
    void shouldGetErrorNotifyIfCVCContains2Digits() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCVCWith2Digits();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidCVC();
    }

    @Test
    void shouldGetErrorNotifyIfCVCContainsLetters() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCVCWithLetters();
        var formPage = mainPage.openCreditForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidCVC();
    }
}