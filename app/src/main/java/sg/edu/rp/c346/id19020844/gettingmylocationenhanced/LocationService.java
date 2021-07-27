package sg.edu.rp.c346.id19020844.gettingmylocationenhanced;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileWriter;

public class LocationService extends Service {

    boolean started;
    FusedLocationProviderClient client;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    String folderLocation;

    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder {
        public void startDetection() {

            if (checkPermission()) {
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(30);
                mLocationRequest.setSmallestDisplacement(500);

                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult != null) {
                            Location data = locationResult.getLastLocation();
                            double lat = data.getLatitude();
                            double lng = data.getLongitude();

                            String msg = lat + "\n" + lng;

                            Toast.makeText(LocationService.this, msg, Toast.LENGTH_SHORT).show();

                            try {
                                folderLocation = getFilesDir().getAbsolutePath() + "/MyFolder";
                                File targetFile = new File(folderLocation, "data.txt");
                                FileWriter writer = new FileWriter(targetFile, false);
                                writer.write(msg);
                                writer.flush();
                                writer.close();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            }
        }


        public void stopDetection() {
            if (client != null) {
                client.removeLocationUpdates(mLocationCallback);
            }
        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        client = LocationServices.getFusedLocationProviderClient(LocationService.this);

        folderLocation = getFilesDir().getAbsolutePath() + "/MyFolder";
        File folder = new File(folderLocation);
        if (folder.exists() == false) {
            boolean result = folder.mkdir();
            if (result == true) {
                Log.d("File Read/Write", "Folder created");
            }
        }
    }

    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}