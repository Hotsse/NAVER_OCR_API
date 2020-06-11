package me.hotsse.naverocr.ocr.vo;

import java.util.List;

import lombok.Data;

@Data
public class OcrImageResponse {

	private String uid;
	private String name;
	private String inferResult;
	private String message;
	private List<OcrImageEntity> fields;
	private Object validationResult;
}
