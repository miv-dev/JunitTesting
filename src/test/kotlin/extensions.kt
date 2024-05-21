import org.openqa.selenium.chrome.ChromeDriver
import java.time.Duration

fun ChromeDriver.wait(millis: Long) {
    manage().timeouts().implicitlyWait(Duration.ofMillis(millis))
}


