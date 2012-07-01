package pankaj.cdac.trainstatus;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class TrainStatusActivity extends Activity{
	
	String trainNumber,stationCode, queryDate;
	Button btnBack,btnNewQuery;
	WebView trainStatusView;
	GetDateActivity dummy = new GetDateActivity();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trainstatus);
		bindItems();
		myOnClickListeners();
		getStatus();
	}


	private void bindItems() {
		btnBack = (Button) findViewById(R.id.btnBack);
		btnNewQuery = (Button) findViewById(R.id.btnNewQuery);
		trainStatusView = (WebView) findViewById(R.id.webTrainStatus);
		trainNumber=getIntent().getStringExtra("trainNumber");
		stationCode = getIntent().getStringExtra("stationCode");
		queryDate = getIntent().getStringExtra("queryDate");
	}
	
	
	private void myOnClickListeners() {
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				TrainStatusActivity.this.finish();
			}
		});
		
		btnNewQuery.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				dummy.finish();
				TrainStatusActivity.this.finish();
			}
		});
		
	}
	
	private void getStatus() {
		
	    String url = "http://www.trainenquiry.com/o/RunningIslTrSt.aspx?tr="+trainNumber+"&st="+stationCode+"&dt="+Uri.encode(queryDate);		
		DefaultHttpClient httpClient = new DefaultHttpClient();
    	HttpGet httpGet = new HttpGet(url);
    	ResponseHandler<String> responseString = new BasicResponseHandler();
    	trainStatusView.setWebViewClient(new Callback());
    	WebSettings ws = trainStatusView.getSettings();
    	ws.setBuiltInZoomControls(true);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";

    	try {
			String page = httpClient.execute(httpGet, responseString);
			if(page.contains("Train may not be running on \'"+queryDate+"\'")){
				String html = "<span style=\"color:Chocolate;font-family:Verdana;font-size:10pt;font-weight:bold;\">Sorry no information found for Running Status of the Train '12359'  and Station 'PNBE'. Train may not be running on '25/06/2012' , Please check the days of run for the selected train & station. Try again.</span>";
				trainStatusView.loadDataWithBaseURL("", html, mimeType, encoding, "");
			}
			else{
				page = page.split(queryDate+"</span></P>")[1];
				//Toast.makeText(getApplicationContext(), page, Toast.LENGTH_LONG).show();
				page=page.split("<TD bgColor=\"#FFF4EA\" width=\"251\" height=\"54\">")[0];
				trainStatusView.loadDataWithBaseURL("", page, mimeType, encoding, "");
			}
				
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Failed to fetch Data.", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    private class Callback extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}
    }
}