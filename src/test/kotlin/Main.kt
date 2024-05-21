import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.chrome.ChromeDriver
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(TakeScreenShotOnFailure::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation::class)
class TestGithub {
    val driver = ChromeDriver()
    lateinit var mospolytechPage: MospolytechPage
    lateinit var raspPage: RaspPage

    @BeforeAll
    fun `open browser`() {
        driver.get(BASE_URL)
        mospolytechPage = MospolytechPage(driver)
    }


    @Test
    @Order(1)
    fun `found 221-322 group schedule`() {
        mospolytechPage.linkSchedulesButton.click()
        mospolytechPage.linkTextButton.click()
        mospolytechPage.switchToNextTab()
        raspPage = RaspPage(driver)
        raspPage.searchGroup("221-322")
        driver.wait(2000)
        raspPage.clickOnGroup()
        driver.wait(3000)
    }

    @Test
    @Order(2)
    fun `compare days`(){
        val weekDayOnPage = raspPage.getDate()
        val systemWeekDay = SimpleDateFormat("EEEE", Locale("ru")).format(Date())
        assertEquals(systemWeekDay, weekDayOnPage.text.lowercase())
    }


    @AfterAll
    fun `close browser`() {
        driver.close()
    }

    companion object {
        const val BASE_URL = "https://mospolytech.ru/"
    }
}

class TakeScreenShotOnFailure : TestWatcher {
    override fun testFailed(e: ExtensionContext, throwable: Throwable?) {
        val testInstance = e.requiredTestInstance as TestGithub
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