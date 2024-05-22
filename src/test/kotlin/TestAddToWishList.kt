import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import kotlin.test.assertContains


@ExtendWith(TakeScreenShotOnFailure::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TestAddToWishList: BaseTestInstance {
    override val driver = ChromeDriver()

    lateinit var page: BikeComponentsPage

    @BeforeAll
    fun `open browser`() {
        driver.get(BASE_URL)
        page = BikeComponentsPage(driver)
        driver.manage().window().maximize()
    }


    @Test
    @Order(1)
    fun `accept cookie`() {
        page.buttonAutoCookieBannerClose.click()
        Thread.sleep(4000)
    }


    @Test
    @Order(2)
    fun `select random tab`() {
        val tabs = page.navTabs
        tabs.subList(1, tabs.size - 3).random().also {
            println("Selected tab: ${it.text}")
            val wait = WebDriverWait(driver, Duration.ofMillis(7_000))
            wait.until(ExpectedConditions.elementToBeClickable(it)).click()
        }
    }

    @Test
    @Order(3)
    fun `select random product`() {
        val products = page.productViewGrid.findElements(By.xpath("//div[contains(@class,'product-item-extended')]/a"))
        val product = products.random()
        Actions(driver)
            .moveToElement(product)
            .click()
            .perform()
    }

    private lateinit var addedToWishlistProductTitle: String

    @Test
    @Order(4)
    fun `add to wishlist`() {
        val detailPage = BikeComponentsDetailPage(driver)
        val wait = WebDriverWait(driver, Duration.ofMillis(10_000))
        wait.until(
            ExpectedConditions.elementToBeClickable(
                detailPage.h1AutoProductName
            )
        )
        addedToWishlistProductTitle = detailPage.h1AutoProductName.text
        Actions(driver)
            .moveToElement(detailPage.buttonWishlist)
            .click()
            .perform()

        Thread.sleep(3000)
    }

    lateinit var wishlistPage: BikeComponentsWishlistPage

    @Test
    @Order(5)
    fun `check added product`() {
        page.linkWishlistIcon.click()
        wishlistPage = BikeComponentsWishlistPage(driver)

        val wait = WebDriverWait(driver, Duration.ofMillis(10_000))
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1")))
        val result = wishlistPage.wishlistItems.map {
            it.findElement(By.xpath("//a[@class='title']")).text.lowercase()
        }
        assertContains(result, addedToWishlistProductTitle.lowercase())
    }

    @Test
    @Order(6)
    fun `delete from wishlist`() {
        val result = wishlistPage.wishlistItems.find {
            it.findElement(By.xpath("//a[@class='title']")).text.lowercase() == addedToWishlistProductTitle.lowercase()
        }
        result?.findElement(By.xpath("//button[@class='link delete']"))?.click()
        val wait = WebDriverWait(driver, Duration.ofMillis(2000))
        wait.until(ExpectedConditions.elementToBeClickable(wishlistPage.confirmButton)).click()
        Thread.sleep(2000)
        val items = wishlistPage.wishlistItems.map {
            it.findElement(By.xpath("//a[@class='title']")).text.lowercase()
        }

        assert(addedToWishlistProductTitle.lowercase() !in items)
    }

    @AfterAll
    fun `close browser`() {
        driver.close()
    }

    companion object {
        const val BASE_URL = "https://www.bike-components.de/en/"
    }


}
