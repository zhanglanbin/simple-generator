package com.simple.generator.constant;

public enum CoulumnsKey {
	PRI("PRI","主键"),
	UNI("UNI","唯一约束"),
	MUL("MUL","可以重复");
	
	private String code;
	private String name;
	
	private CoulumnsKey(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public static String getName(String code) {
		for (CoulumnsKey coulumnsKey : CoulumnsKey.values()) {
			if (code.equals(coulumnsKey.getCode())) {
				return coulumnsKey.name;
			}
		}
		return "";
	}
	
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
}
