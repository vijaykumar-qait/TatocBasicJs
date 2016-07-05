package tatocBasic;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class TatocBasic {

	public static void main(String[] args) throws InterruptedException {
		//create a new instance of fireFox
		GeneralActions actions= new GeneralActions();
		WebDriver webdriver= actions.getDriver(Utility.getConfigValue("browser"));
		actions.setDriver(webdriver);
		actions.getURL(Utility.getConfigValue("url"));
//--------------------------------------------------------------------------------------------------		
		//go to tatoc page
		webdriver.findElement(By.partialLinkText("tatoc")).click();
		webdriver.findElement(By.linkText("Basic Course")).click();
//--------------------------------------------------------------------------------------------------		
		//
		JavascriptExecutor js;
		if(webdriver instanceof JavascriptExecutor){
			js = (JavascriptExecutor)webdriver;			
		}
		else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
//--------------------------------------------------------------------------------------------------		
		//1 Grid Gate
		js.executeScript("document.getElementsByClassName(\"greenbox\")[0].click();");
//--------------------------------------------------------------------------------------------------
		//Frame Dungeon
		String color1, color2;
		do{
			color1 = (String) js.executeScript("return document.getElementById(\"main\").contentWindow.document.getElementById(\"answer\").className;");
			color2 = (String) js.executeScript("return document.getElementById(\"main\").contentWindow.document.getElementById(\"child\").contentWindow.document.getElementById(\"answer\").className;");
			if(color1.equals(color2))
				break;
			js.executeScript("$(\"#main\").contents().find(\"a:contains(Repaint Box 2)\").click();");
			Thread.sleep(200);
		}while(true);
		js.executeScript("$(\"#main\").contents().find(\"a:contains(Proceed)\").click();");
//---------------------------------------------------------------------------------------------------
		//3 Drag Around
		webdriver.get("http://10.0.1.86/tatoc/basic/drag");
		js.executeScript("document.getElementById(\"dropbox\").appendChild(document.getElementById(\"dragbox\"));"+
				"var d=document.getElementById(\"dragbox\");"+
				"d.style.position = \"absolute\";"+
				"d.style.left = document.getElementById(\"dropbox\").getBoundingClientRect().left;"+
				"$(\"a:contains(Proceed)\")[0].click();");	
		//--------------------------------------------------------------------------------------------------
		//4 Popup Windows
		String parent_window = webdriver.getWindowHandle();
		//driver.findElement(By.partialLinkText("Launch Popup Window")).click();
		js.executeScript("$(\"a:contains(Launch Popup Window)\").click();");
		for(String handle:webdriver.getWindowHandles()){
			webdriver.switchTo().window(handle);
		}
		js.executeScript("document.getElementById(\"name\").value=\"vijay kumar\";"+
				"document.getElementById(\"submit\").click();");
		webdriver.switchTo().window(parent_window);
		js.executeScript("$(\"a:contains(Proceed)\").click();");
		//--------------------------------------------------------------------------------------------------
		//5 Cookie Handling
		js.executeScript("$(\"a:contains(Generate Token)\").click();"+
				"var token = document.getElementById(\"token\").innerText.split(\": \")[1];"+
				"\"Token=\"+token;"+
				"document.cookie=\"Token=\"+token;"+
				"$(\"a:contains(Proceed)\").click();");
//--------------------------------------------------------------------------------------------------
		//closing browser
		webdriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		webdriver.close();
	}
}