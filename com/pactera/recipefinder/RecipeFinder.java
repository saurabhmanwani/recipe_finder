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

/**
 * @author saurabhmanwani
 *
 */
public class RecipeFinder {

	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	private static final String JSON_PARSE_ERROR = "Error occured while parsing recipe json file";
	
	private static final String CSV_PARSE_ERROR = "Error occured while parsing fridge items csv file";
	
	private static final String ENTER_JSON_FILE = "Enter recipe json file path";
	
	private static final String ENTER_CSV_FILE = "Enter fridge items csv file path";
	
	private static final String ALGO_ERROR = "Error occured while runing recipe finder algorithm";
	
	private static final String EXIT_RECIPE_FINDER = "Exiting recipe finder";
	
	private static final String RECIPE_FOUND = "Recipe found is : ";
	
	/** Execution starts from this method 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.println(ENTER_JSON_FILE);
		String jsonFile = sc.next();
		System.out.println(ENTER_CSV_FILE);
		String csvFile = sc.next();
		
		sc.close();
		
		RecipeFinder recipeFinder = new RecipeFinder();
		recipeFinder.startRecipeFinderExecution(jsonFile, csvFile);		
	}
	

	/**
	 * @param jsonFile
	 * @param csvFile
	 */
	private void startRecipeFinderExecution(String jsonFile, String csvFile) {
		
		// parse json file
	//	jsonFile = "/Users/saurabhmanwani/Desktop/recipeJson.txt";
		String recipe = parseRecipeJSONFile(jsonFile);
		
		// parse csv file
	//	csvFile = "/Users/saurabhmanwani/Desktop/fridgeItemsCsv.csv";
		Map<String,List<Object>> fridgeItems = parseFridgeItemsCSVFile(csvFile);
		
		if(recipe!=null && fridgeItems!=null) {
			JSONArray recipeArray =  new JSONArray(recipe);
			// run recipe finder
			String recipeName = runRecipeFinder(recipeArray, fridgeItems);
			if(recipeName !=null){
				System.out.println(RECIPE_FOUND+ recipeName);
			} else {
				System.out.println(EXIT_RECIPE_FINDER);
			}
		} else{
			System.out.println(EXIT_RECIPE_FINDER);
		}
	}
	

	/** This method parses json recipe file
	 * @param jsonFile
	 * @return File as a String
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
			return jsonFileString.toString();
		} catch (Exception e) {
			System.out.println(JSON_PARSE_ERROR);
			return null;
		} finally {
			if (fileInput != null) {
				try {
					fileInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	

	/** This method parses fridge items csv file
	 * @param csvFile
	 * @return Fridge items HashMap
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
			return fridgeItems;
		} catch (Exception e) {
			System.out.println(CSV_PARSE_ERROR);
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	/** This method populates the values of fridge items in data structure
	 * @param fridgeItems
	 * @param csvLine
	 */
	private void poplutateFridgeItems(Map<String,List<Object>> fridgeItems, String[] csvLine){
		
		List<Object> csvLineVals = new ArrayList<Object>();
		for(int count=1;count<csvLine.length;count++) {
			csvLineVals.add(csvLine[count]);
		}
		fridgeItems.put(csvLine[0], csvLineVals);
	}
	

	/** This method runs the algorithm for recipe finder
	 * @param recipeArray
	 * @param fridgeItems
	 * @return Recipe String
	 */
	private String runRecipeFinder(JSONArray recipeArray, Map<String,List<Object>> fridgeItems) {
		try {
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
					
					
					if(!expiryDate.before(new Date()) && 
							(Unit)fridgeItemDetails.get(1) == (Unit.valueOf("")) &&
							(Integer)fridgeItemDetails.get(0) == 4) {
						
						
					}
				}
			}
		}	
		return recipeFound;
		} catch(Exception e) {
			System.out.println(ALGO_ERROR);
			return null;
		}
	}
	

	/** This is a utility mehotd for date parsing
	 * @param dateString
	 * @return Date
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
