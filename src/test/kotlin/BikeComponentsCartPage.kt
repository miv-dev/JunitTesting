import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.FindBys
import org.openqa.selenium.support.PageFactory

// page_url = https://www.bike-components.de/en/wishlist/
class BikeComponentsCartPage(driver: WebDriver) {

    @FindBys(FindBy(xpath = "//div[contains(@class, 'description')]/a"))
    lateinit var cartItems: MutableList<WebElement>

    @FindBy(xpath = "//button[contains(@class, 'js-button-confirm')]")
    lateinit var confirmButton: WebElement

    init {
        PageFactory.initElements(driver, this)
    }
}