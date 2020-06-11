package me.hotsse.naverocr.ocr.vo;

import lombok.Data;

@Data
public class OcrImageEntity {
	
	private String valueType;
	private String inferText;
	private double inferConfidence;
	private String type;
	private Object boundingPoly;

}
