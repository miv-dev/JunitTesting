import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.FindBys
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class GithubPage(private val driver: WebDriver) {

    @FindBy(css = "h2")
    lateinit var titleSelector: WebElement

    @FindBy(className = "ng-binding")
    lateinit var remainingSelector: WebElement

    @FindBys(FindBy(css = "ul li"))
    lateinit var todoItemsSelector: MutableList<WebElement>

    @FindBy(id = "sampletodotext")
    lateinit var inputSampletodotext: WebElement

    @FindBy(id = "addbutton")
    lateinit var inputAddbutton: WebElement


    fun getNthCheckbox(n: Int): WebElement {
        return driver.findElement(By.cssSelector("ul li:nth-child($n) input[type=checkbox]"))
    }


    fun addNewItem(text: String) {
        inputSampletodotext.sendKeys(text)
        inputAddbutton.click()
    }


    fun findCheckbox(webElement: WebElement): WebElement {
        return webElement.findElement(By.cssSelector("input[type=checkbox]"))
    }

    fun getCheckboxSpan(webElement: WebElement): WebElement {
        return webElement.findElement(By.cssSelector("span"))
    }

    fun getSpanClass(webElement: WebElement): String {
        return webElement.findElement(By.xpath("following-sibling::*[1]")).getAttribute("class")

    }

    init {
        PageFactory.initElements(driver, this)
    }

    fun open() {
        driver.manage().window().maximize()
        WebDriverWait(driver, Duration.ofMillis(1000))
    }


}