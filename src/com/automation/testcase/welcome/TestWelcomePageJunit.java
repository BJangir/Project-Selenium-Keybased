/*package com.automation.testcase.welcome;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.support.PageFactory;

import com.automation.core.util.WebDriverUtil;
import com.automation.generalpage.MessagePageImp;
import com.automation.page.login.LoginPageImp;
import com.automation.page.welcome.WelcomePageImp;

public class TestWelcomePageJunit {
	
	
	protected  static String CHROME_DRIVER="D:\\mydrivers\\chromedriver.exe";
static	LoginPageImp lPage;
	@BeforeClass
	public static void precodition(){
		WebDriverUtil driverUtil=new WebDriverUtil("Chrome", CHROME_DRIVER);
		driverUtil.openBrowser("http://xcelerator.ninja");
		WelcomePageImp welpage = (WelcomePageImp)PageFactory.initElements(WebDriverUtil.driver, WelcomePageImp.class);
		 lPage = welpage.clickOnLoginButton();
	}

	
	@Test
	public void testEmptyUserName(){
		lPage.setuserName("babu");
		lPage.clearUserName();
		MessagePageImp mepage = (MessagePageImp)PageFactory.initElements(WebDriverUtil.driver, MessagePageImp.class);
		List<String> loginPageErrormessgaes = mepage.getLoginPageErrormessgaes();
		for(String a:loginPageErrormessgaes){
			System.out.println("ERROR --"+a);
		}
		Assert.assertTrue(	loginPageErrormessgaes.contains("Email cannot be empty"));
	}
	
	
	
	
	
}
*/