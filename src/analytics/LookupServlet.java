package analytics;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LookupServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		
		String passcode = req.getParameter("passcode");
        Key guestbookKey1 = KeyFactory.createKey("Guestbook", "key1");
		DatastoreService datastore1 = DatastoreServiceFactory.getDatastoreService();
		Query query1 = new Query("Passcode", guestbookKey1);
	    List<Entity> greetings1 = datastore1.prepare(query1).asList(FetchOptions.Builder.withLimit(100));
		String storedpass = (String)greetings1.get(0).getProperty("passcode");
	    
		
		if(storedpass.equals(passcode))
		{
		Key guestbookKey = KeyFactory.createKey("Guestbook", "key1");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Hitsdata", guestbookKey).addSort("date", Query.SortDirection.DESCENDING);
		
	    List<Entity> greetings = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(100));
	    
	    String dump = "";
        int noofhits = greetings.size();
        
        dump += "\n number of hits :" + noofhits;
        for(int i = 0 ;i < greetings.size();i++){
        	Entity en = greetings.get(i);
        	String referer = (String)en.getProperty("referer");
        	String useragent = (String)en.getProperty("useragent");
        	String country = (String)en.getProperty("country");
        	String region = (String)en.getProperty("region");
        	String citylatlong = (String)en.getProperty("citylatlong");
        	String hostIp = (String)en.getProperty("hostIp");
        	String hostPort = (String)en.getProperty("hostPort");
        	String hostName = (String)en.getProperty("hostName");
        	Date date = (Date)en.getProperty("date");
        	//Text fulldump = (Text)en.getProperty("fulldump");
        	
        	dump +="\n Dislaying hit no : "+(i+1)+ " : \n";
        	
        	if(referer!=null)
            dump += "\n Page name : " + referer;
        	if(useragent!=null)
        	dump += "\n Useragent : " + useragent;
        	if(country!=null)
        	dump += "\n Country : " + country;
        	if(region!=null)
        	dump += "\n Region : " + region;
        	if(citylatlong!=null)
        	dump += "\n Citylatlong : " + citylatlong;
        	if(hostIp!=null)
        	dump += "\n HostIp : " + hostIp;
        	if(hostPort!=null)
        	dump += "\n hostPort : " + hostPort;
        	if(hostName!=null)
        	dump += "\n hostName : " + hostName;
        	if(date!=null)
            dump += "\n date time : " + date.toString();
        	
        	dump +="\n\n";
        	
        	//dump += "\n hostPort : " + hostPort;
        	
        			
        }
        
		resp.getWriter().println("Displaying visits info \n\n"+dump);
		}
		else
		{
			resp.getWriter().println("Invalid password");
		}
		
	}

}
