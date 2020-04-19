package com.example.itemlocator;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * This class is displayed to the user when on the create item screen. It contains
 * A UPC field, a name field, an aisle field, a side of aisle radio button, and the GPS location fields.
 */
public class CreateProduct extends Fragment implements View.OnClickListener {

    private Button btnCreate, btnGetLocation;
    private DBHelper db;
    private EditText etUPC, etName, etAisle, etLong, etLat;
    private RadioGroup radioGroup;
    private RadioButton selectedButton;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Location currentLocation;

    /**
     * This method is used to inflate the view and manage when the location changes, if it does.
     * Also intiializes all the private EditText fields, etc.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.createproduct, container, false);

        db = new DBHelper(getActivity().getApplicationContext());

        etUPC = myView.findViewById(R.id.etUPC);
        etName = myView.findViewById(R.id.etName);
        etAisle = myView.findViewById(R.id.etAisle);
        etLong = myView.findViewById(R.id.etLong);
        etLat = myView.findViewById(R.id.etLat);
        radioGroup = myView.findViewById(R.id.rgSideOfAisleFound);
        btnCreate = myView.findViewById(R.id.btnCreate);
        btnGetLocation = myView.findViewById(R.id.btnGetLocation);

        btnGetLocation.setOnClickListener(this);
        btnCreate.setOnClickListener(this);

        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        return myView;
    }

    /**
     * This method manages the user clicking on the create item button and the get location button.
     * If the create button is clicked, it redirects the application to the create product method.
     * If the button is the get location button, it tries to find the location and populate the fields.
     * If the location is null, it generates a toast.
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnCreate:
                createProduct();
                break;
            case R.id.btnGetLocation:
                if (currentLocation == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "GPS location unavailable.", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    etLat.setText(String.valueOf(currentLocation.getLatitude()));
                    etLong.setText(String.valueOf(currentLocation.getLongitude()));
                }
                break;
        }
    }

    /**
     * This essentially pauses the GPS location listener to preserve battery life.
     */
    @Override
    public void onStop() {
        locationManager.removeUpdates(locationListener);
        super.onStop();
    }

    /**
     * This method starts the location listener again when the user opens the app.
     */
    @Override
    public void onResume() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
        } catch (SecurityException exp) {
            //Toast.makeText(getActivity().getApplicationContext(), exp.getMessage(), Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    /**
     * This method validates all the fields when creating a new product to store in the database.
     * If all fields are valid, it pushes the new product to the database.
     */
    private void createProduct() {
        selectedButton = getActivity().findViewById(radioGroup.getCheckedRadioButtonId());

        Product newProduct = new Product();

        boolean upcIsValid = false, nameIsValid = false, aisleIsValid = false, sideOfAisleIsValid = false, gpsLatIsValid = false, gpsLonIsValid = false;

        db.open();

        if (etUPC.getText().toString().isEmpty() && etName.getText().toString().isEmpty() && etAisle.getText().toString().isEmpty() && radioGroup.getCheckedRadioButtonId() == -1
                && etLat.getText().toString().isEmpty() && etLong.getText().toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "All fields empty, please fill in the fields to create a product.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etUPC.getText().toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "UPC field is empty, please fill in this field to proceed", Toast.LENGTH_SHORT).show();
        } else {
            newProduct.setUPC(etUPC.getText().toString());
            upcIsValid = true;
        }

        if (etName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Name field is empty, please fill in this field to proceed", Toast.LENGTH_SHORT).show();
        } else {
            newProduct.setName(etName.getText().toString());
            nameIsValid = true;
        }

        if (etAisle.getText().toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Aisle field is empty, please fill in this field to proceed", Toast.LENGTH_SHORT).show();
        } else {
            newProduct.setAisle(Integer.parseInt(etAisle.getText().toString()));
            aisleIsValid = true;
        }

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity().getApplicationContext(), "Side of Aisle field is empty, please fill in this field to proceed", Toast.LENGTH_SHORT).show();
        } else {
            newProduct.setSideOfAisle(selectedButton.getText().toString());
            sideOfAisleIsValid = true;
        }

        if (etLat.getText().toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Latitude field is empty, please fill in this field to proceed", Toast.LENGTH_SHORT).show();
        } else {
            if (etLat.getText().toString().matches("^[+-]?[0-9]{0,900}(?:\\.[0-9]{0,900})?$")) {
                newProduct.setLatitude(etLat.getText().toString());
                gpsLatIsValid = true;
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Latitude field contains invalid characters, please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        if (etLong.getText().toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Longitude field is empty, please fill in this field to proceed", Toast.LENGTH_SHORT).show();
        } else {
            if (etLong.getText().toString().matches("^[+-]?[0-9]{0,900}(?:\\.[0-9]{0,900})?$")) {
                newProduct.setLongitude(etLong.getText().toString());
                gpsLonIsValid = true;
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Longitude field contains invalid characters, please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        if (upcIsValid && nameIsValid && aisleIsValid && sideOfAisleIsValid && gpsLatIsValid && gpsLonIsValid) {
            db.createProduct(newProduct);
            db.close();

            etUPC.setText("");
            etName.setText("");
            etAisle.setText("");
            radioGroup.check(-1);
            etLat.setText("");
            etLong.setText("");
            Toast.makeText(getActivity().getApplicationContext(), "All fields valid, product created!", Toast.LENGTH_LONG).show();

        }

    }
}