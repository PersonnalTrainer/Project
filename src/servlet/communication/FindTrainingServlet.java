package servlet.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

//import net.sf.jsr107cache.Cache;
//import net.sf.jsr107cache.CacheException;
//import net.sf.jsr107cache.CacheFactory;
//import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
//import com.google.appengine.api.memcache.MemcacheService;
//import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;

/**
 * Servlet implementation class InitHomePageServlet
 */
public class FindTrainingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DatastoreService datastore;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FindTrainingServlet() {
		super();
		// TODO Auto-generated constructor stub
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		JSONObject jsonToSend;
		jsonToSend=new JSONObject();

		Query q = new Query("User");
		q.addFilter("login", Query.FilterOperator.EQUAL, "Jacques");
		PreparedQuery pq = datastore.prepare(q);
		ArrayList<EmbeddedEntity> liste = null;

		for (Entity result : pq.asIterable()) {
			liste = (ArrayList<EmbeddedEntity>)result.getProperty("TrainingList");
		}


		JSONArray JAT = new JSONArray();
		for(EmbeddedEntity x: liste) {
			jsonToSend.put("name", x.getProperty("name"));
			jsonToSend.put("time", x.getProperty("time"));
			jsonToSend.put("completed", x.getProperty("completed"));
			JAT.put(jsonToSend);
		}
		PrintWriter out= response.getWriter();
		out.write(JAT.toString());


		/*
		int i=1;
		int taille = liste.size();
		jsonToSend.put("size", taille);
	    for(EmbeddedEntity x: liste) {
			jsonToSend.put("name"+i, x.getProperty("name"));
			jsonToSend.put("time"+i, x.getProperty("time"));
			jsonToSend.put("completed"+i, x.getProperty("completed"));
			i++;
	     }
	    */
	    
		/*
		Gson gson = new Gson();
		test = gson.fromJson(jsonString, liste);
	    jsonToSend.put("key", test);
	    */

		//		jsonToSend.put("trainingList", liste.toString());
		/*
		PrintWriter out= response.getWriter();
		out.write(jsonToSend.toString());
*/
	}

}
