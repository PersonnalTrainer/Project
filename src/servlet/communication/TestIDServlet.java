package servlet.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.*;

import org.json.simple.JSONObject;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;

@SuppressWarnings("serial")
public class TestIDServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world GET");
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
//		resp.getWriter().println("Hello, world POST");

		Cache cache=null;
		Map props = new HashMap(); 
		props.put(GCacheFactory.EXPIRATION_DELTA, 3600); 
		props.put(MemcacheService.SetPolicy.ADD_ONLY_IF_NOT_PRESENT, true); 
		try {
			// Récupération du Cache
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory(); 
			// création/récupération du cache suivant des propriétés spécifiques
			cache = cacheFactory.createCache(props);
			// Si aucune propriété n'est spécifiée,
			//créer/récupérer un cache comme ci-dessous
			//cache = cacheFactory.createCache(Collections.emptyMap());
		} catch (CacheException e) {
			// Traitement en cas d'erreur sur la récupération/configuration du cache
		}

		// Récupération de l'objet stocke
		String key="usermail"; 
		String value = (String)cache.get(key);
//		resp.getWriter().println("Cache: "+ value.toString() );
		
		if (value==null) value="Veuillez vous identifier";
		
		//Format the answer
		resp.setContentType("application/json");
		JSONObject jsonToSend;
		jsonToSend=new JSONObject();
		jsonToSend.put(key, value);

		//Send the Json object to the web browser
		PrintWriter out= resp.getWriter();
		out.write(jsonToSend.toString());

	}

}
