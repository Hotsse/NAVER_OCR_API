package me.hotsse.naverocr;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.hotsse.naverocr.ocr.vo.OcrImageEntity;
import me.hotsse.naverocr.ocr.vo.OcrImageResponse;
import me.hotsse.naverocr.ocr.vo.OcrResponse;

public class JavaRegExTests {
	
	@Test
	public void test() {
		
		final String filePath = "C:/OcrResponse2.json";
		
		ObjectMapper mapper = new ObjectMapper();
		
		OcrResponse ocrResponse = null;
		try {
			String json = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
			ocrResponse = mapper.readValue(json, OcrResponse.class);			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		String originText = "";
		
		List<OcrImageResponse> ocrImageResponses = ocrResponse.getImages();
		for(OcrImageResponse ocrImageResponse : ocrImageResponses) {
			List<OcrImageEntity> ocrImageEntities = ocrImageResponse.getFields();
			for(OcrImageEntity ocrImageEntity : ocrImageEntities) {
				originText += ocrImageEntity.getInferText();
			}
		}
		
		System.out.println("Naver OCR API");
		
		System.out.println("---");
		
		System.out.println("originText");
		System.out.println(originText);
		
		System.out.println("---");
		
		// 신용카드 관련 정보 획득
		List<String> cardInfoText = this.fetchStringWithRegEx(originText, "[0-9||*]{4}-?[0-9||*]{4}-?[0-9||*]{4}-?[0-9||*]{4}");
		// List<String> cardInfoText = this.fetchStringWithRegEx(oneImageEntities, "[0-9||*]{4}-?[0-9||*]{4}-?[0-9||*]{4}-?[0-9||*]{4}");
		
		System.out.println("신용카드 관련 수집정보");
		for(String text : cardInfoText) {
			System.out.println(text);
		}
		
		System.out.println("---");
		
		// 금액 관련 정보 획득
		List<String> cashInfoText = this.fetchStringWithRegEx(originText, "[0-9||,]{3,}원");
		// List<String> cashInfoText = this.fetchStringWithRegEx(oneImageEntities, "[0-9||,]{3,}원");
		
		System.out.println("금액 관련 수집정보");
		for(String text : cashInfoText) {
			System.out.println(text);
		}
	}
	
	private List<String> fetchStringWithRegEx(List<OcrImageEntity> ocrImageEntities, String patternText) {
		
		List<String> result = new ArrayList<String>();
		Pattern pattern = Pattern.compile(patternText);
		
		for(OcrImageEntity ocrImageEntity : ocrImageEntities) {
			Matcher matcher = pattern.matcher(ocrImageEntity.getInferText());
			
			while(matcher.find()) {
				result.add(matcher.group(0));
				if(matcher.group(0) == null) break;
			}
		}		
		
		return result;
	}
	
	private List<String> fetchStringWithRegEx(String originText, String patternText) {
		
		List<String> result = new ArrayList<String>();
		Pattern pattern = Pattern.compile(patternText);
		Matcher matcher = pattern.matcher(originText);
		
		while(matcher.find()) {
			result.add(matcher.group(0));
			if(matcher.group(0) == null) break;
		}
		
		return result;
	}
	
	

}
