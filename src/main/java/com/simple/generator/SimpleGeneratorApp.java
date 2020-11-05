package com.simple.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SimpleGeneratorApp {
	public static void main(String[] args) {
		try {
			SpringApplication sa = new SpringApplication(SimpleGeneratorApp.class);
			sa.setWebApplicationType(WebApplicationType.NONE);
	        sa.run(args);
			// 必须try catch 不然启动失败不知道原因
		} catch (Exception e) {
			if (!(e.getStackTrace() != null && e.getStackTrace().length <= 16
					&& "org.springframework.boot.devtools.restart.SilentExitExceptionHandler"
							.equals(e.getStackTrace()[0].getClassName()))) {
				e.printStackTrace();
			}
		}
	}
}
