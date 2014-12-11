package servlet.init;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;

/**
 * Servlet implementation class InitHomePageServlet
 */
public class InitHomePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DatastoreService datastore;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitHomePageServlet() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Cache cache=null;
		
		Map props = new HashMap();
		props.put(GCacheFactory.EXPIRATION_DELTA, 3600);
		props.put(MemcacheService.SetPolicy.ADD_ONLY_IF_NOT_PRESENT, true);
		
		try {
			// R�cup�ration du Cache
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			// cr�ation/r�cup�ration du cache suivant des propri�t�s sp�cifiques
			cache = cacheFactory.createCache(props);
			// Si aucune propri�t� n'est sp�cifi�e,
			//cr�er/r�cup�rer un cache comme ci-dessous
			//cache = cacheFactory.createCache(Collections.emptyMap());
		} catch (CacheException e) {
			// Traitement en cas d'erreur sur la r�cup�ration/configuration du cache
		}
		
		
		
		response.setContentType("application/json");
		JSONObject jsonToSend;
		jsonToSend=new JSONObject();
		if(cache.containsKey("messagecache")){
			jsonToSend.put("messagecache", cache.get("messagecache").toString());}
		else
		{
			Query q = new Query("MessageAccueil");
			
			// R�cup�ration du r�sultat de la requ�te � l�aide de PreparedQuery 
			PreparedQuery pq = datastore.prepare(q);
			String messageAccueil="";
			for (Entity result : pq.asIterable()) {
				messageAccueil = (String)result.getProperty("MessageAccueil");
			}
			
			
			cache.put("messagecache", messageAccueil);
			jsonToSend.put("messagecache", cache.get("messagecache").toString());
		}
		
		
		PrintWriter out= response.getWriter();
		out.write(jsonToSend.toString());
		
		
		
		
		
		/*String key="loggeduser"; // D�finir la cl� de la valeur � stocker
		String value="myValue1"; // D�finir l'objet � stocker
		// Mise en cache de l'objet � l'aide d'une cl�
		cache.put(key, user);
		// R�cup�ration de l'objet stock�
		value = ((UserModelBean)cache.get(key)).toString();
		*/
		
		
		
		
	}

}
