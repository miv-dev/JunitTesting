import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TakeScreenShotOnFailure : TestWatcher {
    override fun testFailed(e: ExtensionContext, throwable: Throwable?) {
        val testInstance = e.requiredTestInstance as BaseTestInstance
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