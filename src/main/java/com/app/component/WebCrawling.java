package com.app.component;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebCrawling {
	
	private WebDriver driver;
	private final String root_url="https://map.naver.com/p/search/";
	public Map<String, Object> getImages() {
		System.setProperty("webdriver.chrome.driver", "C:\\IDE\\chromedriver-win64\\chromedriver.exe");
		driver = new ChromeDriver();
		try {
			
		}finally {
			driver.quit();
		}
//		driver.get(root_url + "관악구 24시옛날집");
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
//		
//		WebElement test = 
		
		return "TESTING…";
	}
	
}
