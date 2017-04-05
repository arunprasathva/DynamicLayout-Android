package com.minscapecomputing.dynamiccomponents;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpRequest {
	
	private int intial = 0;

	public static final int CONNECTION_TIMEOUT = 10;
	public static final int UPLOAD_TIMEOUT = 10;
	public static final int READ_TIMEOUT = 30;

	public static final int RETRY_COUNT = 2;

	private OnResponseListener onResponseListener;
	private OnStartStopListener onStartStopListener;

	public synchronized void httpPost(final Activity act, final Request request, final ProgressDialog pd, final boolean showDialog) {
		
		NetworkConnection network = new NetworkConnection();
		network.setOnConnectedListener(new OnConnectedListener() {
			@Override
			public void httpConnect() {
				new GetData(act, request, pd, showDialog).execute();
			}
		});
		network.isOnline(act);
	}

	class GetData extends AsyncTask<String, Void, String> {
		
		Activity act;
		Request request;
		ProgressDialog pb;
		boolean showDialog;
		
		public GetData(Activity act, Request request, ProgressDialog pb, boolean showDialog) {
			// TODO Auto-generated constructor stub
			this.act = act;
			this.request = request;
			this.pb = pb;
			this.showDialog = showDialog;
			if (onStartStopListener != null)
				onStartStopListener.onStart();
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				if (showDialog && pb != null) {
					pb.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			try {
				intial++;
				OkHttpClient client = new OkHttpClient.Builder()
						.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
						.writeTimeout(UPLOAD_TIMEOUT, TimeUnit.SECONDS)
						.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
						.build();

				Response response = client.newCall(request).execute();
				return response.body().string();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.e("TAG", "res-"+result);
			try {
				if (result.length() <= 0 && intial < RETRY_COUNT) {
					new GetData(act, request, pb, showDialog).execute();
				} else {
					if (onResponseListener != null) 
						onResponseListener.onResponse(result);
				}
			} catch (Exception e) {
				// TODO: handle exception
				if (intial < RETRY_COUNT) {
					new GetData(act, request, pb, showDialog).execute();
				}
			} finally {
				if (pb != null && pb.isShowing())
					pb.dismiss();
				if (onStartStopListener != null)
					onStartStopListener.onStop();
			}
		}
	}

	public void setOnResponseListener(OnResponseListener onResponseListener) {
		
		this.onResponseListener = onResponseListener;
	}

	public void setOnStartStopListener(OnStartStopListener onStartStopListener) {
		this.onStartStopListener = onStartStopListener;
	}

	public interface OnStartStopListener {
		void onStart();
		void onStop();
	}
}
