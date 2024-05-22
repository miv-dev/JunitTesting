import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import java.time.Duration

fun WebDriver.wait(millis: Long) = manage().timeouts().implicitlyWait(Duration.ofMillis(millis))