package StepDefinations;

import io.cucumber.java.After;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationStepdefs {

    private WebDriver driver;
    private WebDriverWait wait;

    private final String url = "file:///C:/Users/akhla/Downloads/Register/Register.html";

    // Locators (inputs)
    private final By firstName = By.name("Forename");
    private final By lastName = By.name("Surname");
    private final By email = By.name("EmailAddress");
    private final By confirmEmail = By.name("ConfirmEmailAddress");
    private final By password = By.name("Password");
    private final By confirmPass = By.name("ConfirmPassword");

    private final By dateInput = By.id("dp");
    private final By submitBtn = By.name("join");

    // Checkboxes
    private final By termsLabel = By.cssSelector("label[for='sign_up_25']");
    private final By over18Label = By.cssSelector("label[for='sign_up_26']");
    private final By ethicsLabel = By.cssSelector("label[for='fanmembersignup_agreetocodeofethicsandconduct']");

    // Success
    private final By successHeader = By.cssSelector("h2.bold");

    // Datepicker containers
    private final By dpDaysSwitch   = By.cssSelector("div.datepicker-days th.datepicker-switch");
    private final By dpMonthsSwitch = By.cssSelector("div.datepicker-months th.datepicker-switch");
    private final By dpYearsSwitch  = By.cssSelector("div.datepicker-years th.datepicker-switch");
    private final By dpYearsPrev    = By.cssSelector("div.datepicker-years th.prev");
    private final By dpYearsNext    = By.cssSelector("div.datepicker-years th.next");


    @After
    public void tearDown() throws InterruptedException {

        Thread.sleep(2000);

        if (driver != null) driver.quit();
    }

    // ---------- GIVEN ----------
    @Given("I am on the registration page")
    public void iAmOnTheRegistrationPage() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(url);
        driver.manage().window().maximize();
    }

    @Given("I am on the registration page in {string}")
    public void iAmOnTheRegistrationPageIn(String browser) {
        driver = createDriver(browser);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(url);
        driver.manage().window().maximize();
    }

    // ---------- WHEN ----------
    @When("I select date of birth {string}")
    public void iSelectDateOfBirth(String date) throws InterruptedException {
        // date format: yyyy-MM-dd
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]); // 1-12
        int day = Integer.parseInt(parts[2]);   // 1-31

        setDateOfBirth(year, month, day);

        // Print the value in the input
        String value = driver.findElement(dateInput).getAttribute("value");
        System.out.println("Date of birth selected: " + value);

        //if (demoMode)
        Thread.sleep(2000);
    }

    @And("I fill in valid registration field")
    public void iFillInValidRegistrationField() throws InterruptedException {
        type(firstName, "test");
        type(lastName, "user");
        type(email, "test@gmail.com");
        type(confirmEmail, "test@gmail.com");
        type(password, "password123");
        type(confirmPass, "password123");
        //if (demoMode)
        Thread.sleep(2000);
    }

    @And("I fill in all required fields except lastname")
    public void iFillInAllRequiredFieldsExceptLastname() throws InterruptedException {
        type(firstName, "Test");
        // lastname is intentionally missing
        type(email, "test@gmail.com");
        type(confirmEmail, "test@gmail.com");
        type(password, "Test123");
        type(confirmPass, "Test123");
        //if (demoMode) Thread.sleep(demoPauseMs);
        Thread.sleep(2000);
    }

    @And("I fill in all required fields with mismatch password")
    public void iFillInAllRequiredFieldsWithMismatchPassword() throws InterruptedException {
        type(firstName, "Test");
        type(lastName, "user");
        type(email, "test@gmail.com");
        type(confirmEmail, "test@gmail.com");
        type(password, "Test123");
        type(confirmPass, "Test321");
        click(termsLabel);
        //if (demoMode) Thread.sleep(demoPauseMs);
        Thread.sleep(2000);
    }

    // Outline: fill all fields with parameters
    @And("I fill in valid registration field with firstName {string} lastname {string} email {string} confirmEmail {string} password {string} confirmPassword {string}")
    public void iFillRegistrationFields(String fn, String ln, String em, String cem, String pwd, String cpwd) throws InterruptedException {
        type(firstName, fn);

        // only type last name if not empty
        if (ln != null && !ln.isBlank()) {
            type(lastName, ln);
        }

        type(email, em);
        type(confirmEmail, cem);
        type(password, pwd);
        type(confirmPass, cpwd);
        Thread.sleep(2000);
    }

    // Terms / checkboxes
    @And("I accept the Terms and Conditions")
    public void iAcceptTheTermsAndConditions() throws InterruptedException {
        click(termsLabel);
        Thread.sleep(2000);

    }

    @And("I don Not accept the Terms and Conditions")
    public void iDoNotAcceptTerms() {

    }

    @And("I accept the I am over 18 or parently responsibility")
    public void iAcceptOver18() throws InterruptedException {
        click(over18Label);
        Thread.sleep(2000);

    }

    @And("I accept that I have read")
    public void iAcceptEthics() throws InterruptedException {
        click(ethicsLabel);
        Thread.sleep(2000);

    }

    // Outline: accept terms based on "true/false"
    @And("I accept the Terms and Conditions to {string}")
    public void iAcceptTermsAccordingTo(String acceptTerms) throws InterruptedException {
        boolean shouldAccept = Boolean.parseBoolean(acceptTerms);

        if (shouldAccept) {
            click(termsLabel);
        }
        click(over18Label);
        click(ethicsLabel);
        Thread.sleep(2000);

    }

    @And("I submit the registration form")
    public void iSubmitTheRegistrationForm() throws InterruptedException {
        click(submitBtn);
        Thread.sleep(2000);

    }

    // ---------- THEN ----------
    @Then("I should see the account creation success message")
    public void iShouldSeeSuccess() {
        WebElement header = waitVisible(successHeader);
        assertTrue(header.isDisplayed());
        System.out.println("SUCCESS header: " + header.getText());
    }

    @Then("I should see error message {string}")
    public void iShouldSeeLastnameErrorMessage(String expectedMessage) {
        // Robust: locate by "for" + message
        By locator = By.xpath("//span[@for='member_lastname' and normalize-space()='" + expectedMessage + "']");
        WebElement error = waitVisible(locator);
        assertEquals(expectedMessage, error.getText().trim());
    }

    @Then("I should see error message for password {string}")
    public void iShouldSeePasswordErrorMessage(String expectedMessage) {
        By locator = By.xpath("//span[@for='signupunlicenced_confirmpassword' and normalize-space()='" + expectedMessage + "']");
        WebElement error = waitVisible(locator);
        assertEquals(expectedMessage, error.getText().trim());
    }

    @Then("I should see error message for Terms and Conditions {string}")
    public void iShouldSeeTermsErrorMessage(String expectedMessage) {
        By locator = By.xpath("//span[@for='TermsAccept' and normalize-space()='" + expectedMessage + "']");
        WebElement error = waitVisible(locator);
        assertEquals(expectedMessage, error.getText().trim());
    }

    // Outline generic assert
    @Then("I should see the field error for {string} with message {string}")
    public void iShouldSeeTheFieldErrorForWithMessage(String errorFor, String expectedMessage) {
        // Ensure validation is triggered
        driver.findElement(By.tagName("body")).click();

        By locator = By.xpath("//span[@for='" + errorFor + "' and normalize-space()='" + expectedMessage + "']");
        WebElement error = waitVisible(locator);

        String actual = error.getText().trim();
        System.out.println("ERROR for=" + errorFor + " -> " + actual);

        assertEquals(expectedMessage, actual);
    }

    // ---------- VG requirement: explicit wait helper method ----------
    private WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void click(By locator) throws InterruptedException {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        el.click();
        Thread.sleep(2000);

    }

    private void type(By locator, String text) {
        WebElement el = waitVisible(locator);
        el.clear();
        el.sendKeys(text);
        System.out.println(locator + " value: " + el.getAttribute("value"));
    }

    private WebDriver createDriver(String browser) {
        if (Objects.equals(browser.toLowerCase(), "edge")) {
            return new EdgeDriver();
        }
        return new ChromeDriver();
    }

    // ---------- Datepicker: works for any year ----------
    private void setDateOfBirth(int year, int month, int day) throws InterruptedException {

        // open datepicker
        click(dateInput);

        // Days -> Months -> Years
        click(dpDaysSwitch);
        click(dpMonthsSwitch);

        // Now we are in YEARS view (shows range  "2020-2029")
        // Move with prev/next until the year exists
        By yearLocator = By.xpath("//div[contains(@class,'datepicker-years')]//span[contains(@class,'year') and normalize-space()='" + year + "']");

        int safety = 30; // prevent infinite loops
        while (driver.findElements(yearLocator).isEmpty() && safety-- > 0) {

            String rangeText = driver.findElement(By.cssSelector("div.datepicker-years th.datepicker-switch")).getText().trim();
            // rangeText example: "2020-2029"
            String[] range = rangeText.split("-");
            int start = Integer.parseInt(range[0]);
            int end = Integer.parseInt(range[1]);

            if (year < start) {
                click(dpYearsPrev);
            } else if (year > end) {
                click(dpYearsNext);
            } else {
                break;
            }
        }

        // select year
        click(yearLocator);

        // (Jan=1..Dec=12)
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        String monthText = months[month - 1];
        By monthLocator = By.xpath("//div[contains(@class,'datepicker-months')]//span[contains(@class,'month') and normalize-space()='" + monthText + "']");
        click(monthLocator);

        // select day (avoid selecting old/new days from other months)
        By dayLocator = By.xpath("//div[contains(@class,'datepicker-days')]//td[contains(@class,'day') and not(contains(@class,'old')) and not(contains(@class,'new')) and normalize-space()='" + day + "']");
        click(dayLocator);
        Thread.sleep(2000);


    }
}
