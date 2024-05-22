import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

// page_url = https://market.yandex.ru/catalog--planshety/26908970/list?hid=6427100&rs=eJx9UE1oE0EU3qmBbqcIsRdrS8uAB_dkd3a3MYs_DfWg7UHUo5fNZLNrF9ImZhLFW7ypPfRXoVrKCgpePRQErUZKwR8Q7F0JWA8GQRB7KBTqvufSgNhePh7f-37ejDF94CJZIwq7djrCzB3AkL-MsLa0AngXMGwCrxw8BcpLyPx4AWidiTD7DvjGR3Ap0ydB8wiY0Me0BGCjjAkXANkc5s9DjtIOTP0reueQmcWWJdQvAtY2sasfttmzmJB_3WJWQZ_ZAU19EnPeY-ZNvD9E7wdgst9BqcxjZh9qhlqvZufw8tt48yHwZrZBn1Vgrj_A7X1Iy4xgSxPfuAxz49grmB_i5VPoOg9YG4VteAUSGr2IhzG_C_9wHdtnV9ZJp_rs18y9JwuLnybJ8Jc2eoSqKkmSbsJIsq2nPe_5olqoMEVTKMdVJ66SPb2ukFVRcFxRLlalV3BMJ6h449KpFEtsYzXQPi8X6XG0qLGlOydk4LYcpYIIJlA_1ezQtjYkTcUVlNFIz_aqkJ4ou2OOgT23fo9QM-756-v7b8-u6ef2mLb2fJhacVmCJSJT__5leOKbpzeoja6uuOrov66yJ0vFCRlc91qFj99e1b4tyCQZVV3fN8x0LnfZ4Cd4ykyb6UHb1vUBl-c514XtDZo691OWq6e5ZbmGLritpyIBH-B_AJz2CtI%2C&recipeId=1180817608&glfilter=7893318%3A153061
class YandexPage(private val driver: WebDriver) {

    @FindBy(xpath = CATALOG_BUTTON_XPATH)
    lateinit var catalogButton: WebElement

    @FindBy(xpath = LAPTOPS_AND_PC_BUTTON_XPATH)
    lateinit var laptopsAndPcButton: WebElement

    @FindBy(xpath = LAPTOP_AND_TABLETS_LINK_XPATH)
    lateinit var laptopAndTabletsButton: WebElement

    @FindBy(xpath = TABLETS_LINK_XPATH)
    lateinit var tabletsLink: WebElement

    @FindBy(xpath = "//div[@data-filter-value-id=\"153061\"]/label")
    lateinit var samsungFilter: WebElement

    @FindBy(xpath = "//button[text()= 'подешевле']")
    lateinit var cheapSortButton: WebElement



    @FindBy(xpath = "//*[@id='header-search']")
    lateinit var inputSearch: WebElement
    @FindBy(xpath = "//*[@id='header-search']/../../../../../button")
    lateinit var buttonSearch: WebElement
    
    
    init {
        PageFactory.initElements(driver, this)
    }


    fun waitElementLocated(locator: By) {
        val wait = WebDriverWait(driver, Duration.ofSeconds(20))
        wait.until(ExpectedConditions.elementToBeClickable(locator))
    }

    fun getNthProductHeader(n: Int): WebElement {
        return driver.findElement(By.xpath(getNthProductHeaderXpath(n)))
    }

    fun getNthProductPrice(n: Int): WebElement {
        return driver.findElement(By.xpath(getNthProductPriceXpath(n)))
    }


    companion object {

        fun getNthProductHeaderXpath(n: Int) =
            "//div[@data-auto=\"SerpList\"]/div[@data-apiary-widget-name=\"@marketfront/SerpEntity\" and position() = ${n + 1}]//h3"

        fun getNthProductPriceXpath(n: Int) =
            "//div[@data-auto=\"SerpList\"]/div[@data-apiary-widget-name=\"@marketfront/SerpEntity\" and position() = ${n + 1}]//div[@data-baobab-name=\"price\"]//span[@data-auto=\"snippet-price-current\"]/span[@class=\"_1ArMm\"]"

        const val TABLETS_LINK_XPATH =
            "//div[@data-baobab-name=\"new-category-snippet\"]//h3/following-sibling::div//div[text() = \"Планшеты\"]/ancestor::a"
        const val LAPTOP_AND_TABLETS_LINK_XPATH =
            "//a[text() = \"Ноутбуки и планшеты\"]"
        const val CATALOG_BUTTON_XPATH = "//button/span[text()=\"Каталог\"]/.."
        const val LAPTOPS_AND_PC_UI_LINK_XPATH =
            "//li/a//span[text() = 'Ноутбуки и компьютеры']/../../../li[position() = 5]"
        const val LAPTOPS_AND_PC_BUTTON_XPATH = "//li/a//span[text() = 'Ноутбуки и компьютеры']/../.."


    }
}