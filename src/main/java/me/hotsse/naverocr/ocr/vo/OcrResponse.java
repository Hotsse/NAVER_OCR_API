package me.hotsse.naverocr.ocr.vo;

import java.util.List;

import lombok.Data;

@Data
public class OcrResponse {
	
	private String version;
	private String requestId;
	private String timestamp;
	private List<OcrImageResponse> images;
}
