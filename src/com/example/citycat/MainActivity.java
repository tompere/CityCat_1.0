package com.example.citycat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Button postEvent;
	Intent goToPostEvent;
	TextView hotEventsText;
	Scroller hotEventsScroller;
	Context context;
	/////////
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hotEventsText = (TextView)this.findViewById(R.id.hotEvents);
        context = this;
        
        postEvent = (Button)this.findViewById(R.id.location);
        goToPostEvent = new Intent(this, ChooseLocation.class);       
        clickHandler click = new clickHandler();
        postEvent.setOnClickListener(click);
        
        Thread hotEventsThread = new Thread(hotEvents);
        hotEventsThread.start();

    }

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
	
	private Runnable hotEvents = new Runnable() {
    	public void run() { 		
    		hotEventsScroller = new Scroller(context, new LinearInterpolator());
    		hotEventsText.setText("It's true that all the men you knew" 
    								+ "\n" + "were dealers who said they were through"
    								+ "\n" + "with dealing Every time you gave them shelter");
    		hotEventsText.setScroller(hotEventsScroller);	
    		while (true)
    		{
    			if (hotEventsScroller.isFinished())
    				hotEventsScroller.startScroll(0, 0, 0, 500, 5000);
    		}
    	}
    };
	
}
