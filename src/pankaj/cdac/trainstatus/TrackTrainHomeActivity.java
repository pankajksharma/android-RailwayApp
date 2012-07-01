package pankaj.cdac.trainstatus;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TrackTrainHomeActivity extends Activity{
	
	EditText edtTrainNo;
	Button btnGetStations,btnGetDates;
	Spinner spinStationsList;
	String globalStationCodes[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracktrainhome);
		bindItems();
		getStations();
		mySubmitListener();
	}

	public void mySubmitListener(){
		btnGetDates.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				Intent dateActivity = new Intent();
				dateActivity.setClassName("pankaj.cdac.trainstatus", "pankaj.cdac.trainstatus.GetDateActivity");
				dateActivity.putExtra("trainNumber", edtTrainNo.getText().toString());
				dateActivity.putExtra("stationCode", globalStationCodes[spinStationsList.getSelectedItemPosition()]);
				startActivity(dateActivity);
			}
		});
	}

	private void bindItems() {
		edtTrainNo = (EditText) findViewById(R.id.edtTrainNumber);
    	btnGetStations = (Button) findViewById(R.id.btnGetStations);
    	spinStationsList = (Spinner) findViewById(R.id.spinStations);
    	btnGetDates = (Button) findViewById(R.id.btnToDateActivity);
	}
	
	public int getStations(){
    	
	    btnGetStations.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
			
	    	if(edtTrainNo.getText().length()<5){
	    		Toast.makeText(getApplicationContext(), "Invalid Train Number!", Toast.LENGTH_LONG).show();
	    		edtTrainNo.setText("");
	    		return;
	    	}
	    	DefaultHttpClient httpClient = new DefaultHttpClient();
	    	HttpGet httpGet = new HttpGet("http://www.indiantrains.org/train-details/?number="+edtTrainNo.getText().toString());
	    	ResponseHandler<String> responseString = new BasicResponseHandler();
	    	try {
				String page = httpClient.execute(httpGet, responseString);
				if(page.contains("Find Trains, Check Ticket Availability")){
					Toast.makeText(getApplicationContext(), "Unkown Train", Toast.LENGTH_LONG).show();
					edtTrainNo.setText("");
				}
				else{
					page=page.split("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\">")[1];
					page=page.split("</td></tr></table>")[0];
					String stationsLink[]=page.split("<a href=\"");
				
					int noOfStations=0;
					String temp="";
					String temp2="";
					String stationsCodes[] = new String[stationsLink.length/2];
					String stationsNames[] = new String[stationsLink.length/2];
					//globalStationCodes = new String[stationsLink.length/2];
				
					for(int i=1;i<stationsLink.length;i+=2){
						stationsLink[i]=stationsLink[i].split("\" title=\"View Station Details\" style=\"color:#000000;\">")[0];
						temp = stationsLink[i].split("code=")[1];
						temp2 = temp;
						stationsCodes[noOfStations] = temp2.split("&name=")[0];
						stationsNames[noOfStations] = temp.split("&name=")[1];
						stationsNames[noOfStations] = stationsNames[noOfStations].replace('+', ' ');
						noOfStations++;
					}
					globalStationCodes = stationsCodes.clone();
					btnGetDates.setVisibility(Button.VISIBLE);
					spinStationsList.setVisibility(Spinner.VISIBLE);
					spinStationsList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stationsNames));
					
					
					
					//Toast.makeText(getApplicationContext(), "hello"+temp, Toast.LENGTH_LONG).show();
					//Toast.makeText(getApplicationContext(), stationsNames[noOfStations-1]+stationsCodes[noOfStations-1], Toast.LENGTH_LONG).show();
				}
			} catch (ClientProtocolException e) {
				Toast.makeText(getApplicationContext(), "Failed to fetch stations.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();  
			}
			}
		});
	    	
			return 1;
	    }    
	
}