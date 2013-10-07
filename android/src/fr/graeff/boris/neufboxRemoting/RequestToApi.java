package fr.graeff.boris.neufboxRemoting;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;
import fr.graeff.boris.neufboxRemoting.pojos.Rsp;
import fr.graeff.boris.neufboxRemoting.utils.Hashing;
import fr.graeff.boris.neufboxRemoting.utils.Log;

/**
 * Handle requests to Neufbox API
 *
 */
public class RequestToApi extends AsyncTask<Void, Void, Boolean>
{
	
	private static final String URL_GET_TOKEN = "/api/?method=auth.getToken";
	private static final String URL_CHECK_TOKEN = "/api/?method=auth.checkToken";
	
	private static final String URL_HOTSPOT_ENABLE = "/api/?method=hotspot.enable";
	private static final String URL_HOTSPOT_DISABLE = "/api/?method=hotspot.disable";
	private static final String URL_HOTSPOT_START = "/api/?method=hotspot.start";
	private static final String URL_HOTSPOT_STOP = "/api/?method=hotspot.stop";
	
	private static final String URL_WLAN_ENABLE = "/api/?method=wlan.enable";
	private static final String URL_WLAN_DISABLE = "/api/?method=wlan.disable";
	private static final String URL_WLAN_STOP = "/api/?method=wlan.stop";
	private static final String URL_WLAN_START = "/api/?method=wlan.start";
	
	private static final String URL_HOTSPOT_INFO = "/api/?method=hotspot.getInfo";
	private static final String URL_WLAN_INFO = "/api/?method=wlan.getInfo";
	
	private String ip;
	private String login; 
	private String securityKey;
	private int action;
	private MainActivity activity;
	
	/**
	 * Constructor
	 * @param action
	 * @param activity
	 */
	public RequestToApi(int action, MainActivity activity)
	{
		this.action = action;
		
		this.activity = activity;
		
		SharedPreferences sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
		ip = sharedPreferences.getString("ipAddress", activity.getResources().getString(R.string.neufbox_ip));
        login = sharedPreferences.getString("login", activity.getResources().getString(R.string.admin));
        securityKey = sharedPreferences.getString("securityKey", activity.getResources().getString(R.string.empty));
	}
	
	@Override
	protected Boolean doInBackground(Void... params)
	{
		HttpParams httpParameters = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParameters, Config.TIMEOUT_CONNECTION);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		
		String token = this.getToken(httpClient);
		
		if(token.isEmpty())
			return false;
		
