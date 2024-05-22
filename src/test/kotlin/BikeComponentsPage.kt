import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.FindBys
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

// page_url = https://www.bike-components.de/en/
class BikeComponentsPage(private val driver: WebDriver) {

    @FindBy(xpath = "//h3[text()='Availability']")
    lateinit var filterAvailability: WebElement


    @FindBy(xpath = "//h3[text()='Availability']/following-sibling::div//label[contains(text(),'in stock')]")
    lateinit var checkboxInStock: WebElement



    fun setFilterInStock() {
        Actions(driver)
            .moveToElement(filterAvailability)
            .click()
            .perform()
        val wait = WebDriverWait(driver, Duration.ofMillis(3000))
        wait.until(ExpectedConditions.elementToBeClickable(checkboxInStock))
        Actions(driver)
            .moveToElement(checkboxInStock)
            .click()
            .perform()
        Thread.sleep(3000)
    }


    @FindBy(xpath = "//a[contains(@title, 'Watchlist')]")
    lateinit var linkWishlistIcon: WebElement

    @FindBy(xpath = "//button[@data-test='auto-cookie-banner-close']")
    lateinit var buttonAutoCookieBannerClose: WebElement


    @FindBys(FindBy(xpath = "//nav[@class='module-navigation-main js-nav-main']/ul/li/a"))
    lateinit var navTabs: MutableList<WebElement>

    @FindBy(xpath = "//div[@data-test='product-view-grid']")
    lateinit var productViewGrid: WebElement
    
    
    
    init {
        PageFactory.initElements(driver, this)
    }
}