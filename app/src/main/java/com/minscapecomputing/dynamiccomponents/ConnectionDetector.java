package com.minscapecomputing.dynamiccomponents;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionDetector {

	private Context _context;

	public ConnectionDetector(Context context) {
		this._context = context;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();

			if (info != null) {

				for (int i = 0; i < info.length; i++) {
					Log.w("INTERNET:", String.valueOf(i));

					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Log.w("INTERNET:", "connected!");
						return true;
					}
				}
			}
		}
		return false;
	}

}
