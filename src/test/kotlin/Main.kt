import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@ExtendWith(TakeScreenShotOnFailure::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestGithub {
    val driver = ChromeDriver()
    private lateinit var githubPage: GithubPage

    @BeforeTest
    fun `open browser`() {
        driver.get(BASE_URL)
        githubPage = GithubPage(driver)
        githubPage.open()
    }


    @Test
    fun `should have correct page title`() {
        assertEquals(githubPage.titleSelector.text, "LambdaTest Sample App")
    }

    @Test
    fun `should display correct remaining text`() {
        assertEquals(githubPage.remainingSelector.text, "5 of 5 remaining")
    }

    @Test
    fun `should toggle checkboxes correctly`() {
        githubPage.todoItemsSelector.forEach { todo ->
            githubPage.getCheckboxSpan(todo)
                .getAttribute("class")
                .also {
                    assertEquals(it, "done-false")
                }
            githubPage.findCheckbox(todo).click()
            githubPage.getCheckboxSpan(todo).getAttribute("class").also {
                assertEquals(it, "done-true")
            }
        }
    }

    @Test
    fun `should add new item correctly`() {
        val newItem = "Six Item"
        githubPage.addNewItem(newItem)
        WebDriverWait(driver, Duration.ofMillis(5000))
            .until {
                return@until githubPage.remainingSelector.text == "6 of 6 remaining"
            }

        val addedCheckbox = githubPage.getNthCheckbox(6)
        assertEquals(addedCheckbox.getAttribute("checked"), null)
        githubPage.getSpanClass(addedCheckbox).also {
            assertEquals(it, "done-false")
        }
        assertEquals(githubPage.remainingSelector.text, "6 of 6 remaining")
        addedCheckbox.click()

        assertEquals(addedCheckbox.getAttribute("checked"), "true")
        githubPage.getSpanClass(addedCheckbox).also {
            assertEquals(it, "done-true")
        }
        assertEquals(githubPage.remainingSelector.text, "5 of 6 remaining")
    }


    @AfterAll
    fun `close browser`() {
        driver.close()
    }

    companion object {
        const val BASE_URL = "https://lambdatest.github.io/sample-todo-app"
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