		checkToken(httpClient, token);
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean success)
	{
		if(success)
		{
			TextView state = (TextView) activity.findViewById(R.id.boxState);
			state.setText(R.string.connected);
			state.setTextColor(activity.getResources().getColor(R.color.blue));
		}
		else
		{
			TextView state = (TextView) activity.findViewById(R.id.boxState);
			state.setText(R.string.not_connected);
			state.setTextColor(activity.getResources().getColor(R.color.red));
		}
    }
	
	/**
	 * Get authentication token from neufbox
	 * @param httpClient
	 * @return
	 */
	private String getToken(HttpClient httpClient)
	{
		String uri = "http://"+ip+URL_GET_TOKEN;
		HttpGet httpget = new HttpGet(uri); 
		HttpResponse response;
		
		Log.d("getToken 1");
		   
	   try
	   {
		   Log.d("getToken 1.2");
	       response = httpClient.execute(httpget);
	       Log.d("getToken 1.3");
	       HttpEntity entity = response.getEntity();
	       Log.d("getToken 3 entity null ? "+(entity == null));
	       if (entity != null)
	       {
	           String res = EntityUtils.toString(entity, HTTP.UTF_8);
	           Serializer serializer = new Persister();
	           Rsp rsp = serializer.read(Rsp.class, res);

	           return rsp.getAuth().getToken(); 
	       }
	   }
	   catch (Exception e)
	   {
		   Log.e("Exception "+e.getMessage(), e);
	   }
	   
	   return "";
	}
	
	// TODO : 
	private void checkToken(HttpClient httpClient, String token)
	{
		String loginHash, passwordHash;
		
		try
		{
			loginHash = Hashing.hashSHA256(login);
			loginHash = Hashing.hashHmacSHA256(token, loginHash);
			
			passwordHash = Hashing.hashSHA256(securityKey);
			passwordHash = Hashing.hashHmacSHA256(token, passwordHash);
  
			String hashFinal = loginHash+passwordHash;
		
		    String params = "&token="+token;
			params += "&hash="+hashFinal;
			
			String uri = "http://"+ip+URL_CHECK_TOKEN;
			HttpGet httpget = new HttpGet(uri+params);
	       
	        HttpResponse response = httpClient.execute(httpget);
	        HttpEntity entity = response.getEntity();
	     	   
 	       if (entity != null)
 	       {
 	    	   String res = EntityUtils.toString(entity, HTTP.UTF_8);
 	    	
	           Serializer serializer = new Persister();
	           Rsp rsp = serializer.read(Rsp.class, res);

	           // Error ?
	           if(rsp.getErr() != null)
	           {
	        	   // Get token
		           token = rsp.getAuth().getToken();
		        
		           if(action == Config.DISABLE_WLAN)
		        	   disableWlan(httpClient, token);
		           else if(action == Config.ENABLE_WLAN)
		        	   enableWlan(httpClient, token);
		           else if(action == Config.DISABLE_HOTSPOT)
		        	   disableHotspot(httpClient, token);
		           else if(action == Config.ENABLE_HOTSPOT)
		        	   enableHotspot(httpClient, token);
		           else
		        	   getInfos(httpClient, token);
	 	       }
	           else
	           {
	        	   Log.e("Error "+rsp.getErr().getMsg());
	           }
 	       }
		}
		catch (Exception e) {
			Log.e("Exception "+e.getMessage(), e);
		}
		
	}

	/**
	 * TODO
	 * @param httpClient
	 * @param token
	 */
	private void getInfos(HttpClient httpClient, String token)
	{
		String uri = "http://"+ip+URL_WLAN_INFO;
		HttpGet httpget = new HttpGet(uri); 
		HttpResponse response;
		   
	   try
	   {
	       response = httpClient.execute(httpget);
	       HttpEntity entity = response.getEntity();
	   
	       if (entity != null)
	       {
	           String res = EntityUtils.toString(entity, HTTP.UTF_8);
	           Log.d("Wlan info : "+res);
	       }
	       
	       uri = "http://"+ip+URL_HOTSPOT_INFO;
	       httpget = new HttpGet(uri); 
	       response = httpClient.execute(httpget);
	       entity = response.getEntity();
	   
	       if (entity != null)
	       {
	           String res = EntityUtils.toString(entity, HTTP.UTF_8);
	           Log.d("Hotspot info : "+res);
	       }
	   }
	   catch (Exception e)
	   {
		   Log.e("Exception "+e.getMessage(), e);
	   }
	}
	
	/**
	 * Disable Wlan
	 * @param httpClient
	 * @param token
	 */
	private void disableWlan(HttpClient httpClient, String token)
	{
		String params = "&token="+token;
		String uri = "http://"+ip+URL_WLAN_STOP;
		HttpPost httppost = new HttpPost(uri+params); 
		HttpResponse response;
		
		
		try {
			response = httpClient.execute(httppost);
			 Log.d("status "+response.getStatusLine().getStatusCode());

		     HttpEntity entity = response.getEntity();
		     
		     if (entity != null)
	 	       {
	 	    	  String res = EntityUtils.toString(entity, HTTP.UTF_8);
	 	           Log.d("Disable wifi : "+res);
	 	       }
		       
		}
		catch (Exception e)
		{
			Log.e("Exception"+ e.getMessage(), e);
		}
	}
	
	/**
	 * Enable hotspot
	 * @param httpClient
	 * @param token
	 */
	private void enableHotspot(HttpClient httpClient, String token)
	{
		
	}
	
	/**
	 * Disable hotspot 
	 * @param httpClient
	 * @param token
	 */
	private void disableHotspot(HttpClient httpClient, String token)
	{
		// TODO
	}
	
	/**
	 * Enable Wlan
	 * @param httpClient
	 * @param token
	 */
	private void enableWlan(HttpClient httpClient, String token)
	{
		String params = "&token="+token;
		String uri = "http://"+ip+URL_WLAN_START;
		HttpPost httppost = new HttpPost(uri+params); 
		HttpResponse response;
		
		try {
			 response = httpClient.execute(httppost);
		     HttpEntity entity = response.getEntity();
		     
		     if (entity != null)
	 	     {
	 	    	  String res = EntityUtils.toString(entity, HTTP.UTF_8);
	 	          Log.d("Enable wifi : "+res);
	 	     }
		       
		}
		catch (Exception e)
		{
			Log.e("Exception"+ e.getMessage(), e);
		}
	}
	
	
}