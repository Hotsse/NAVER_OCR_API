package me.hotsse.naverocr.core.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class ImageUtils {

	/**
	 * 이미지 파일 압축
	 * 
	 * @param inputImage 원본파일
	 * @param imageType 이미지 색상 포맷
	 * @param resizable 이미지 사이즈 압축 실행여부
	 * @return 압축된 jpeg 파일
	 */
	public static byte[] resizeImage(byte[] inputImage, int imageType, boolean resizable) throws Exception {
		
		byte[] result = null;
		
        try {
        	
        	BufferedImage in = ImageIO.read(new ByteArrayInputStream(inputImage));
    		
    		int width = in.getWidth(null);  
            int height = in.getHeight(null);
            
            if(resizable) { // 이미지 사이즈 압축
            	double ratio = Math.min((double)width / (double)height, 1);
    			width = (int)(width * ratio);
    			height = (int)(height * ratio);
            }
            
			BufferedImage outImage = new BufferedImage(width, height, imageType);
			Graphics2D g = outImage.createGraphics();
			g.drawImage(in, 0, 0, width, height, null);
			
			ByteArrayOutputStream output = new ByteArrayOutputStream();			
			ImageIO.write(outImage, "jpg", output);
			result = output.toByteArray();
		}
		catch(Exception e){
			e.printStackTrace();
		}
        
        return result;
	}
	
	/**
	 * PDF > jpeg 이미지 변환
	 * 
	 * @param file PDF 원본파일
	 * @param imageType 이미지 색상포맷
	 * @return jpeg 파일
	 * @throws Exception
	 */
	public static byte[] convertPdfToJpeg(byte[] file, ImageType imageType) throws Exception {
		
		PDDocument document = null;
		byte[] result = null;
		
		try {
			
			document = PDDocument.load(file);
			
			if(document.getNumberOfPages() != 1) {
				throw new RuntimeException("2개 이상의 이미지로 이루어진 PDF 파일입니다.");
			}
			
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			BufferedImage outImage = pdfRenderer.renderImageWithDPI(0, 300, imageType);
			ByteArrayOutputStream output = new ByteArrayOutputStream();			
			ImageIO.write(outImage, "jpg", output);
			result = output.toByteArray();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			
			if(document != null) {
				document.close();
				document = null;
			}
		}
		
		return result;
	}
}
