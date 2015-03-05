package com.pactera.recipefinder;

public enum Unit {
	    OF(""),
	    GRAMS("grams"),
	    ML("ml"),
	    SLICES("slices");
	    
	    private final String unitOfMeasure;

	    private Unit(String unitOfMeasure) {
	        this.unitOfMeasure = unitOfMeasure;
	    }
}
