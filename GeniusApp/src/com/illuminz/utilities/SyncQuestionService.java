package com.illuminz.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;

import com.illuminz.database.DatabaseManager;

public class SyncQuestionService extends Activity{
	
static HashMap<String, String> map;
	
	public static void syncQuestion(ArrayList<HashMap<String, String>> servicesData, Activity activity) {

		DatabaseManager db = new DatabaseManager(activity);
		db.open();
		ArrayList<HashMap<String, String>> dataFromdb = db.ReteriveTableData();
	
		for (int i = 0; i < servicesData.size(); i++) 
		{
			map = servicesData.get(i);
			
			if (dataFromdb.size() == 0) 
			{
				db.SaveQuestionData(map.get("id"), map.get("question"), map.get("option_1"),map.get("option_2"),map.get("option_3"), map.get("option_4"),map.get("answer"));
			//System.out.println("save question answer");
			} else 
			{
				db.updateQuestionData(map.get("id"), map.get("question"), map.get("option_1"),map.get("option_2"),map.get("option_3"), map.get("option_4"),map.get("answer"));
				//System.out.println("update question answer");
			}	
		}
		db.close();
		
	}

}