package com.example.citycat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class AppAlertDialog {
	
	public static void showNeutraAlertDialog(final Context context, String title, String msg, Intent goToActivity)
	{
		final Intent whatTodo = goToActivity;

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder
		//.setMessage("Ooops! One of the events details are invalid. Please Try again.")
		.setMessage(msg)
		.setCancelable(false)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {				
				if (whatTodo == null) dialog.cancel(); //cancel dialog
				else context.startActivity(whatTodo); //go to chosen activity
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

}
