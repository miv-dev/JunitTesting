import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.test.assertEquals


@ExtendWith(TakeScreenShotOnFailure::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TestYandex {
    val driver = ChromeDriver()
    private lateinit var page: YandexPage

    @BeforeAll
    fun `open browser`() {
        driver.get(BASE_URL)
        driver.manage().window().maximize()
        val wait = WebDriverWait(driver, Duration.ofSeconds(20))
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(YandexPage.CATALOG_BUTTON_XPATH)))
        page = YandexPage(driver)
    }

    @Test
    @Order(1)
    fun `click catalog button`() {
        page.catalogButton.click()
        driver.wait(1000)
    }

    @Test
    @Order(2)
    fun `nav to laptops and pc`() {
        page.waitElementLocated(By.xpath(YandexPage.LAPTOPS_AND_PC_UI_LINK_XPATH))
        driver.wait(1000)

        page.laptopsAndPcButton.click()
        driver.wait(1000)
    }

    @Test
    @Order(3)
    fun `nav to laptops and tablets link`() {
        page.waitElementLocated(By.xpath(YandexPage.LAPTOP_AND_TABLETS_LINK_XPATH))
        driver.wait(1000)

        page.laptopAndTabletsButton.click()
        driver.wait(1000)
    }

    @Test
    @Order(4)
    fun `nav to tablets link`() {
        page.waitElementLocated(By.xpath(YandexPage.TABLETS_LINK_XPATH))
        driver.wait(1000)

        page.tabletsLink.click()
        driver.wait(1000)
    }

    @Test
    @Order(5)
    fun `set filter and sort`() {
        page.samsungFilter.click()
        driver.wait(1000)

        page.cheapSortButton.click()
        driver.wait(5000)

        page.waitElementLocated(By.xpath(YandexPage.getNthProductHeaderXpath(1)))
        driver.wait(3000)
    }

    lateinit var secondProductHeader: String
    lateinit var secondProductPrice: String

    @Test
    @Order(6)
    fun `log 5 products`() {
        Thread.sleep(2000)
        for (i in 1..5) {
            var header: String
            var price: String
            if (i <= 3) {
                header = page.getNthProductHeader(i).text
                price = page.getNthProductPrice(i).text
            } else {
                header = page.getNthProductHeader(i + 1).text
                price = page.getNthProductPrice(i + 1).text
            }
            if (i == 2) {
                secondProductHeader = header
                secondProductPrice = price
            }
            println("Found: \n Name:$header \n Price:$price")

        }
        println("Remeber: \n Name:$secondProductHeader \n Price:$secondProductPrice")
    }


    @Test
    @Order(7)
    fun `search product`(){
        page.inputSearch.sendKeys(secondProductHeader)
        Thread.sleep(2000)
        page.buttonSearch.click()

        Thread.sleep(2000)
        page.waitElementLocated(By.xpath(YandexPage.getNthProductHeaderXpath(1)))
        driver.wait(3000)
        val header = page.getNthProductHeader(1).text
        assertEquals(header, secondProductHeader)

    }


    @AfterAll
    fun `close browser`() {
        driver.close()
    }

    companion object {
        const val BASE_URL = "https://market.yandex.ru/"
    }
}

class TakeScreenShotOnFailure : TestWatcher {
    override fun testFailed(e: ExtensionContext, throwable: Throwable?) {
        val testInstance = e.requiredTestInstance as TestYandex
        val screenshot = (testInstance.driver as TakesScreenshot).getScreenshotAs(OutputType.FILE)
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val destinationFile = File("screenshots/error_$timestamp.png")
        screenshot.copyTo(destinationFile, overwrite = true)
    }

    override fun testSuccessful(context: ExtensionContext?) {
        super.testSuccessful(context)
        println("All tests passed!")
    }
}