package com.felight.flashlight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class FlashActivity extends Activity {

	Switch stflash;
	Camera camera;
	boolean isFlashOn;
	boolean hasFlash;
	Parameters params;
	MediaPlayer mp;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash);
		stflash = (Switch) findViewById(R.id.stflash);

		hasFlash = getApplicationContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		if (!hasFlash) {
			AlertDialog alert = new AlertDialog.Builder(FlashActivity.this)
					.create();
			alert.setTitle("Error");
			alert.setMessage("Device Don't Have Flash");
			alert.setButton(null, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			alert.show();
			return;
		}
		getCamera();
		stflash.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFlashOn) {
					flashOff();
				} else {
					flashOn();
				}

			}
		});
	}

	// Open Camera
	private void getCamera() {
		if (camera == null) {
			try {
				camera = Camera.open();
				params = camera.getParameters();
			} catch (RuntimeException e) {
				Toast.makeText(this, "Failed to open camera",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// Flash On Method
	private void flashOn() {
		if (!isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(params);
			camera.startPreview();
			isFlashOn = true;

		}
	}

	private void flashOff() {
		if (isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(params);
			camera.stopPreview();
			isFlashOn = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flash, menu);
		return true;
	}

}
