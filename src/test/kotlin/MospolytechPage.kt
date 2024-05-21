import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory

// page_url = https://mospolytech.ru/
class MospolytechPage(private val driver: WebDriver) {
    @FindBy(css = "a[title='Расписание']")
    lateinit var linkSchedulesButton: WebElement

    @FindBy(css = "a[href='https://rasp.dmami.ru/']")
    lateinit var linkTextButton: WebElement


    init {
        PageFactory.initElements(driver, this)
    }


    fun switchToNextTab() {
        driver.switchTo().window(driver.windowHandles.last())
    }

}

// page_url = https://rasp.dmami.ru/
class RaspPage(private val driver: WebDriver) {
    @FindBy(xpath = "//input[@class='groups']")
    lateinit var inputSearch: WebElement


    init {
        PageFactory.initElements(driver, this)
    }

    fun clickOnGroup() {
        driver.findElement(By.xpath("//div[@id=\"221-322\"]")).click()
    }

    fun getDate(): WebElement {
        return driver.findElement(By.xpath("//div[contains(@class, \"schedule-day_today\")]/div[contains(@class, \"schedule-day__title\")]"))
    }

    fun searchGroup(group: String) {

        inputSearch.sendKeys(group)
    }

}