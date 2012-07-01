package pankaj.cdac.trainstatus;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreenActivity extends Activity {
	
	Button btnGetTrainNo,btnGetPnr,btnTrackTrains;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bindItems();
        findTrainListener();
        getPnrListener();
        trackTrainListener();
    }
    
    
	private void trackTrainListener() {
		btnTrackTrains.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent findingIntent = new Intent();
				findingIntent.setClassName(getPackageName(), "pankaj.cdac.trainstatus.TrackTrainHomeActivity");
				startActivity(findingIntent);
			}
		});
	}


	private void findTrainListener() {
    	btnGetTrainNo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent findingIntent = new Intent();
				findingIntent.setClassName(getPackageName(), "pankaj.cdac.trainstatus.GetTrainNoActivity");
				startActivity(findingIntent);
			}
		});
	}

	
	private void getPnrListener() {
		btnGetPnr.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent findingIntent = new Intent();
				findingIntent.setClassName(getPackageName(), "pankaj.cdac.trainstatus.PnrActivity");
				startActivity(findingIntent);
			}
		});
	}

	public void bindItems(){
    	btnGetTrainNo = (Button) findViewById(R.id.btnGetTrainNo); 
    	btnGetPnr = (Button) findViewById(R.id.btnPnrStatus);
    	btnTrackTrains = (Button) findViewById(R.id.btnTrainTrack);
    }
    
    
}