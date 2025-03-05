package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.HomePage;
import page.CreditPage;
import static com.codeborne.selenide.Selenide.open;

public class PaymentTest {
    HomePage home;
    CreditPage creditPage;
    String approvedCardNumber = DataHelper.getCardApproved().getCardNumber();
    String declinedCardNumber = DataHelper.getCardDeclined().getCardNumber();
    String randomCardNumber = DataHelper.getRandomCardNumber();
    String validMonth = DataHelper.getRandomMonth(1);
    String validYear = DataHelper.getRandomYear(1);
    String validOwnerName = DataHelper.getRandomName();
    String validCode = DataHelper.getNumberCVC(3);

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterAll
    public static void shouldCleanBase() {
        SQLHelper.cleanBase();
    }

    @Test
    public void shouldApprovedCardPayment() {
        var cardPage = home.cardPayment();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        cardPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCardPayment());
    }

    @Test
    public void shouldDeclinedCardPayment() {
        var cardPage = home.cardPayment();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        cardPage.bankDeclinedOperation();
        Assertions.assertEquals("DECLINED", SQLHelper.getCardPayment());
    }

    @Test
    public void shouldRandomCardNumberCardPayment() {
        var cardPage = home.cardPayment();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(randomCardNumber, validMonth, validYear, validOwnerName, validCode);
        cardPage.bankDeclinedOperation();
    }

    @Test
    public void shouldInvalidCardNumberCardPayment() {
        var cardPage = home.cardPayment();
        var invalidCardNumber = DataHelper.getRandomShorterCardNumber();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(invalidCardNumber, validMonth, validYear, validOwnerName, validCode);
        cardPage.errorFormat();
    }

    @Test
    public void shouldEmptyCardNumberCardPayment() {
        var cardPage = home.cardPayment();
        var emptyCardNumber = DataHelper.getEmptyField();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(emptyCardNumber, validMonth, validYear, validOwnerName, validCode);
        cardPage.errorFormat();
    }

    @Test
    public void shouldCardPaymentWithMonthTermValidityExpired() {
        var cardPage = home.cardPayment();
        var currentYear = DataHelper.getRandomYear(0);
        var monthTermValidityExpired = DataHelper.getRandomMonth(-1);
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, monthTermValidityExpired, currentYear, validOwnerName, validCode);
        cardPage.errorCardTermValidity();
    }

    @Test
    public void shouldCardPaymentWithInvalidMonth() {
        var cardPage = home.cardPayment();
        var invalidMonth = DataHelper.getInvalidMonth();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, invalidMonth, validYear, validOwnerName, validCode);
        cardPage.errorCardTermValidity();
    }

    @Test
    public void shouldCardPaymentWithEmptyMonth() {
        var cardPage = home.cardPayment();
        var emptyMonth = DataHelper.getEmptyField();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, emptyMonth, validYear, validOwnerName, validCode);
        cardPage.errorFormat();
    }

    @Test
    public void shouldCardPaymentWithYearTermValidityExpired() {
        var cardPage = home.cardPayment();
        var yearTermValidityExpired = DataHelper.getRandomYear(-1);
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, yearTermValidityExpired, validOwnerName, validCode);
        cardPage.termValidityExpired();
    }

    @Test
    public void shouldCardPaymentWithInvalidYear() {
        var cardPage = home.cardPayment();
        var invalidYear = DataHelper.getRandomYear(6);
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, invalidYear, validOwnerName, validCode);
        cardPage.errorCardTermValidity();
    }

    @Test
    public void shouldCardPaymentWithEmptyYear() {
        var cardPage = home.cardPayment();
        var emptyYear = DataHelper.getEmptyField();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, emptyYear, validOwnerName, validCode);
        cardPage.errorFormat();
    }

    @Test
    public void shouldRusLangNameCardPayment() {
        var cardPage = home.cardPayment();
        var rusLangName = DataHelper.getRandomNameRus();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, rusLangName, validCode);
        cardPage.errorFormat();
    }

    @Test
    public void shouldDigitsNameCardPayment() {
        var cardPage = home.cardPayment();
        var digitsName = DataHelper.getNumberName();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, digitsName, validCode);
        cardPage.errorFormat();
    }

    @Test
    public void shouldSpecSymbolsNameCardPayment() {
        var cardPage = home.cardPayment();
        var specSymbolsName = DataHelper.getSpecSymbolName();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, specSymbolsName, validCode);
        cardPage.errorFormat();
    }

    @Test
    public void shouldEmptyNameCardPayment() {
        var cardPage = home.cardPayment();
        var emptyName = DataHelper.getEmptyField();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, emptyName, validCode);
        cardPage.emptyField();
    }

    @Test
    public void shouldTwoDigitsCodeCardPayment() {
        var cardPage = home.cardPayment();
        var twoDigitsCode = DataHelper.getNumberCVC(2);
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, twoDigitsCode);
        cardPage.errorFormat();
    }

    @Test
    public void shouldLettersCodeCardPayment() {
        var cardPage = home.cardPayment();
        var lettersCode = DataHelper.getRandomName();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, lettersCode);
        cardPage.errorFormat();
    }

    @Test
    public void shouldSpecSymbolsCodeCardPayment() {
        var cardPage = home.cardPayment();
        var specSymbolsCode = DataHelper.getSpecSymbolName();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, specSymbolsCode);
        cardPage.errorFormat();
    }

    @Test
    public void shouldEmptyCodeCardPayment() {
        var cardPage = home.cardPayment();
        var emptyCode = DataHelper.getEmptyField();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, emptyCode);
        cardPage.errorFormat();
    }

    @Test
    public void shouldEmptyAllFieldsCardPayment() {
        var cardPage = home.cardPayment();
        var emptyCardNumber = DataHelper.getEmptyField();
        var emptyMonth = DataHelper.getEmptyField();
        var emptyYear = DataHelper.getEmptyField();
        var emptyName = DataHelper.getEmptyField();
        var emptyCode = DataHelper.getEmptyField();
        cardPage.cleanFields();
        cardPage.fillCardPaymentForm(emptyCardNumber, emptyMonth, emptyYear, emptyName, emptyCode);
        cardPage.errorFormat();
    }
}
