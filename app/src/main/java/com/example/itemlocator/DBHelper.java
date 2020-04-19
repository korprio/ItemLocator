package com.example.itemlocator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "products.db", TABLE_NAME = "ProductList", ID = "_id", UPC = "UPC",
            PRODUCT_NAME = "Name", AISLE = "Aisle", SIDE_OF_AISLE = "SideOfAisle", LATITUDE_LOCATION = "Latitude",
            LONGITUDE = "Longitude";
    public static final int DB_VERSION = 1;

    public SQLiteDatabase sqlDB;

    /**
     * This method creates the table when the application is first called, if the file does not exist.
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sCreate = "CREATE TABLE " +
                TABLE_NAME + "(" +
                ID + " integer primary key autoincrement, " +
                UPC + " text not null, " +
                PRODUCT_NAME + " text not null, " +
                AISLE + " text not null, " +
                SIDE_OF_AISLE + " text not null, " +
                LATITUDE_LOCATION + " text not null, " +
                LONGITUDE + " text not null);";

        sqLiteDatabase.execSQL(sCreate);

    }

    /**
     * This method is called when upgrading the database.
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * Constructor for the databse helper class
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    /**
     * This method opens the dbhelper to be used for queries, and creating records.
     * @throws SQLException
     */
    public void open() throws SQLException {
        sqlDB = this.getWritableDatabase();
    }

    /**
     * This method closes the write-able database.
     */
    public void close() {
        sqlDB.close();
    }

    /**
     * This method creates a new record from the passed in product.
     * @param product
     * @return
     */
    public long createProduct(Product product) {

        ContentValues cvs = new ContentValues();

        cvs.put(UPC, product.getUPC());
        cvs.put(PRODUCT_NAME, product.getName());
        cvs.put(AISLE, String.valueOf(product.getAisle()));
        cvs.put(SIDE_OF_AISLE, product.getSideOfAisle());
        cvs.put(LATITUDE_LOCATION, product.getLatitude());
        cvs.put(LONGITUDE, product.getLongitude());

        return sqlDB.insert(TABLE_NAME, null, cvs);
    }

    /**
     * This method is used to update a record in the database. Currently not used in the GUI. Could add at a later time.
     * @param product
     * @return
     */
    public boolean updateProduct(Product product) {

        ContentValues cvs = new ContentValues();

        cvs.put(UPC, product.getUPC());
        cvs.put(PRODUCT_NAME, product.getName());
        cvs.put(AISLE, String.valueOf(product.getAisle()));
        cvs.put(SIDE_OF_AISLE, product.getSideOfAisle());
        cvs.put(LATITUDE_LOCATION, product.getLatitude());
        cvs.put(LONGITUDE, product.getLongitude());

        return sqlDB.update(TABLE_NAME, cvs, UPC + " = '" + product.getUPC() + "'", null) > 0;
    }

    /**
     * This method deletes a product by finding the record in the database by its UPC. Not used in the GUI currently.
     * @param product
     * @return
     */
    public boolean deleteProduct(Product product) {
        return sqlDB.delete(TABLE_NAME, UPC + " = '" + product.getUPC() + "'", null) > 0;
    }

    /**
     * This method returns all the products found in the database. Currently not used in the GUI.
     * @return
     */
    public Cursor getAllProducts() {
        String[] sFields = new String[]{UPC, PRODUCT_NAME, AISLE, SIDE_OF_AISLE, LATITUDE_LOCATION, LONGITUDE};
        return sqlDB.query(TABLE_NAME, sFields, null, null, null, null, null);
    }

    /**
     * THis method finds a product in the database by its name.
     * @param name
     * @return
     */
    public Cursor getProductByName(String name) {
        String[] sFields = new String[]{UPC, PRODUCT_NAME, AISLE, SIDE_OF_AISLE, LATITUDE_LOCATION, LONGITUDE};
        return sqlDB.query(TABLE_NAME, sFields, PRODUCT_NAME + " LIKE '%" + name + "%'", null, null, null, null);
    }

    /**
     * This method finds a product in the database by its UPC.
     * @param upc
     * @return
     */
    public Cursor getProductByUPC(String upc) {
        String[] sFields = new String[]{UPC, PRODUCT_NAME, AISLE, SIDE_OF_AISLE, LATITUDE_LOCATION, LONGITUDE};
        return sqlDB.query(TABLE_NAME, sFields, UPC + " LIKE '%" + upc + "%'", null, null, null, null);
    }

}
