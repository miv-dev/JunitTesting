import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebElement
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
class TestGithub {
    val driver = ChromeDriver()

    @Test
    fun `test Github`() {
        driver.get(BASE_URL)
        driver.manage().window().maximize()
        WebDriverWait(driver, Duration.ofMillis(1000))
        val element = driver.findElement(By.cssSelector("h2"))
        assert(element.text == "LambdaTest Sample App")
        assert(getTextRemaining() == "5 of 5 remaining")

        val checkbox = getNthCheckbox(1)
        var span = checkbox.findElement(By.xpath("following-sibling::*[1]"))
        assert(checkbox.getAttribute("checked").isNullOrEmpty())
        assert(span.getAttribute("class") == "done-false")

        val todoItems = driver.findElements(By.cssSelector("ul li"))
        todoItems.forEach { todoItem ->
            val checkbox = todoItem.findElement(By.cssSelector("input[type=checkbox]"));
            val spanClass = todoItem.findElement(By.cssSelector("span")).getAttribute("class");
            assertEquals(spanClass, "done-false");
            checkbox.click()
            assertEquals(todoItem.findElement(By.cssSelector("span")).getAttribute("class"), "done-true");
        }

        val newElement = "Six Item"

        driver.findElement(By.id("sampletodotext")).sendKeys(newElement)
        driver.findElement(By.id("addbutton")).click()


        val wait = WebDriverWait(driver, Duration.ofMillis(5000))
        val regex = Regex("\\d+ of \\d+ remaining")
        wait.until(ExpectedConditions.textMatches(By.className("ng-binding"), regex.toPattern()))

        val addedCheckbox = getNthCheckbox(6)
        span = addedCheckbox.findElement(By.xpath("following-sibling::*[1]"))
        assertEquals(addedCheckbox.getAttribute("checked"), null)
        assertEquals(span.getAttribute("class"), "done-false")
        assertEquals(getTextRemaining(), "1 of 6 remaining")


        addedCheckbox.click()
        assertEquals(addedCheckbox.getAttribute("checked"), "true")
        assertEquals(span.getAttribute("class"), "done-true")
        assertEquals(getTextRemaining(), "0 of 6 remaining")

    }

    private fun getTextRemaining(): String {
        return driver.findElement(By.className("ng-binding")).text
    }

    private fun getNthCheckbox(n: Int): WebElement {
        return driver.findElement(By.cssSelector("ul li:nth-child($n) input[type=checkbox]"))
    }


    companion object {
        const val BASE_URL = "https://lambdatest.github.io/sample-todo-app"
    }


    @AfterAll
    fun `close browser`() {
        driver.close()
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