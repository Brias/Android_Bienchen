package de.androidbienchen.data;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaDrm;
import android.os.Message;
import android.text.InputType;
import android.widget.EditText;
import de.androidbienchen.R;

public class UpdateDialogHelper {
	
	private static ProgressDialog update; 
	
	public static void setUpdateDialog(Context context){
		update = new ProgressDialog(context);
		update.setMessage(context.getResources().getString(R.string.updating_data));
		update.setCancelable(false);
		update.show();
	}
	
	public static void UpdateCanceledDialog(Context context, String status, String description){
		AlertDialog.Builder updateCanceled = buildUpdateDialog(context, context.getResources().getString(R.string.update_error), status, description);
		
		updateCanceled.create().show();
	}
	
	private static AlertDialog.Builder buildUpdateDialog(Context context, String title, String status, String description){
		AlertDialog.Builder updateDialog = new AlertDialog.Builder(context);
		updateDialog.setTitle(title);
		updateDialog.setMessage(status+description);
		updateDialog.setPositiveButton(context.getResources().getString(R.string.btn_ok), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		return updateDialog;
	}
	
	private static void UpdateSucceedDialog(Context context, String status, String description){
		AlertDialog.Builder updateSucceed = buildUpdateDialog(context, context.getResources().getString(R.string.update_succeed), status, description);
		updateSucceed.create().show();
	}
	
	public static void dismissUpdateDialog(Context context, String status, String description){
		update.dismiss();
		UpdateSucceedDialog(context, status, description);
	}
}
