package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SqlHelper;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.SqlHelper.cleanDatabase;
import static ru.netology.data.SqlHelper.sut;


public class PaymentTest {

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
        open(sut);
    }

    @AfterEach
    public void teardrop() {
        cleanDatabase();
    }

    @Test
    void shouldSucceedIfApprovedCardAndValidData() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidApprovedCard();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkSuccessNotification();
        SqlHelper.expectedPaymentStatus("APPROVED");
    }

    @Test
    void shouldBeRejectedIfDeclinedCardAndValidData() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidDeclinedCard();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotification();
        SqlHelper.expectedPaymentStatus("DECLINED");
    }

    @Test
    void shouldSucceedIfCurrentMonthAndCurrentYear() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidCardWithCurrentMonthAndCurrentYear();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkSuccessNotification();
        SqlHelper.expectedPaymentStatus("APPROVED");
    }

    @Test
    void shouldSucceedIfHyphenatedHolderName() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidCardWithHyphenatedName();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkSuccessNotification();
        SqlHelper.expectedPaymentStatus("APPROVED");
    }

    @Test
    void shouldGetErrorIfShortHolderName() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithShortName();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidHolder();
    }

    @Test
    void shouldGetErrorNotifyIfEmptyCardNumber() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInfoIfEmptyCardNumber();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfEmptyNumber();
    }

    @Test
    void shouldGetErrorNotifyIfNumberContains15Numbers() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWith15Numbers();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidNumber();
    }

    @Test
    void shouldGetErrorNotifyIfNumberContains17Numbers() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWith17Numbers();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidNumber();
    }

    @Test
    void shouldGetErrorNotifyIfNumberContains16RandomDigits() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWith16RandomDigits();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotification();
    }

    @Test
    void shouldGetErrorNotifyIfNumberContainsLetters() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithLettersInNumber();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidNumber();
    }

    @Test
    void shouldGetErrorNotifyIfEmptyMonth() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInfoIfEmptyCardMonth();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfEmptyMonth();
    }

    @Test
    void shouldGetErrorNotifyIfMonthValueIs00() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithMonthIf00();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidMonth();
    }

    @Test
    void shouldGetErrorNotifyIfMonthValueIs13() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithMonthIf13();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidMonth();
    }

    @Test
    void shouldGetErrorNotifyIfExpiredMonth() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithExpiredMonth();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfExpiredMonth();
    }

    @Test
    void shouldGetErrorNotifyIfEmptyYear() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInfoIfEmptyCardYear();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfEmptyYear();
    }

    @Test
    void shouldSucceedIfExpirationDateIs4YearsFromCurrent() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidCardWithPlus4YearsFromCurrent();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkSuccessNotification();
        SqlHelper.expectedPaymentStatus("APPROVED");
    }

    @Test
    void shouldSucceedIfExpirationDateIs5YearsFromCurrent() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidCardWithPlus5YearsFromCurrent();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkSuccessNotification();
        SqlHelper.expectedPaymentStatus("APPROVED");
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
    void shouldGetErrorNotifyIfExpiredYear() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithExpiredYear();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfExpiredYear();
    }

    @Test
    void shouldGetErrorNotifyIfEmptyHolder() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInfoIfEmptyCardHolder();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfEmptyHolder();
    }

    @Test
    void shouldSuccessIfCyrillicHolderName() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getValidCardWithCyrillicName();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkSuccessNotification();
        SqlHelper.expectedPaymentStatus("APPROVED");
    }

    @Test
    void shouldGetErrorIfHolderNameContainsDigits() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithNumbersInName();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidHolder();
    }

    @Test
    void shouldGetErrorIfHolderNameContainsSpecialSymbols() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCardWithSpecialSymbolsInName();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidHolder();
    }

    @Test
    void shouldGetErrorNotifyIfEmptyCVC() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInfoIfEmptyCardCVC();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfEmptyCVC();
    }

    @Test
    void shouldGetErrorNotifyIfCVCContains2Digits() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCVCWith2Digits();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidCVC();
    }

    @Test
    void shouldGetErrorNotifyIfCVCContainsLetters() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInvalidCVCWithLetters();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorNotifyIfInvalidCVC();
    }
    @Test
    void shouldGetErrorIfAllFieldsEmpty() {
        var mainPage = new MainPage();
        var cardInfo = DataHelper.getInfoIfEmptyAllFields();
        var formPage = mainPage.openPaymentForm();
        formPage.setValues(cardInfo);
        formPage.checkErrorIfEmptyAllFields();
    }
}