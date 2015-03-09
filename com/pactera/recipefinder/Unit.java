package com.pactera.recipefinder;

public enum Unit {
	    of(""),
	    grams("grams"),
	    ml("ml"),
	    slices("slices");
	    
	    private final String unitOfMeasure;

	    private Unit(String unitOfMeasure) {
	        this.unitOfMeasure = unitOfMeasure;
	    }
}

