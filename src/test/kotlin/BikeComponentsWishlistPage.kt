import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.FindBys
import org.openqa.selenium.support.PageFactory

// page_url = https://www.bike-components.de/en/wishlist/
class BikeComponentsWishlistPage(driver: WebDriver) {

    @FindBys(FindBy(xpath = "//div[@class='items']/div"))
    lateinit var wishlistItems: MutableList<WebElement>

    @FindBy(xpath = "//button[contains(@class, 'confirm')]")
    lateinit var confirmButton: WebElement

    init {
        PageFactory.initElements(driver, this)
    }
}