package me.hotsse.naverocr;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

public class Sha256Tests {
	
	@Test
	public void test() {
		String origin = "test1234@";
		String sha256hex = DigestUtils.sha256Hex(origin);
		String sha512hex = DigestUtils.sha512Hex(origin);
		
		System.out.println(sha256hex);
		System.out.println(sha512hex);
	}
	
}
