package com.BlackBerryJuice;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.InputStream;
import java.io.OutputStream;

public class Constant {
	
	// API URL configuration
	static String AdminPageURL = "http://unix-team.ir/FlatlabRTL/";
	static String Update_ProfileURL = AdminPageURL + "api/update.php";
	static String Update_Message = AdminPageURL + "api/update-message.php";
	static String Message_Send = AdminPageURL + "api/recieve-message.php";
	static String SliderImageURL = AdminPageURL + "upload/slider/";
	static String GalleryImageURL = AdminPageURL + "upload/gallery/";
	static String CategoryAPI = AdminPageURL + "api/get-all-category-data.php";
	static String GalleryAPI = AdminPageURL + "api/get-all-image-gallery.php";
	static String SliderAPI = AdminPageURL + "api/get-all-image-slider.php";
	static String MenuAPI = AdminPageURL + "api/get-menu-data-by-category-id.php";
	static String TaxCurrencyAPI = AdminPageURL + "api/get-tax-and-currency.php";
	static String MenuDetailAPI = AdminPageURL + "api/get-menu-detail.php";
	static String SendDataAPI = AdminPageURL + "api/add-reservation.php";
	static String Login = AdminPageURL + "api/login.php";
	static String Register = AdminPageURL + "api/register.php";

	// change this access similar with accesskey in admin panel for security reason
	static String AccessKey = "12345";
	
	// database path configuration
	static String DBPath = "/data/data/com.BlackBerryJuice/databases/";
	
	// method to check internet connection
	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// method to handle images from server
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

}
