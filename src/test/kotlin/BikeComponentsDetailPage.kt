import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory

// page_url = https://www.bike-components.de/en/ASSOS/Chamois-Creme-p82794/
class BikeComponentsDetailPage(driver: WebDriver) {
    @FindBy(xpath = "//h1")
    lateinit var h1AutoProductName: WebElement

    @FindBy(xpath = "//button[contains(@class, 'wishlist')]")
    lateinit var buttonWishlist: WebElement

    @FindBy(xpath = "//div[@class='add-to-cart-button']/div/button")
    lateinit var buttonAddToCart: WebElement

    @FindBy(xpath = "//button[@class='site-link link-to-cart']")
    lateinit var buttonGoToCart: WebElement

    init {
        PageFactory.initElements(driver, this)
    }
}