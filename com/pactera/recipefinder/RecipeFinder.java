package com.pactera.recipefinder;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main class for the Recipe Finder
 * @author saurabhmanwani
 *
 */
public class RecipeFinder {
	
	private static final String ENTER_JSON_FILE = "Enter recipe json file path";
	
	private static final String ENTER_CSV_FILE = "Enter fridge items csv file path";
	
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

		RecipeFinderController recipeFinderController = new RecipeFinderController();

		// parse the JSON String
		List<RecipeData> recipeDataList = recipeFinderController.parseRecipeJSONFile(jsonFile);

		// parse the csv file
		Map<String, FridgeItem> fridgeItemMap = recipeFinderController.parseFridgeItemsCSVFile(csvFile);

		if (null == recipeDataList || null == fridgeItemMap) {
			System.out.println("Error found during the parsing of data");
			return;
		}

		String responseString = recipeFinderController.runRecipeFinder(recipeDataList, fridgeItemMap);
		System.out.println(RECIPE_FOUND + responseString);

	}

}
