package com.example.testapp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.parse.ParseFile;
import com.parse.ParseObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore;

public class MainActivity extends ActionBarActivity {
	private static final int REQUEST_TAKE_PHOTO = 1;
	private static final int CAM_REQUEST=1313;
	private static final String FILE_NAME="Save";
	
	Button btnTakePhoto,btnShowLocation,btnSubmit;
	ImageView imgTakenPhoto;
	TrackerGPS gps;
	private double latitude,longitude;
	private boolean isPhotoTaken=false; 
	private String mCurrentPhotoPath,cleanFullPath;
	private File photoFile = null;
	private FileOutputStream outputStream;
	private Coordinats coord; 
	private TextView filecon;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		filecon = (TextView)findViewById(R.id.filecon);
		btnSubmit=(Button)findViewById(R.id.button3);
		btnShowLocation=(Button)findViewById(R.id.button2);
		btnTakePhoto=(Button)findViewById(R.id.button1);
		imgTakenPhoto=(ImageView)findViewById(R.id.imageView1);
		
		
		//prikaži spremljene kooridinate
		btnShowLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
			      MyFileReader fop = new MyFileReader();
			      String text = fop.read(FILE_NAME);
			      if(text != null){
			      filecon.setText(text);
			      }
			      else {
			        Toast.makeText(getApplicationContext(), "File not Found", Toast.LENGTH_SHORT).show();
			        filecon.setText(null);
			      }
				
			}
		});
		
	//upload-aj fotku	
		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(isPhotoTaken){
	                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
	                        R.id.container);
	                ByteArrayOutputStream stream = new ByteArrayOutputStream();
	                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	                byte[] image = stream.toByteArray();
	 
	                ParseFile file = new ParseFile("cleanFullPath", image);
	                file.saveInBackground();
	 
	                ParseObject imgupload = new ParseObject("ImageUpload");
	 
	                imgupload.put("ImageName", "AndroidBegin Logo");
	                imgupload.put("ImageFile", file);
	                imgupload.saveInBackground();
	                Toast.makeText(MainActivity.this, "Image Uploaded",
	                        Toast.LENGTH_SHORT).show();
	                isPhotoTaken=false;
					
				}
				
			}
		});
		
		//aktiviraj kameru i uzmi lokaciju i spremi koordinate u file
		btnTakePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				gps=new TrackerGPS(MainActivity.this);
				
				if(i.resolveActivity(getPackageManager())!=null)
				{
					startActivityForResult(i,CAM_REQUEST);
					isPhotoTaken=true;
					dispatchTakePictureIntent();
					
				}
				if(gps.canGetLocation()){
					latitude=gps.getLatitude();
					longitude=gps.getLongitude();
					Toast.makeText(getApplicationContext(),"Your Locaton is -LAT\n"+latitude+"\nLONG\n "
																		+longitude,Toast.LENGTH_LONG).show();
					coord=new Coordinats(latitude, longitude);
					try {
						  outputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
						  outputStream.write(coord.toString().getBytes());
						  outputStream.close();
						} catch (Exception e) {
						  e.printStackTrace();
						}
					
					
				}else{
					gps.showSettingsAlert();
				}
				
			}
		});

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			
		}
	}

	//generiraj ime
	public void OnActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode==CAM_REQUEST && resultCode==RESULT_OK)
		{
			Bundle bndl= new Bundle();
			bndl=data.getExtras();
			Bitmap bmp;
			bmp=(Bitmap)bndl.get("data");
			imgTakenPhoto.setImageBitmap(bmp);
		}
	}
	
	//generiraj ime photo
	private File createImageFile() throws IOException {
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  
	        ".jpg",         
	        storageDir      
	    );
	    cleanFullPath=image.getAbsolutePath();
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}

 //napravi photo.jpg
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	        	ex.printStackTrace();
	        }
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
