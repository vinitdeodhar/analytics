package analytics;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.*;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.appengine.api.xmpp.JID;
//import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class AnalyticsServlet extends HttpServlet {
	
	public String checknull(String src)
	{
		if(src == null)
			return "null";
		else
			return src;
	}

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//prepare to store data
		    Key guestbookKey = KeyFactory.createKey("Guestbook", "key1");
	        Entity greeting = new Entity("Hitsdata", guestbookKey);
		resp.setContentType("text/plain");
		String hostIp = null; 
		String hostName = null;
		String pageName = null;
		String hostOS = null;
		String hostBrowser = null;
		String hostPort = null;
	    String country = null;
	    String city = null;
	    String region = null;
	    String citylatlong = null;
	    String useragent = null;
		String referer = null;
		
		String dump = null;
		hostIp = req.getRemoteAddr();
		hostIp = checknull(hostIp);
		hostName = req.getRemoteHost();
		hostName = checknull(hostName);
		hostPort = ""+ req.getRemotePort();
		hostPort = checknull(hostPort);
		
		
		//http header
		 dump = " ";
		 Enumeration headerNames = req.getHeaderNames();
		 dump = dump + "Printing headers";
		 
		 while(headerNames.hasMoreElements()) {
		      String headerName = (String)headerNames.nextElement();
		      dump = dump + headerName+ "  :  ";
		      dump = dump + req.getHeader(headerName)+" \n";
		      if(headerName.equalsIgnoreCase("User-Agent"))
		      {
		    	  useragent = req.getHeader(headerName);
		    	  dump += "\n Adding agent "+useragent; 
			        greeting.setProperty("useragent", useragent);

		      }
		      if(headerName.equalsIgnoreCase("X-AppEngine-Country"))
		      {
		    	  country = req.getHeader(headerName);
		    	  dump += "\n Adding country "+country; 
		    	  greeting.setProperty("country", country);
		      }
		      if(headerName.equalsIgnoreCase("X-AppEngine-Region"))
		      {
		    	  region = req.getHeader(headerName);
		    	  dump += "\n Adding region "+region; 
		    	  greeting.setProperty("region", region);
		      }
		      if(headerName.equalsIgnoreCase("X-AppEngine-CityLatLong"))
		      {
		    	  citylatlong = req.getHeader(headerName);
		    	  dump += "\n Adding citylatlon "+citylatlong; 
		    	  greeting.setProperty("citylatlong", citylatlong);
		      }
		      
		      if(headerName.equalsIgnoreCase("Referer"))
		      {
		    	  referer = req.getHeader(headerName);
		    	  dump += "\n Adding Referer "+referer; 
		    	  greeting.setProperty("referer", referer);
		      }
		      
		    }
		 
		
	    dump +=  "host IP   : " + hostIp + " \n";
		dump += "host port :" +hostPort + "\n";
		dump += "host name : "+hostName + "\n";
		greeting.setProperty("hostIp", hostIp);
		greeting.setProperty("hostPort", hostPort);
		greeting.setProperty("hostName", hostName);
		
		//resp.getWriter().println("Dump \n"+dump);
     
		// send chat
		JID jid = new JID("vadeodhar89@gmail.com");
        String msgBody1 = "Someone is viewing your homepage";
        com.google.appengine.api.xmpp.Message msg1 = new MessageBuilder()
            .withFromJid(new JID("vinitiitbasitedmin@appspot.com"))
            .withRecipientJids(jid)
            .withBody(msgBody1)
            .build();

        boolean messageSent = false;
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        xmpp.sendInvitation(jid,new JID("vinitiitbasitedmin@appspot.com"));
        SendResponse status = xmpp.sendMessage(msg1);
        messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
        
        dump+="\n\n Chat message sent status : "+messageSent;
        Date today = new Date();
        
        dump+=  "\n" + today.toString();
		greeting.setProperty("date", today);
  
        
		//send email
		/* Properties props = new Properties();
	        Session session = Session.getDefaultInstance(props, null);

	        String msgBody = dump;

	        try {
	            Message msg = new MimeMessage(session);
	            msg.setFrom(new InternetAddress("vadeodhar1989@gmail.com", "Vinit"));
	            msg.addRecipient(Message.RecipientType.TO,
	                             new InternetAddress("vadeodhar1989@gmail.com", "Vinit"));
	            msg.setSubject("SITE UPDATE");
	            msg.setText(msgBody);
	            Transport.send(msg);

	        } catch (AddressException e) {
	            // ...
	        } catch (MessagingException e) {
	            // ...
	        }*/
		
	        Text fulldump = new Text(dump);
	        
	        greeting.setProperty("fulldump", fulldump);
	      
	        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	        datastore.put(greeting);
	        
	       
	       
	        /*Key guestbookKey1 = KeyFactory.createKey("Guestbook", "key1");
	        Entity greeting1 = new Entity("Passcode", guestbookKey1);
	        greeting1.setProperty("passcode", "passcode");
	        datastore.put(greeting1);*/
	        
		resp.getWriter().println("Logo could not be loaded");
		
		
		
	}
}
