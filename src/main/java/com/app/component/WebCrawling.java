package com.app.component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import com.app.dto.StoreDTO;
import com.app.dto.crawling.KageDTO;
import com.app.dto.crawling.MenuDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebCrawling {
	
	private WebDriver driver;
	private final String root_url="https://map.naver.com/p/search/";
	
	public KageDTO process(StoreDTO storeInfo){
		driver = new ChromeDriver();
		KageDTO kagedle = null;
		try {
			
			kagedle = getStore(storeInfo.getAreaNm(), storeInfo.getTitle());
						
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			driver.close();
			driver.quit();
		}
		
		return kagedle;
	}
	
	private KageDTO getStore(String areaNm, String storeNm) throws InterruptedException {
//		String areaNm = "강남구";
//		String storeNm = "남도계절음식 무돌";
		System.setProperty("webdriver.chrome.driver", "C:\\IDE\\chromedriver-win64\\chromedriver.exe");
		

		List<MenuDTO> storeMenu = new ArrayList<>();
		List<String> storeImage = new ArrayList<>();
	
		driver.get(root_url + areaNm + " " + storeNm);
		Thread.sleep(1000);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
		
		if(driver.findElements(By.cssSelector("iframe#entryIframe")).size() == 0) {
			//검색 리스트로 열리면
			driver.switchTo().frame(driver.findElement(By.cssSelector("iframe#searchIframe")));
			List<WebElement> storeList = driver.findElements(By.cssSelector("#_pcmap_list_scroll_container ul li"));
//				log.info("검색 개수: {}", storeList.size());
			boolean isStore = false;
			for(WebElement li : storeList) {
				//데이터 베이스의 가게 이름과 똑같은지 
				if(li.findElements(By.className("YwYLL")).size() > 0) {
					if(storeNm.equals(li.findElement(By.className("YwYLL")).getText())) {
						li.findElement(By.className("P7gyV")).click();;
						isStore = true;
						break;
					}
				}else if(li.findElements(By.className("TYaxT")).size() > 0) {
					if(storeNm.equals(li.findElement(By.className("TYaxT")).getText())) {
						li.findElement(By.className("tzwk0")).click();;
						isStore = true;
						break;
					}
				}						
			}
			//일치하는 가게 정보가 없으면 null 반환
			if(!isStore) return KageDTO.builder()
								.storeNm(storeNm)
								.areaNm(areaNm)
								.menuItems(storeMenu)
								.imgURLs(storeImage)
								.build();;
			driver.switchTo().defaultContent();
		}
		//가게 정보로 들어가기
		Thread.sleep(2000);
		driver.switchTo().frame(driver.findElement(By.cssSelector("iframe#entryIframe")));
		List<WebElement> tabList = driver.findElements(By.cssSelector("[role='tab']"));
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
		if(indexOfMenu > -1) {
			tabList.get(indexOfMenu).click();
						
			List<WebElement> categoryList = driver.findElements(By.className("gkWf3"));
			List<WebElement> tableList = driver.findElements(By.className("order_list_inner"));
			int numOfmenu = 0;
			
			if(categoryList.size() > 0) {
				//카테고리별로 정리된 경우
				for(WebElement category : categoryList) {
					//카테고리 이름
					String category_title = "";
					if(category.findElements(By.className("place_section_header_title")).size() > 0)
						category_title = category.findElement(By.className("place_section_header_title")).getText();
					//메뉴 담기
					Thread.sleep(1000);
					List<MenuDTO> menuTable = getMenu(category, category_title);
					numOfmenu += menuTable.size();
					if(numOfmenu > 70) break;
					storeMenu.addAll(menuTable);
				}
			}else if(tableList.size() > 0){
				//네이버 주문
				
				for(WebElement table : tableList) {
					
//					List<MenuDTO> menuTable = new ArrayList<>();
					
					//더보기 버튼 다 누르기
					while(table.findElements(By.className("fvwqf")).size() > 0) {
						table.findElement(By.className("fvwqf")).click();
					}
					//카테고리
					String category ="";
					if(table.findElements(By.className("title")).size() > 0)
						category = table.findElement(By.className("title")).getText();
					//메뉴 리스트
					List<WebElement> menuList = table.findElements(By.className("order_list_item"));
					numOfmenu += menuList.size();
					if(numOfmenu > 70) break;
					
					for(WebElement li : menuList) {
						Thread.sleep(1000);
						MenuDTO menu = new MenuDTO();
						//카테고리
						if(!"".equals(category) && category != null)
							menu.setCategory(category);
						//이름
						if(li.findElements(By.className("tit")).size() > 0) {
							WebElement class_text = li.findElement(By.className("tit"));
							String full_text = class_text.getText();
							String trash_text = "";
							if(class_text.findElements(By.className("ico_group")).size() > 0)
								trash_text = class_text.findElement(By.className("ico_group")).getText();
							if("".equals(trash_text)) {
								menu.setName(full_text);
							}else {
								menu.setName(full_text.substring(0, full_text.indexOf(trash_text)));							
							}
						}
						//가격
						if(li.findElements(By.className("price")).size() > 0)
							menu.setPrice(li.findElement(By.className("price")).getText());
						//설명
						if(li.findElements(By.className("detail_txt")).size() > 0)
							menu.setDescription(li.findElement(By.className("detail_txt")).getText());
						//이미지 주소
						if(li.findElements(By.tagName("img")).size() > 0) {
							String src = li.findElement(By.tagName("img")).getAttribute("src");
							if(!"https://g-place.pstatic.net/assets/shared/images/menu_icon_noimg_food.png".equals(src))
								menu.setImage(src);
						}
						log.info("메뉴: {}", menu.toString());
						storeMenu.add(menu);
					}
					
				}
				
			}else {
				if(driver.findElements(By.cssSelector("[data-nclicks-area-code='bmv']")).size() > 0) {
					WebElement table = driver.findElement(By.cssSelector("[data-nclicks-area-code='bmv']"));
					//메뉴 담기
					Thread.sleep(1000);				
					storeMenu.addAll(getMenu(table, ""));
				}
			}
		}
		//사진탭 클릭
		if(driver.findElements(By.cssSelector("[role='tab']")).size() < 1) {
			driver.navigate().back();
			Thread.sleep(3000);
		}
		
		if(indexOfPhoto > -1) {
//			메뉴 탭이 많아서 안 보일 때
			if(indexOfPhoto - indexOfMenu > 2) {
				if(indexOfPhoto - indexOfMenu > 3) {
					tabList = driver.findElements(By.cssSelector("[role='tab']"));
					tabList.get(indexOfPhoto - 2).click();
					Thread.sleep(1000);
				}
				tabList = driver.findElements(By.cssSelector("[role='tab']"));
				tabList.get(indexOfPhoto - 1).click();
				Thread.sleep(1000);
			}
			tabList = driver.findElements(By.cssSelector("[role='tab']"));
			tabList.get(indexOfPhoto).click();
			
			List<WebElement> photoCategory = driver.findElements(By.className("Me4yK"));
			
			if(photoCategory.size() > 0) {
				for(WebElement category : photoCategory) {
					if(category.findElement(By.tagName("span")).getText().contains("업체")) {
						category.findElement(By.tagName("a")).click();
						Thread.sleep(1000);
						WebElement photoTable = driver.findElement(By.className("Nd2nM"));
						List<WebElement> photos = photoTable.findElements(By.tagName("img"));
						for(WebElement photo : photos) {
							storeImage.add(photo.getAttribute("src"));
							log.info("사진: {}", photo.getAttribute("src"));
						}
						break;
					}
				}
			}
		}		
		return KageDTO.builder()
				.storeNm(storeNm)
				.areaNm(areaNm)
				.menuItems(storeMenu)
				.imgURLs(storeImage)
				.build();
	}
	
	private List<MenuDTO> getMenu(WebElement table, String category) {
		
		List<MenuDTO> storeMenu = new ArrayList<>();
		
		//더보기 버튼 다 누르기
		while(table.findElements(By.className("fvwqf")).size() > 0) {
			table.findElement(By.className("fvwqf")).click();
		}		
		
		//메뉴 리스트
		List<WebElement> menuList = table.findElements(By.className("E2jtL"));
		
		for(WebElement li : menuList) {
			
			MenuDTO menu = new MenuDTO();
			//카테고리
			if(!"".equals(category) && category != null)
				menu.setCategory(category);
			//이름
			if(li.findElements(By.className("lPzHi")).size() > 0)
				menu.setName(li.findElement(By.className("lPzHi")).getText());
			//가격
			if(li.findElements(By.className("GXS1X")).size() > 0)
				menu.setPrice(li.findElement(By.className("GXS1X")).getText());
			//설명
			if(li.findElements(By.className("kPogF")).size() > 0)
				menu.setDescription(li.findElement(By.className("kPogF")).getText());
			//이미지 주소
			if(li.findElements(By.tagName("img")).size() > 0) {
				String src = li.findElement(By.tagName("img")).getAttribute("src");
				if(!"https://g-place.pstatic.net/assets/shared/images/menu_icon_noimg_food.png".equals(src))
					menu.setImage(src);
			}
			log.info("메뉴: {}", menu.toString());
			storeMenu.add(menu);
		}
		
		return storeMenu;
	}
	
}
