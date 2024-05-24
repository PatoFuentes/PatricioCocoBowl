package cl.telecom.patriciococobowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1000;

    TextView tvLatitud, tvLongitud, tvDireccion;
    LocationManager locationManager;
    Localizacion locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); // Asegúrate de que este es el layout correcto

        tvLatitud = findViewById(R.id.tvlatitud);
        tvLongitud = findViewById(R.id.tvlongitud);
        tvDireccion = findViewById(R.id.tvdireccion);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new Localizacion();
        locationListener.setMainActivity(this);

        checkLocationPermissionAndStart();
    }

    private void checkLocationPermissionAndStart() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            locationStart();
        }
    }

    private void locationStart() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            tvLatitud.setText("Localización GPS");
            tvDireccion.setText("");
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al iniciar las actualizaciones de ubicación", Toast.LENGTH_SHORT).show();
        }
    }

    public void setLocation(Location loc) {
        if (loc != null && loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    tvDireccion.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
            } else {
                Toast.makeText(this, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class Localizacion implements LocationListener {
        MainActivity2 mainActivity2;

        public void setMainActivity(MainActivity2 mainActivity2) {
            this.mainActivity2 = mainActivity2;
        }

        @Override
        public void onLocationChanged(@NonNull Location loc) {
            tvLatitud.setText(String.valueOf(loc.getLatitude()));
            tvLongitud.setText(String.valueOf(loc.getLongitude()));
            this.mainActivity2.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            tvLatitud.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            tvLatitud.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("Location Status", "AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("Location Status", "OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("Location Status", "TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
}
