/*package com.automation.testcase.welcome;




import java.util.List;

import org.openqa.selenium.support.PageFactory;

import com.automation.core.util.WebDriverUtil;
import com.automation.generalpage.MessagePageImp;
import com.automation.page.login.LoginPageImp;
import com.automation.page.welcome.WelcomePageImp;

public class TestWelcomePage {
	protected  static String CHROME_DRIVER="D:\\mydrivers\\chromedriver.exe";
	public static void main(String[] args) {
		WebDriverUtil driverUtil=new WebDriverUtil("Chrome", CHROME_DRIVER);
		driverUtil.openBrowser("http://xcelerator.ninja");
		
		WelcomePageImp page = (WelcomePageImp)PageFactory.initElements(WebDriverUtil.driver, WelcomePageImp.class);
		
		LoginPageImp clickOnLoginButton = page.clickOnLoginButton();
		clickOnLoginButton.setuserName("bablaljangir");
		clickOnLoginButton.setPassword("Babu");
		
		clickOnLoginButton.clearPassword();
		clickOnLoginButton.clickOnUserNameTextBox();
		
		MessagePageImp mepage = (MessagePageImp)PageFactory.initElements(WebDriverUtil.driver, MessagePageImp.class);
		List<String> loginPageErrormessgaes = mepage.getLoginPageErrormessgaes();

		for(String a:loginPageErrormessgaes){
			System.out.println("ERROR --"+a);
		}
		
		//String text = WebDriverUtil.driver.findElement(By.tagName("body")).getText();
		
		WebDriver driver = driverUtil.getDriver();
		driver=driverUtil.driver;
		driver.get("http://xcelerator.ninja");
		WebElement findElement = driver.findElement(By.linkText("Login"));
		findElement.click();
		Thread.sleep(5000);
		
		
	}

}
*/