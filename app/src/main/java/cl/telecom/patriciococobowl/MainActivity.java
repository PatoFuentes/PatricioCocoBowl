package cl.telecom.patriciococobowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cl.telecom.patriciococobowl.Clases.Pedido;
import cl.telecom.patriciococobowl.adapter.PedidoAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1000;

    EditText nombre, telefono;
    Button btnAgregar;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;
    TextView tvLatitud, tvLongitud, tvDireccion;
    RecyclerView mRecycle;
    PedidoAdapter mAdapter;

    private FirebaseFirestore mfirestore;
    LocationManager locationManager;
    Localizacion locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mfirestore = FirebaseFirestore.getInstance();
        mRecycle = findViewById(R.id.recyclerViewPedidos);
        mRecycle.setLayoutManager(new LinearLayoutManager(this));
        Query query = mfirestore.collection("pedidos");
        FirestoreRecyclerOptions<Pedido> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pedido>().setQuery(query, Pedido.class).build();
        mAdapter = new PedidoAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycle.setAdapter(mAdapter);

        nombre = findViewById(R.id.textNombre);
        telefono = findViewById(R.id.textTelefono);
        checkBox1 = findViewById(R.id.checkBox);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        btnAgregar = findViewById(R.id.btnPedir);
        tvLatitud = findViewById(R.id.tvlatitud);
        tvLongitud = findViewById(R.id.tvlongitud);
        tvDireccion = findViewById(R.id.tvdireccion);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new Localizacion();
        locationListener.setMainActivity(this);

        checkLocationPermissionAndStart();

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nombre.getText().toString().trim();
                String phone = telefono.getText().toString().trim();
                String address = tvDireccion.getText().toString().trim();


                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese los datos", Toast.LENGTH_SHORT).show();
                } else {
                    // Obtener la dirección actual y pasarla a postProductos
                    postProductos(name, phone, address);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
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

    private void postProductos(String name, String phone, String address) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", name);
        map.put("telefono", phone);
        map.put("direccion", address); // Agregar la dirección al mapa

        if (checkBox1.isChecked()) {
            map.put("producto1", "Pan con Churrasco Lechuga");
        }
        if (checkBox2.isChecked()) {
            map.put("producto2", "Pan con Pollo Lechuga");
        }
        if (checkBox3.isChecked()) {
            map.put("producto3", "Bowl Pollo, Lechuga, Choclo y Zanahoria");
        }
        if (checkBox4.isChecked()) {
            map.put("producto4", "Bowl Pollo a la plancha, Lechuga, Choclo y Zanahoria");
        }
        if (checkBox5.isChecked()) {
            map.put("producto5", "Moster Blanco");
        }

        mfirestore.collection("pedidos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Creacion exitosa", Toast.LENGTH_SHORT).show();
                // Limpiar campos después del éxito
                nombre.setText("");
                telefono.setText("");
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al enviar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(@NonNull Location loc) {
            tvLatitud.setText(String.valueOf(loc.getLatitude()));
            tvLongitud.setText(String.valueOf(loc.getLongitude()));
            this.mainActivity.setLocation(loc);
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
