package com.simple.generator.constant;


public enum CoulumnsNull {
	NO("NO", 0, "不能是null"), YES("YES", 1, "可以是null");

	private String code;
	private int state;
	private String explain;

	CoulumnsNull(String code, int state, String explain) {
		this.code = code;
		this.state = state;
		this.explain = explain;
	}

	public static int getState(String code) {
		for (CoulumnsNull coulumnsNull : CoulumnsNull.values()) {
			if (code.equals(coulumnsNull.getCode())) {
				return coulumnsNull.state;
			}
		}
		return 0;
	}

	public static String getExplain(String code) {
		for (CoulumnsNull coulumnsNull : CoulumnsNull.values()) {
			if (code.equals(coulumnsNull.getCode())) {
				return coulumnsNull.explain;
			}
		}
		return "";
	}

	public String getCode() {
		return code;
	}

	public int getState() {
		return state;
	}

	public String getExplain() {
		return explain;
	}
}
