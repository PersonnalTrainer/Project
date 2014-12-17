package servlet.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import org.json.simple.JSONObject;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

	private static final Map<String, String> openIdProviders;
	static {
		openIdProviders = new HashMap<String, String>();
		openIdProviders.put("G", "https://www.google.com/accounts/o8/id");
		openIdProviders.put("Y", "yahoo.com");
		//openIdProviders.put("M", "myspace.com");
		openIdProviders.put("A", "aol.com");
		//openIdProviders.put("O", "myopenid.com");
	}


	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		doGet(req, resp);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		//CACHE
		Cache cache=null;
		Map props = new HashMap();
		props.put(GCacheFactory.EXPIRATION_DELTA, 3600);
		props.put(MemcacheService.SetPolicy.ADD_ONLY_IF_NOT_PRESENT, true);

		try {
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			cache = cacheFactory.createCache(props);
		} catch (CacheException e) {
			// Traitement en cas d'erreur sur la récupération/configuration du cache
		}

		// TRAITEMENT
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser(); // or req.getUserPrincipal()
		Set<String> attributes = new HashSet();
		String Machaine;

		if (user != null) {
			Machaine = "<i>" + user.getNickname() + "</i>";
			Machaine += "<br><a href=\""
					+ userService.createLogoutURL("/ha-search-screen.html")
					+ "\">sign out</a>";

			//Put in cache
			String mail = user.getNickname();
			cache.put("usermail", mail);
		} 
		else {
			//Remove in cache
			cache.remove("usermail");
			
			Machaine = "";
			for (String providerName : openIdProviders.keySet()) {
				String providerUrl = openIdProviders.get(providerName);
				String loginUrl = userService.createLoginURL("/ha-search-screen.html", null, providerUrl, attributes);
				if(providerName=="G") Machaine += "<a class=\"btn btn-primary\" href=\"" + loginUrl + "\">" + providerName + "</a> ";
				if(providerName=="Y") Machaine += "<a class=\"btn btn-success\" href=\"" + loginUrl + "\">" + providerName + "</a> ";
				if(providerName=="A") Machaine += "<a class=\"btn btn-warning\" href=\"" + loginUrl + "\">" + providerName + "</a> ";
			}

		}

		//Format the answer
		resp.setContentType("application/json");
		JSONObject jsonToSend;
		jsonToSend=new JSONObject();
		jsonToSend.put("Machaine", Machaine);

		//Send the Json object to the web browser
		PrintWriter out= resp.getWriter();
		out.write(jsonToSend.toString());


	}
}