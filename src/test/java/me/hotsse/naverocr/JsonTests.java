package me.hotsse.naverocr;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.hotsse.naverocr.ocr.vo.OcrCredentials;

public class JsonTests {
	
	@Test
	public void test() {
		
		final String credentialPath = "C:/OcrCredentials.json";
		ObjectMapper mapper = new ObjectMapper();
		
		OcrCredentials ocrCredentials = null;
		try {
			String json = FileUtils.readFileToString(new File(credentialPath), StandardCharsets.UTF_8);
			ocrCredentials = mapper.readValue(json, OcrCredentials.class);
			System.out.println(ocrCredentials.toString());
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
	}

}
