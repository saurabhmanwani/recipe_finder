package com.pactera.recipefinder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class RecipeFinder {

	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter recipe json file path");
		String jsonFile = sc.next();
		System.out.println("Enter fridge items csv file path");
		String csvFile = sc.next();
		
		sc.close();
		
		RecipeFinder recipeFinder = new RecipeFinder();
		recipeFinder.startRecipeFinderExecution(jsonFile, csvFile);		
	}
	
	/*
	 * 
	 * 
	 * 
	 */
	private void startRecipeFinderExecution(String jsonFile, String csvFile) {
		
		// parse json file
	//	jsonFile = "/Users/saurabhmanwani/Desktop/recipeJson.txt";
		String recipe = parseRecipeJSONFile(jsonFile);
		JSONArray recipeArray =  new JSONArray(recipe);
		
		// parse csv file
	//	csvFile = "/Users/saurabhmanwani/Desktop/fridgeItemsCsv.csv";
		Map<String,List<Object>> fridgeItems = parseFridgeItemsCSVFile(csvFile);
		
		// run recipe finder
		runRecipeFinder(recipeArray, fridgeItems);
	}
	
	/*
	 * 
	 * 
	 * 
	 */
	private String parseRecipeJSONFile(String jsonFile) {

		StringBuilder jsonFileString = new StringBuilder();
		FileInputStream fileInput = null;
		try {			 		
			fileInput = new FileInputStream(jsonFile);
			int r;
			while ((r = fileInput.read()) != -1) {
			   char lineCharacter = (char) r;
			   // append to StringBuilder
			   jsonFileString.append(lineCharacter);
			}
			fileInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInput != null) {
				try {
					fileInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonFileString.toString();
	}
	
	/*
	 * 
	 * 
	 * 
	 */
	private Map<String, List<Object>> parseFridgeItemsCSVFile(String csvFile){
	
		Map<String,List<Object>> fridgeItems = new HashMap<String,List<Object>>();
		BufferedReader br = null;
		String line = "";
		String splitBy = ",";
		try {			 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
			    // use comma as separator
				String[] csvLine = line.split(splitBy);
	 
				// populate fridge items
				poplutateFridgeItems(fridgeItems, csvLine);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fridgeItems;
	}
	
	/*
	 * 
	 * 
	 * 
	 */
	private void poplutateFridgeItems(Map<String,List<Object>> fridgeItems, String[] csvLine){
		
		List<Object> csvLineVals = new ArrayList<Object>();
		for(int count=1;count<csvLine.length;count++) {
			csvLineVals.add(csvLine[count]);
		}
		fridgeItems.put(csvLine[0], csvLineVals);
	}
	
	/*
	 * 
	 * 
	 * 
	 */
	private void runRecipeFinder(JSONArray recipeArray, Map<String,List<Object>> fridgeItems) {
		
		String recipeFound = "Order Takeout";
		
		for(int recipeCount=0;recipeCount<recipeArray.length();recipeCount++){
			
			JSONObject currentRecipe = (JSONObject)recipeArray.get(recipeCount);
			JSONArray currentRecipeIngredients = (JSONArray)currentRecipe.get("ingredients");
			
			for(int ingredientsCount=0;ingredientsCount<currentRecipeIngredients.length();ingredientsCount++){
				
				JSONObject currentIngredient = (JSONObject)currentRecipeIngredients.get(ingredientsCount);
				
				if(fridgeItems.containsKey(currentIngredient.get("item"))) {
					
					String itemName = (String)currentIngredient.get("item");
					
					List<Object> fridgeItemDetails = fridgeItems.get(itemName);
					
					Date expiryDate = parseDate(fridgeItemDetails.get(2));
					
				}
			}
		}	
		System.out.println("Recipe found is : "+recipeFound);
	}
	
	/*
	 * 
	 * 
	 * 
	 */
	private Date parseDate(Object dateString) {
		
		DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
		try {
			Date date = format.parse(dateString.toString());
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
