package me.hotsse.naverocr;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.hotsse.naverocr.ocr.vo.OcrCredentials;
import me.hotsse.naverocr.ocr.vo.OcrImageEntity;
import me.hotsse.naverocr.ocr.vo.OcrImageResponse;
import me.hotsse.naverocr.ocr.vo.OcrResponse;

@SpringBootTest
class NaverocrApplicationTests {
	
	@Test
	void contextLoads() {
		
		final String credentialPath = "C:/OcrCredentials.json";
		final String filePath = "C:/testimg11.jpg";
		ObjectMapper mapper = new ObjectMapper();
		
		OcrCredentials ocrCredentials = null;
		try {
			String json = FileUtils.readFileToString(new File(credentialPath), StandardCharsets.UTF_8);
			ocrCredentials = mapper.readValue(json, OcrCredentials.class);			
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
		
		WebClient webClient = WebClient.builder()
				.baseUrl(ocrCredentials.getBaseUrl())
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
				.defaultHeader("X-OCR-SECRET", ocrCredentials.getSecretKey())
				.build();
		
		MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
		
		Map<String, Object> messageMap = new HashMap<String, Object>();
		List<Map<String, String>> imageMaps = new ArrayList<Map<String,String>>();
		Map<String, String> imageMap = new HashMap<String, String>();
		imageMap.put("format", "jpg");
		imageMap.put("name", "tempimg");
		imageMaps.add(imageMap);
		
		messageMap.put("version", "V2");
		messageMap.put("requestId", ocrCredentials.getSecretKey());
		messageMap.put("timestamp", "1584062336793");
		messageMap.put("lang", "ko");
		messageMap.put("images", imageMaps);
		
		String message = "";
		try {			
			message = mapper.writeValueAsString(messageMap);
						
			System.out.println(message);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		bodyBuilder.part("message", message);
		bodyBuilder.part("file", new FileSystemResource(filePath));
		
		
		String result = webClient
				.post()
				.uri(ocrCredentials.getUri())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromMultipartData(bodyBuilder.build()))
				.retrieve()
		        .bodyToMono(String.class)
		        .block();
		
		System.out.println(result);
		
		try {
			
			String allText = "";
			
			OcrResponse ocrResponse = mapper.readValue(result, OcrResponse.class);
			List<OcrImageResponse> ocrImageResponses = ocrResponse.getImages();
			for(OcrImageResponse ocrImageResponse : ocrImageResponses) {
				List<OcrImageEntity> ocrImageEntities = ocrImageResponse.getFields();
				for(OcrImageEntity ocrImageEntity : ocrImageEntities) {
					allText += ocrImageEntity.getInferText();
					System.out.println(ocrImageEntity.getInferText());
				}
			}
			
			System.out.println(allText);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		/*
		ResponseEntity result = webClient.method(HttpMethod.POST)
				.uri(ocrCredentials.getUri())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromMultipartData(bodyBuilder.build()))
				.exchange()
				.flatMap(response ->
						response.bodyToMono(String.class).map(myResponse ->
								ResponseEntity.status(response.statusCode())
									.headers(response.headers().asHttpHeaders())
									.body(myResponse)
						)
				)
				.block();
		
		System.out.println(result.getBody().toString());
		*/
				
	}

}
