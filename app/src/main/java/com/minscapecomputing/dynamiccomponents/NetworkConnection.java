package com.minscapecomputing.dynamiccomponents;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NetworkConnection {
	
	OnConnectedListener onConnectedListener;
	ConnectionDetector connectionDetector;
	
	public void isOnline(Context context) {
		connectionDetector = new ConnectionDetector(context);
		if (!connectionDetector.isConnectingToInternet()) {
			
			final Dialog serverConnErrDialog = new Dialog(context);
			serverConnErrDialog.setTitle(context.getResources().getString(R.string.alert_conn_err_title));
			serverConnErrDialog.setContentView(R.layout.layout_unexpected_error_dialog);
			((TextView) serverConnErrDialog.findViewById(R.id.txtMessage)).setText(context.getString(
							R.string.alert_network_error_message));
			final Button buttontryConn = (Button) serverConnErrDialog
					.findViewById(R.id.btnSerErrTrytoConnect);
			final Button buttonSupportEmail = (Button) serverConnErrDialog
					.findViewById(R.id.btnSupportEmail);

			buttonSupportEmail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) { }
			});

			buttontryConn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated
					if (connectionDetector.isConnectingToInternet()) {
						if (onConnectedListener != null)
							onConnectedListener.httpConnect();
						serverConnErrDialog.dismiss();
					}
				}
			});
			serverConnErrDialog.show();
		} else {
			if (onConnectedListener != null)
				onConnectedListener.httpConnect();
		}
	}
	
	public void setOnConnectedListener(OnConnectedListener onConnectedListener) {
		
		this.onConnectedListener = onConnectedListener;
	}
}