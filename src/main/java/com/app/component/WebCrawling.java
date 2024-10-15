package com.app.component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import com.app.dto.crawling.KageDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebCrawling {
	
	private WebDriver driver;
	private final String root_url="https://map.naver.com/p/search/";
	
	public List<KageDTO> process(){
		List<KageDTO> kagedle = new ArrayList<>();
		
		return kagedle;
	}
	
	public KageDTO getImageAndMenu() {
		String areaNm = "강남구";
		String storeNm = "청담주문진항";
		System.setProperty("webdriver.chrome.driver", "C:\\IDE\\chromedriver-win64\\chromedriver.exe");
		driver = new ChromeDriver();
		
		KageDTO store = new KageDTO();
		try {
			driver.get(root_url + areaNm + " " + storeNm);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			
			if(driver.findElements(By.cssSelector("iframe#searchIframe")).size() > 1) {
				//검색 리스트로 열리면
				driver.switchTo().frame(driver.findElement(By.cssSelector("iframe#searchIframe")));
				List<WebElement> storeList = driver.findElements(By.cssSelector("#_pcmap_list_scroll_container ul li"));
//				log.info("검색 개수: {}", storeList.size());
				boolean isStore = false;
				for(WebElement li : storeList) {
					//데이터 베이스의 가게 이름과 똑같은지 
					if(storeNm.equals(li.findElement(By.className("YwYLL")).getText())) {
						li.findElement(By.className("P7gyV")).click();;
						isStore = true;
						break;
					}
				}
				//일치하는 가게 정보가 없으면 null 반환
				if(!isStore) return null;
				driver.switchTo().defaultContent();
			}
			//가게 정보로 들어가기
			driver.switchTo().frame(driver.findElement(By.cssSelector("iframe#entryIframe")));
			List<WebElement> tabList = driver.findElements(By.className("_tab-menu"));
			
			log.info("탭 개수: {}", tabList.size());
			int indexOfMenu = -1;
			int indexOfPhoto = -1;
			for(int i = 0; i < tabList.size(); i++) {
				//메뉴 찾기
				if(tabList.get(i).getAttribute("href").contains("menu")) indexOfMenu = i;
				//사진 찾기
				if(tabList.get(i).getAttribute("href").contains("photo")) indexOfPhoto = i;				
			}
			//메뉴탭 클릭
			tabList.get(indexOfMenu).click();
						
			List<WebElement> menuList = driver.findElements(By.className("gkWf3"));
			if(menuList.size() > 0) {
				
			}else {
				//더보기 버튼 다 누르기
				while(driver.findElements(By.className("fvwqf")).size() > 0) {
					driver.findElement(By.className("fvwqf")).click();
				}
				menuList = driver.findElements(By.className("E2jtL"));
				for(WebElement menu : menuList) {
					log.info("메뉴 이름: {}", menu.findElement(By.className("lPzHi")).getText());
					log.info("메뉴 가격: {}", menu.findElement(By.className("GXS1X")).getText());
					//log.info("메뉴 설명: {}", menu.findElement(By.className("lPzHi")).getText());
					log.info("이미지 주소: {}", menu.findElement(By.tagName("img")).getAttribute("src"));
				}
			}
			
			//tabList.get(4).click();
		}finally {
			//driver.quit();
		}
		
		return store;
	}
	
}
