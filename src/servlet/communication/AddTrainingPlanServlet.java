package servlet.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class AddTrainingPlanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String EMAIL_LABEL="email";
	private final static String CMD_LABEL="cmd";
	private final static String PWD_LABEL="pwd";
	private DatastoreService datastore;
       
    public AddTrainingPlanServlet() {
        super();
    }
    
   @Override
   public void init() throws ServletException {
	   super.init();
	   //Create a connection to the datastore ONETIME at the init servlet process
		datastore = DatastoreServiceFactory.getDatastoreService();
   }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title =request.getParameter("title");
		String description =request.getParameter("description");
		String domain =request.getParameter("domain");
		String table =request.getParameter("table");
		
		Entity trainingPlan = new Entity("TrainingPlan");
		
		trainingPlan.setProperty("title", title);
		trainingPlan.setProperty("description", description);
		trainingPlan.setProperty("domain", domain);
		datastore.put(trainingPlan);
		
		String[] TableRows= table.split("nextLine");
		for(int i=0;i<TableRows.length;i++)
		{
			Entity Exercice = new Entity("Exercice");
			String[] TableCells= TableRows[i].split("nextValue");
			Exercice.setProperty("position", TableCells[0]);
			Exercice.setProperty("title", TableCells[1]);
			Exercice.setProperty("description", removeHTML(TableCells[2]));
			Exercice.setProperty("duree", TableCells[3]);
			Exercice.setProperty("TrainingPlan", title);
			
			datastore.put(Exercice);
		}
	}
	
	public static String removeHTML(String input) {
	    int i = 0;
	    String[] str = input.split("");

	    String s = "";
	    boolean inTag = false;

	    for (i = 0; i < input.length(); i++) {
	    	if(str[i].equals("<"))
	    		inTag = true;
	    	else if(str[i].equals(">"))
	    		inTag = false;
	    	
	    	if(inTag == false && !str[i].equals(">"))
	    		s += str[i];
	    }
	  
	    return s;
	}
	

}
