package pankaj.cdac.trainstatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PnrActivity extends Activity{
	EditText edtPnrNo;
	Button btnGetPnrStatus;
	WebView wvTrainDetails;
	TextView txtTrainDetails;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pnrstatus);
		bindItems();
		myListener();
	}


	private void bindItems() {
		edtPnrNo = (EditText) findViewById(R.id.edtPnrNumber);
		btnGetPnrStatus = (Button) findViewById(R.id.btnGetPnr);
		wvTrainDetails = (WebView) findViewById(R.id.webPnrStatus);
		txtTrainDetails = (TextView) findViewById(R.id.txtPnrInfo);
	}
	
	private void myListener(){
		btnGetPnrStatus.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if(edtPnrNo.getText().toString().length()<10){
					Toast.makeText(getApplicationContext(), "Invalid PNR", Toast.LENGTH_LONG).show();
					edtPnrNo.setText("");
					return;
				}		
				
				getTrainDetails();
			}

			private void getTrainDetails() {
				wvTrainDetails.setVisibility(WebView.VISIBLE);
				DefaultHttpClient httpClient = new DefaultHttpClient();		  
				HttpPost httpPost = new HttpPost("http://www.indianrail.gov.in/cgi_bin/inet_pnrstat_cgi.cgi");
				
		    	wvTrainDetails.setWebViewClient(new Callback());
		    	WebSettings ws = wvTrainDetails.getSettings();
		    	ws.setBuiltInZoomControls(true);
		        final String mimeType = "text/html";
		        final String encoding = "UTF-8";

		    	try {
		    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		    		nameValuePairs.add(new BasicNameValuePair("lccp_pnrno1", edtPnrNo.getText().toString()));
		    		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    		HttpResponse response  = httpClient.execute(httpPost);
		    		HttpEntity entity = response.getEntity();
		    		String page = EntityUtils.toString(entity);
					wvTrainDetails.loadDataWithBaseURL("", page, mimeType, encoding, "");
					if(page.contains("<BR><H2 align=\"center\"> FLUSHED PNR / PNR NOT YET GENERATED</H2>")){
						Toast.makeText(getApplicationContext(), "PNR number not Valid.", Toast.LENGTH_LONG).show();
						wvTrainDetails.setVisibility(WebView.INVISIBLE);
						txtTrainDetails.setVisibility(TextView.INVISIBLE);
					}
					else{
						page = page.split("<td width=\"6%\">Class</Td>")[1];
						String info = page.split("</TR>")[1];
						String usableInfo[] = info.split("<TD class=\"table_border_both\">");
						
						String trainDetails = "";
						for(int i=1;i<usableInfo.length;i++)
						{
							usableInfo[i] = usableInfo[i].split("</TD>")[0];
							//Toast.makeText(getApplicationContext(), usableInfo[i], Toast.LENGTH_SHORT).show();
						}
						
						trainDetails += "Train No: "+usableInfo[1].replace('*', ' ')+"\n";
						trainDetails += "Train Name: "+usableInfo[2]+"\n";
						trainDetails += "Boarding Date: "+usableInfo[3]+"\n";
						trainDetails += "Boarding Point: "+usableInfo[7]+"\n";
						trainDetails += "Reserved Upto: "+usableInfo[6]+"\n";
						
						txtTrainDetails.setVisibility(TextView.VISIBLE);
						txtTrainDetails.setText(trainDetails);
						
						page = page.split("</TR></TABLE>")[1];
						page = page.split("<td align=\"left\" valign=\"top\" class=\"pad_self\"><table width=\"100%\" border=\"0\" cellpadding=\"2\" cellspacing=\"2\">")[0];
						page = page.replace("Booking Status <br /> (Coach No , Berth No., Quota)", "Booking Status");
						page = page.replace("Current Status <br />(Coach No , Berth No.)", "Current Status)");
						page = page.replace("Passenger", "P");
						
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
			return true;
		}
    }
}