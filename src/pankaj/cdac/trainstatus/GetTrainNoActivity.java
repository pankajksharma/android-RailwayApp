package pankaj.cdac.trainstatus;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class GetTrainNoActivity extends Activity {
	
	EditText edtTrainName;
	Button btnSubmit;
	WebView wvTrainDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findtrainnumber);
		bindItems();
		myListener();
	}

	private void bindItems() {
		edtTrainName = (EditText) findViewById(R.id.edtTrainName);
		btnSubmit = (Button) findViewById(R.id.btnFindTrainNo);
		wvTrainDetails = (WebView) findViewById(R.id.webTainNames);
	}
	
	private void myListener(){
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if(edtTrainName.getText().toString().length()<2){
					Toast.makeText(getApplicationContext(), "Atleast provide 1 character.", Toast.LENGTH_LONG).show();
					edtTrainName.setText("");
					return;
				}		
				
				getTrainDetails();
			}

			private void getTrainDetails() {
				wvTrainDetails.setVisibility(WebView.VISIBLE);
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String url= edtTrainName.getText().toString().trim();
		    	HttpGet httpGet = new HttpGet("http://www.indiantrains.org/train-list/?navigate="+url.replace(' ', '+'));
		    	ResponseHandler<String> responseString = new BasicResponseHandler();
		    	wvTrainDetails.setWebViewClient(new Callback());
		    	WebSettings ws = wvTrainDetails.getSettings();
		    	ws.setBuiltInZoomControls(true);
		        final String mimeType = "text/html";
		        final String encoding = "UTF-8";

		    	try {
					String page = httpClient.execute(httpGet, responseString);
					if(page.contains("<b>Destination Station</b></td></tr></table>")){
						Toast.makeText(getApplicationContext(), "Could Not find any Train.\nPlease try again.", Toast.LENGTH_LONG).show();
						wvTrainDetails.setVisibility(WebView.INVISIBLE);
					}
					else{
						page = page.split("font-size:150%;\">"+edtTrainName.getText().toString()+"</h3>")[1];
						//Toast.makeText(getApplicationContext(), page, Toast.LENGTH_LONG).show();
						page=page.split("</a></td></tr></table>")[0]+"</a></td></tr></table>";
						wvTrainDetails.loadDataWithBaseURL("", page, mimeType, encoding, "");
					}
						
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Failed to fetch Data.", Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	private class Callback extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//return super.shouldOverrideUrlLoading(view, url);
			return true;
		}
    }
}
	