package servlet.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;

public class UserRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String LOGIN_LABEL="login";
	private final static String EMAIL_LABEL="email";
	private final static String CMD_LABEL="cmd";
	private final static String PWD_LABEL="pwd";
	private DatastoreService datastore;
       
    public UserRegistrationServlet() {
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

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String cmdValue=request.getParameter(CMD_LABEL);
		if("Register".equals(cmdValue)){
			
			//Retreive Parameter to HTTP request
			String emailValue=request.getParameter(EMAIL_LABEL);
			String pwdValue= request.getParameter(PWD_LABEL);
			String loginValue= request.getParameter(LOGIN_LABEL);
			
			//Create Entity for the datastore
			Entity user = new Entity("User");
			ArrayList <EmbeddedEntity> trainingList = new ArrayList<EmbeddedEntity>();
			ArrayList <EmbeddedEntity> exerciceList = new ArrayList<EmbeddedEntity>();
			
			
			//// A commenter /////////////
			EmbeddedEntity training = new EmbeddedEntity();
			Date trainingDate = new Date();
			training.setProperty("date",trainingDate);
			training.setProperty("name","MyTrain1");
			training.setProperty("time",10);
			training.setProperty("completed","Yes");
			
			EmbeddedEntity training2 = new EmbeddedEntity();
			Date trainingDate2 = new Date();
			training.setProperty("date",trainingDate2);
			training2.setProperty("name","MyTrain2");
			training2.setProperty("time",20);
			training2.setProperty("completed","No");
			
			EmbeddedEntity exercice = new EmbeddedEntity();
			exercice.setProperty("name","MyEx1");
			exercice.setProperty("time",10);
			exercice.setProperty("completed","Yes");
			
			EmbeddedEntity exercice2 = new EmbeddedEntity();
			exercice2.setProperty("name","MyEx2");
			exercice2.setProperty("time",20);
			exercice2.setProperty("completed","No");
			
			trainingList.add(training);
			trainingList.add(training2);
			
			exerciceList.add(exercice);
			exerciceList.add(exercice2);
			
			//////////////////////
			
			//Add Properties to the Entity
			user.setProperty(LOGIN_LABEL, loginValue);
			user.setProperty(EMAIL_LABEL, emailValue);
			user.setProperty(PWD_LABEL, pwdValue);
			Date registerDate = new Date();
			user.setProperty("registerDate", registerDate);
			user.setProperty("TrainingList", trainingList);
			user.setProperty("ExerciceList", exerciceList);
			
			//Add the new Entity to the dataStore
			datastore.put(user);
			
			//Format the answer
			response.setContentType("application/json");
			JSONObject jsonToSend;
			jsonToSend=new JSONObject();
			
			jsonToSend.put("userid", user.getKey().getId());
			
			//Send the Json object to the web browser
			PrintWriter out= response.getWriter();
			out.write(jsonToSend.toString());
			
		}
	}

}
