package fr.graeff.boris.neufboxRemoting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Configuration of neufbox ip, login and password
 *
 */
public class SettingsActivity extends Activity
{
	private EditText ipAddress;
	private EditText login;
	private EditText securityKey;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.settings);
        
        SharedPreferences sharedPreferences = getSharedPreferences(Config.PREFS_FILE, Context.MODE_PRIVATE);

        ipAddress = (EditText) findViewById(R.id.ipAddress);
        login = (EditText) findViewById(R.id.login);
        securityKey = (EditText) findViewById(R.id.securityKey);
        
        ipAddress.setText(sharedPreferences.getString("ipAddress", getResources().getString(R.string.neufbox_ip)));
        login.setText(sharedPreferences.getString("login", getResources().getString(R.string.admin)));
        securityKey.setText(sharedPreferences.getString("securityKey", ""));
        
        ImageButton save = (ImageButton) findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				savePreferences();
				finish();
			}
		});
    }
    
    /**
     * Save settings state
     */
    private void savePreferences()
    {	 
    	SharedPreferences sharedPreferences = getSharedPreferences(Config.PREFS_FILE, Context.MODE_PRIVATE);
    	Editor editor = sharedPreferences.edit();
    	editor.putString("ipAddress", ipAddress.getText().toString().trim());
    	editor.putString("login", login.getText().toString().trim());
    	editor.putString("securityKey", securityKey.getText().toString().trim());
    	editor.commit();
	 }
    
    @Override
    public void onBackPressed()
    {
    	this.savePreferences();
    	
    	super.onBackPressed();
    }
    
}