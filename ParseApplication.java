  /////////////////////////
 //autentikacijska klasa//
/////////////////////////
package com.example.testapp;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import android.app.Application;


public class ParseApplication extends Application {
	//stringovi su inicijalizirani na "EXAMPLE" jer im je uloga kao pokazni primjer
   private static final String APPLICATION_ID="EXAMPLE";
	private static final String CLIENT_KEY="EXAMPLE";
	
	@Override
    public void onCreate() {
        super.onCreate();
        if(APPLICATION_ID!="" && CLIENT_KEY!=""){
        	Parse.initialize(this,APPLICATION_ID,CLIENT_KEY);
            ParseUser.enableAutomaticUser();
            ParseACL defaultACL = new ParseACL();
            defaultACL.setPublicReadAccess(true);
     
            ParseACL.setDefaultACL(defaultACL, true);
        	
        }
        
    }

}
