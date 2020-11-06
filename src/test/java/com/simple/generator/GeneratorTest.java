package com.simple.generator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.simple.generator.utils.GeneratorUtils;

@RunWith(SpringRunner.class)
public class GeneratorTest {
	
	@Test
	public void run() {
		String path = GeneratorUtils.getThisProjectPath();
		System.out.println(path);
	}
}
