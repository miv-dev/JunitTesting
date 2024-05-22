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
class TestAddToCart: BaseTestInstance{
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
    fun `set filter in stock`(){
        page.setFilterInStock()
    }


    @Test
    @Order(4)
    fun `select random product`() {
        val products = page.productViewGrid.findElements(By.xpath("//div[contains(@class,'product-item-extended')]/a"))
        val product = products.random()
        Actions(driver)
            .moveToElement(product)
            .click()
            .perform()
    }

    private lateinit var addedToCartProductTitle: String

    @Test
    @Order(5)
    fun `add to cart`() {
        val detailPage = BikeComponentsDetailPage(driver)
        val wait = WebDriverWait(driver, Duration.ofMillis(10_000))
        wait.until(
            ExpectedConditions.elementToBeClickable(
                detailPage.h1AutoProductName
            )
        )
        addedToCartProductTitle = detailPage.h1AutoProductName.text
        Actions(driver)
            .moveToElement(detailPage.buttonAddToCart)
            .click()
            .perform()

        Thread.sleep(2000)
        detailPage.buttonGoToCart.click()
        Thread.sleep(3000)

    }

    lateinit var cartPage: BikeComponentsCartPage

    @Test
    @Order(6)
    fun `check added product`() {
        cartPage = BikeComponentsCartPage(driver)

        val wait = WebDriverWait(driver, Duration.ofMillis(10_000))
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1")))
        val result = cartPage.cartItems.map {
            it.findElement(By.xpath("//a[@class='title']")).text.lowercase()
        }
        assertContains(result, addedToCartProductTitle.lowercase())
    }

    @Test
    @Order(7)
    fun `delete from wishlist`() {
        val result = cartPage.cartItems.find {
            it.findElement(By.xpath("//a[@class='title']")).text.lowercase() == addedToCartProductTitle.lowercase()
        }
        result?.findElement(By.xpath("//button[@class='link delete']"))?.click()
        val wait = WebDriverWait(driver, Duration.ofMillis(2000))
        wait.until(ExpectedConditions.elementToBeClickable(cartPage.confirmButton)).click()
        Thread.sleep(2000)
        val items = cartPage.cartItems.map {
            it.findElement(By.xpath("//a[@class='title']")).text.lowercase()
        }
        assert(addedToCartProductTitle.lowercase() !in items)
    }

    @AfterAll
    fun `close browser`() {
        driver.close()
    }

    companion object {
        const val BASE_URL = "https://www.bike-components.de/en/"
    }


}

