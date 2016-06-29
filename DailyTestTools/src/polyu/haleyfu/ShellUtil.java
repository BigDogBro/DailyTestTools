package polyu.haleyfu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtil {
	
	 
	 public static String getProperty(String property)
	  {
	    Process p = null;

	    String result = "";
	    try {
	      p = Runtime.getRuntime().exec("getprop " + property);
	      p.waitFor();
	      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
	      result = reader.readLine();
	      reader.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    } finally {
	      if (p != null)
	        p.destroy();
	    }

	    return result;
	  }

}
