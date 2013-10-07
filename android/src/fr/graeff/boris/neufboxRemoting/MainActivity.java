package fr.graeff.boris.neufboxRemoting;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import fr.graeff.boris.neufboxRemoting.pojos.Client;
import fr.graeff.boris.neufboxRemoting.utils.Connectivity;
import fr.graeff.boris.neufboxRemoting.utils.ExpandableListView;
import fr.graeff.boris.neufboxRemoting.utils.Log;

/**
 * Main activity
 */
public class MainActivity extends Activity
{
	private List<Client> wlanClients;
	private List<Client> hotspotClients;
	
	// Connection status 
	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
        @Override
        public void onReceive(Context context, Intent intent)
        {
        	connectionStatus();
        }
    };
	
    /**
     * Check if user has filled connection infos in settings activity
     * @return
     */
	private boolean hasFilledConnectionInfos()
	{
		SharedPreferences sharedPreferences = getSharedPreferences(Config.PREFS_FILE, Context.MODE_PRIVATE);
		String ip = sharedPreferences.getString("ipAddress", "");
        String login = sharedPreferences.getString("login", "");
        String securityKey = sharedPreferences.getString("securityKey", "");
       
        return ! (ip.isEmpty() || login.isEmpty() || securityKey.isEmpty());
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		unregisterReceiver(receiver);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		
		// Connected clients lists
		wlanClients = new ArrayList<Client>();
		hotspotClients = new ArrayList<Client>();
		
		// TODO : temporary
		wlanClients.add(new Client("192.168.1.1", "22:11:33:44"));
		wlanClients.add(new Client("192.168.1.2", "22:11:33:55"));
		wlanClients.add(new Client("192.168.1.3", "22:11:33:66"));
		
		hotspotClients.add(new Client("192.168.1.1", "22:11:33:44"));
		
		ExpandableListView wlanClientsView = (ExpandableListView) findViewById(R.id.wlanClients);
		ExpandableListView hotspotClientsView = (ExpandableListView) findViewById(R.id.hotspotClients);
		wlanClientsView.setExpanded(true);
		hotspotClientsView.setExpanded(true);
		ClientListAdapter wlanAdapter = new ClientListAdapter(this, wlanClients);
		wlanClientsView.setAdapter(wlanAdapter);
		
		ClientListAdapter hotspotAdapter = new ClientListAdapter(this, hotspotClients);
		hotspotClientsView.setAdapter(hotspotAdapter);
		
		// Buttons 
		final Switch hotspot = (Switch) findViewById(R.id.hotspot);
		final Switch wlan = (Switch) findViewById(R.id.wlan);
		ImageButton settings = (ImageButton) findViewById(R.id.settings);	
		
		// Settings
		settings.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        		startActivity(intent);
			}
		});
		
		// Switchs
		hotspot.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{	
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(hasFilledConnectionInfos())
				{
					if(hotspot.isChecked())
					{
						RequestToApi r = new RequestToApi(Config.ENABLE_HOTSPOT, MainActivity.this);
						// TODO r.execute();
					}
					else
					{
						RequestToApi r = new RequestToApi(Config.DISABLE_HOTSPOT, MainActivity.this);
						// TODO r.execute();
					}
				}
				else
				{
					Log.d("TODO : FLASH WARNING !"); // TODO
				}
			}
		});
		
		wlan.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{	
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(hasFilledConnectionInfos())
				{
					if(wlan.isChecked())
					{
						RequestToApi r = new RequestToApi(Config.ENABLE_WLAN, MainActivity.this);
						// TODO r.execute();
					}
					else
					{
						RequestToApi r = new RequestToApi(Config.DISABLE_WLAN, MainActivity.this);
						// TODO r.execute();
					}
				}
				else
				{
					Log.d("TODO : FLASH WARNING !"); // TODO
				}
			}
		});
		
	}
	
	/**
	 * Display connection status
	 */
	private void connectionStatus()
	{
		TextView state = (TextView) findViewById(R.id.internetState);
		
		if(Connectivity.isConnectedWifi(this))
		{
			state.setText(R.string.connected_wifi);
			state.setTextColor(getResources().getColor(R.color.blue));
		}
		else if(Connectivity.isConnectedMobile(this))
		{
			state.setText(R.string.connected_mobile);
			state.setTextColor(getResources().getColor(R.color.blue));
		}
		else
		{
			state.setText(R.string.not_connected);
			state.setTextColor(getResources().getColor(R.color.red));
		}
	}
	
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		// Internet connection status
		connectionStatus();
		
		// Display warning ?
		RelativeLayout warning = (RelativeLayout) findViewById(R.id.warning);
		
		if(hasFilledConnectionInfos())
			warning.setVisibility(View.GONE);
		else
			warning.setVisibility(View.VISIBLE);
		
		// Connected to the box ?
		RequestToApi r = new RequestToApi(Config.GET_INFOS, this);
		// TODO r.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
        switch (item.getItemId())
        {
        	case R.id.action_settings:
        		Intent intent = new Intent(this, SettingsActivity.class);
        		startActivity(intent);
        		break;
        }
 
        return true;
    }
	
	
	/**
	 * Adapter for clients list view 
	 */
	public class ClientListAdapter extends BaseAdapter
	{
		private MainActivity activity;
		private List<Client> clients;
		
		/**
		 * Constructor
		 * @param activity
		 * @param clients
		 */
		public ClientListAdapter(MainActivity activity, List<Client> clients)
		{
			this.activity = activity;
			this.clients = clients;
		}
	 
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 
			View listView;
			Client client = (Client) getItem(position);
	 
			if (convertView == null)
			{
				listView = inflater.inflate(R.layout.list_item_client, null);
			}
			else
			{
				listView = convertView;
			}
			
			((TextView) listView.findViewById(R.id.ip)).setText(client.getIp());
			((TextView) listView.findViewById(R.id.mac)).setText(client.getMac());
		
			return listView;
		}
	 
		@Override
		public int getCount()
		{
			return clients.size();
		}
	 
		@Override
		public Object getItem(int position)
		{
			return clients.get(position);
		}
	 
		@Override
		public long getItemId(int position)
		{
			return 0;
		}
	}

}
