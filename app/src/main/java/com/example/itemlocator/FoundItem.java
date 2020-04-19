package com.example.itemlocator;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * This class is responsible for displaying the found product to the user. Contains
 * a field for the products UPC, Name, Aisle, SideOfAisle and GPS location.
 */
public class FoundItem extends AppCompatActivity {

    private EditText etUPC, etName, etAisle, etLat, etLong;
    private RadioGroup radioGroup;
    private Button btnback;
    private TextView itemFound;

    /**
     * This inflates the view and gets references to all the views in the layout, as well as the button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.founditem);

        etUPC = findViewById(R.id.etUPCFound);
        etName = findViewById(R.id.etNameFound);
        etAisle = findViewById(R.id.etAisleFound);
        etLat = findViewById(R.id.etLatFound);
        etLong = findViewById(R.id.etLongFound);
        radioGroup = findViewById(R.id.rgSideOfAisleFound);
        btnback = findViewById(R.id.btnBack);
        itemFound = findViewById(R.id.tvProductNameFound);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#d64035"));
        actionBar.setBackgroundDrawable(colorDrawable);

        /*
        This button closes the activity.
         */
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        etUPC.setText(intent.getStringExtra("UPC"));
        etName.setText(intent.getStringExtra("ProductName"));
        etAisle.setText(intent.getStringExtra("Aisle"));
        etLat.setText(intent.getStringExtra("GPS Lat"));
        etLong.setText(intent.getStringExtra("GPS Long"));
        String message = "You have found " + intent.getStringExtra("ProductName") + "!";
        itemFound.setText(message);

        etUPC.setEnabled(false);
        etName.setEnabled(false);
        etAisle.setEnabled(false);
        etLat.setEnabled(false);
        etLong.setEnabled(false);

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }
        radioGroup.setClickable(false);

        radioGroup.check(intent.getStringExtra("SideOfAisle").equals("North") ? R.id.rdNorthFound : R.id.rdSouthFound);

    }
}
