package com.example.itemlocator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class is responsible for creating a fragment to display the user.
 */
public class HomeScreenFragment extends Fragment implements View.OnClickListener {

    private Button btnSearch;
    private EditText etSearch;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.homescreen, container, false);

        btnSearch = myView.findViewById(R.id.btnSearch);
        etSearch = myView.findViewById(R.id.etSearch);
        dbHelper = new DBHelper(getActivity().getApplicationContext());

        btnSearch.setOnClickListener(this);
        return myView;
    }

    /**
     * The method is responsible for finding a product in the database. Displays a toast if no product is found.
     */
    @Override
    public void onClick(View view) {

        dbHelper.open();
        Cursor foundProduct = null;

        if (etSearch.getText().toString().matches("^\\d+$")) {
            foundProduct = dbHelper.getProductByUPC(etSearch.getText().toString());
        }
        else if (etSearch.getText().toString().matches("^\\D+$")) {
            foundProduct = dbHelper.getProductByName(etSearch.getText().toString());
        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(),"Please refine your search paramters, no item was found.",Toast.LENGTH_SHORT).show();
        }

        if (foundProduct != null) {
            if (foundProduct.getCount() > 0) {
                foundProduct.moveToFirst();
                Intent newIntent = new Intent(getActivity().getApplicationContext(), FoundItem.class);
                newIntent.putExtra("UPC", foundProduct.getString(0));
                newIntent.putExtra("ProductName", foundProduct.getString(1));
                newIntent.putExtra("Aisle", foundProduct.getString(2));
                newIntent.putExtra("SideOfAisle", foundProduct.getString(3));
                newIntent.putExtra("GPS Lat", foundProduct.getString(4));
                newIntent.putExtra("GPS Long", foundProduct.getString(5));
                this.startActivity(newIntent);
            }
        }

        dbHelper.close();
    }
}