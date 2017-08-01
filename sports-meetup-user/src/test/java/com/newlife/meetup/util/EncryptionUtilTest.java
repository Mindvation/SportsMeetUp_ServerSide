package com.newlife.meetup.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncryptionUtilTest {

	@Test
	public void testGetEncryptString() {
		String ciphertext = EncryptionUtil.getEncryptString("257258");
		System.out.println("=== ciphertext  "+ ciphertext);
	}

	@Test
	public void testGetDecryptString() {
		fail("Not yet implemented");
	}

}
