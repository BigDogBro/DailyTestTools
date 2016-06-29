package polyu.haleyfu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class test {
	/*public static void main(String[] args) {
          System.out.println(pushCheck("4.2.0004"));
	}
	*/
	
	public static void main(String[] args) throws JSONException, IOException {
		System.out.println(System.getProperty("user.dir"));
		String r=FileUtil.readTxtFile(System.getProperty("user.dir")+"\\src\\config\\button.txt");
	/*	System.out.println(r);
		System.out.println(getJsonArrayValues(r,"buttons").length);*/
		String rr=FileUtil.getValue(r, "buttons");
		
		
		String ar="{\"name\":\"clear12\",\"command\":\"pm clear com.android.browser\"}";
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("name", "sew");
		resultJSON.put("na11me", "s11ew");
		
		JSONArray resultArray = new JSONArray(rr);
		resultArray.put(resultJSON);
		System.out.println(resultArray.toString());
		
		
	}
	
	public void addJson(String name,String command) throws JSONException, IOException{
		String json=FileUtil.readTxtFile(FileUtil.jsonPath);
		String buttonJson=FileUtil.getValue(json, "buttons");
		String write="{\"name\":\""+name+"\",\"command\":\""+command+"\"}";
		JSONArray resultArray = new JSONArray(buttonJson);
		resultArray.put(write);
		JSONObject newResult = new JSONObject();
		newResult.put("buttons", resultArray); 
		FileUtil.writeTxt(newResult.toString());
	}
	
	public static Object[] getJsonArrayValues(String response, String key) throws JSONException {
		String value = null;
		JSONObject resultJSON = new JSONObject(response);
		Iterator it = resultJSON.keys();

		String res = resultJSON.get(key).toString();
		JSONArray resultArray = new JSONArray(res);
		int num = resultArray.length();
		Object[] result = new String[num];
		if (num > 0) {
			for (int i = 0; i < num; i++) {
				JSONObject obj = resultArray.getJSONObject(i);	
				result[i] = obj.toString();
			}
		}
		return result;
	}
	

    

}
