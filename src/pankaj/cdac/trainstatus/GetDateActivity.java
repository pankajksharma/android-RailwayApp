package pankaj.cdac.trainstatus;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class GetDateActivity extends Activity {
	
	Date date;
	String finalDate="",stationCode,trainNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getdates);
	}
	
	public void getTrainStatus(View v){
		
		String month,year;
		int tempDate;
		SimpleDateFormat sdf;
		date = new Date();
		sdf = new SimpleDateFormat("MM");
		month = sdf.format(date);
		
		sdf = new SimpleDateFormat("yyyy");
		year = sdf.format(date);
		
		switch(v.getId()){
			case(R.id.btnToday):
				sdf = new SimpleDateFormat("dd");
				tempDate = Integer.parseInt(sdf.format(date).toString().trim());
				finalDate = tempDate+"";
				finalDate = finalDate+"/"+month+"/"+year;
				break;
			case(R.id.btnTomorrow):
				sdf = new SimpleDateFormat("dd");
				tempDate = Integer.parseInt(sdf.format(date).toString().trim())+1;
				finalDate = tempDate+"";
				finalDate = finalDate+"/"+month+"/"+year;
				break;
			case(R.id.btnDayAfterTomorrow):
				sdf = new SimpleDateFormat("dd");
				tempDate = Integer.parseInt(sdf.format(date).toString().trim())+2;
				finalDate = tempDate+"";
				finalDate = finalDate+"/"+month+"/"+year;
				break;
			case(R.id.btnYesterday):
				sdf = new SimpleDateFormat("dd");
				tempDate = Integer.parseInt(sdf.format(date).toString().trim())-1;
				finalDate = tempDate+"";
				finalDate = finalDate+"/"+month+"/"+year;
				break;
		}
		Toast.makeText(getApplicationContext(), "Making Query for "+finalDate, Toast.LENGTH_LONG).show();
		takeToTrainStatusActivity();
	}

	private void takeToTrainStatusActivity() {
		trainNumber = getIntent().getStringExtra("trainNumber").toString();
		stationCode = getIntent().getStringExtra("stationCode").toString();
		Intent statusIntent = new Intent();
		statusIntent.setClassName("pankaj.cdac.trainstatus", "pankaj.cdac.trainstatus.TrainStatusActivity");
		statusIntent.putExtra("trainNumber", trainNumber);
		statusIntent.putExtra("stationCode", stationCode);
		statusIntent.putExtra("queryDate", finalDate);
		//Toast.makeText(getApplicationContext(), finalDate+" "+stationCode+" "+trainNumber, Toast.LENGTH_LONG).show();
		startActivity(statusIntent);
	}
	
}