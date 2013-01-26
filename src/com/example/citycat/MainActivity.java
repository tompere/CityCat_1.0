package com.example.citycat;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Button postEvent;
	Intent goToPostEvent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        postEvent = (Button)this.findViewById(R.id.location);
        goToPostEvent = new Intent(this, ChooseLocation.class);       
        clickHandler click = new clickHandler();
        postEvent.setOnClickListener(click);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	class clickHandler implements View.OnClickListener {
		public void onClick(View v)
		{
			if ((Button)v == postEvent)
			{
				startActivity(goToPostEvent);
			}
		}
	}
}
