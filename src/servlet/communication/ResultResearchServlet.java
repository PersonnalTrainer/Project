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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class ResultResearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DatastoreService datastore;
       
    public ResultResearchServlet() {
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
		
		String domain =request.getParameter("domain");
		
		String TrainingPlansInfos ="";
		String ExerciceInfos="";
		
		Query qTraining = new Query("TrainingPlan");
		qTraining.addFilter("domain", Query.FilterOperator.EQUAL, domain);
		PreparedQuery pqTraining = datastore.prepare(qTraining);
		for (Entity resultTraining : pqTraining.asIterable()) {
			String TitleTrainingPlan = (String)resultTraining.getProperty("title");
			
			int dureeTraining=0;
			Query qExerciceTime = new Query("Exercice");
			qExerciceTime.addFilter("TrainingPlan", Query.FilterOperator.EQUAL, TitleTrainingPlan);
			PreparedQuery pqExerciceTime = datastore.prepare(qExerciceTime);
			for (Entity resultExerciceTime : pqExerciceTime.asIterable()) {
				String dureeExercice = (String)resultExerciceTime.getProperty("duree");
				String TitreExercice = (String)resultExerciceTime.getProperty("title");
				
				
				ExerciceInfos += "nextvalue" + TitreExercice + "nextvalue" + dureeExercice + "nexttraining";
				
				dureeExercice= dureeExercice.substring(0, dureeExercice.length()-3);
				dureeTraining += Integer.parseInt(dureeExercice);
			}
			String dureeTrainingS = dureeTraining + "min";
			
			TrainingPlansInfos += "nextvalue" + TitleTrainingPlan + "nextvalue" + dureeTrainingS + "nexttraining";
			
			
			
			
		}
			
		//Format the answer
		response.setContentType("application/json");
		JSONObject jsonToSend;
		jsonToSend = new JSONObject();
		
		jsonToSend.put("training", TrainingPlansInfos);
		jsonToSend.put("exercice", ExerciceInfos);
		
		//Send the Json object to the web browser
		PrintWriter out= response.getWriter();
		out.write(jsonToSend.toString());
	}
}
