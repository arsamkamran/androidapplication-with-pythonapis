package com.example.arsam.cloudcomputing;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static float total;
    String scanCode;
    Button btn_checkout,btn_clearcart;
    RecyclerView recyclerViewCart;
    public static CartItemsRecycler adapterCartItems;
    public static TextView tvtotal;
    ProgressDialog pDialog;
    RequestQueue queue;

    public static List<CartItemsModel> cartItemsModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pDialog = new ProgressDialog(MainActivity.this);
        queue = Volley.newRequestQueue(this);


        RecyclerView.LayoutManager layoutManagerCart = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCart = findViewById(R.id.recycler_cart_items);
        recyclerViewCart.setLayoutManager(layoutManagerCart);
        adapterCartItems = new CartItemsRecycler(this, MainActivity.cartItemsModels);
        recyclerViewCart.setAdapter(adapterCartItems);

        tvtotal = findViewById(R.id.total_cart_fragment);


        btn_clearcart = findViewById(R.id.btn_cart_clearcart);
        btn_clearcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cartItemsModels.clear();
                tvtotal.setText("0");
                MainActivity.total =0;
                adapterCartItems.notifyDataSetChanged();
            }
        });


        btn_checkout = findViewById(R.id.btn_cart_checkout);
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                dialogBuilder.setTitle("Search Product");
                dialogBuilder.setMessage("Enter Item ID");
                dialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getProduct(edt.getText().toString());
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
                //startQRScanner();

            }
        });


    }

    private void startQRScanner() {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("");
        integrator.setOrientationLocked(false);     // to change the orientation
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "No Barcode Scanned", Toast.LENGTH_LONG).show();
            } else {
                scanCode = result.getContents();
                Toast.makeText(this, scanCode, Toast.LENGTH_LONG).show();
                addToRecyclerCart();    //call api to add product
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addToRecyclerCart() {
        getProductInformation(scanCode);                //api call to get product information
    }

    private boolean itemAlreadyAdded(int id){
        for (int i=0;i<cartItemsModels.size();i++){
            if (cartItemsModels.get(i).getId() == id)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_item) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.add_product_layout_admin, null);
            dialogBuilder.setView(dialogView);

            final EditText name = (EditText) dialogView.findViewById(R.id.edt_name_add_product);
            final EditText itemid = (EditText) dialogView.findViewById(R.id.edt_barcode_add_product);

            dialogBuilder.setTitle("Add Product");
            dialogBuilder.setMessage("Enter details");
            dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //send api call with data
                    addProduct(name.getText().toString(),
                            Long.parseLong(itemid.getText().toString()));
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //pass
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
        }

        if (id == R.id.delete_item) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
            dialogBuilder.setView(dialogView);

            final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

            dialogBuilder.setTitle("Remove Product");
            dialogBuilder.setMessage("Enter Item ID");
            dialogBuilder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //send api call with data
                    removeProduct(Long.parseLong(edt.getText().toString()));
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //pass
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
        }

        if (id == R.id.edit_item){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
            dialogBuilder.setView(dialogView);

            final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

            dialogBuilder.setTitle("Search Product");
            dialogBuilder.setMessage("Enter Item ID");
            dialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    getProduct(edt.getText().toString());
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //pass
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
        }

        if (id == R.id.logout) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

//_______________________________________________________________________________________________________________________________________


    private void getProduct(String barcode){
        final String url = Server.IP + "/product/" + barcode;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        if (response.equals("product not found")){
                            Toast.makeText(MainActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            try {

                                JSONObject pd = new JSONObject(response.toString());
                                //JSONArray products = product.getJSONArray("product");
                                //JSONObject pd = products.getJSONObject(0);

                                if (itemAlreadyAdded(pd.getInt("itemId"))){
                                    Toast.makeText(MainActivity.this, "Item already added", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    CartItemsModel item = new CartItemsModel(
                                            pd.getInt("itemId")
                                            ,pd.getString("name"),
                                            1f,
                                            ((float) pd.getDouble("price")),
                                            ((float) pd.getDouble("price"))
                                            ,R.drawable.ic_minus,R.drawable.ic_plus);

                                    MainActivity.cartItemsModels.add(item);
                                    adapterCartItems.notifyDataSetChanged();

                                    for (int i =0;i<cartItemsModels.size();i++)
                                    {
                                        total = total + cartItemsModels.get(i).getTotal();
                                    }
                                    tvtotal.setText(String.valueOf(total));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Exception in get product", Toast.LENGTH_SHORT).show();
                            }
                        }

                        pDialog.dismiss();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "error");
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }


    private void getProductInformation(String barcode) {

        //SharedPref sp = new SharedPref(MainActivity.this);

        String url = Server.IP + "/product/" + barcode;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                Log.d("response:getProduct", response);

                if (response.equals("product not found")){
                    Toast.makeText(MainActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {

                        JSONObject pd = new JSONObject(response);
                        //JSONArray products = product.getJSONArray("product");
                        //JSONObject pd = products.getJSONObject(0);

                        if (itemAlreadyAdded(pd.getInt("itemId"))){
                            Toast.makeText(MainActivity.this, "Item already added", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            CartItemsModel item = new CartItemsModel(
                                    pd.getInt("itemId")
                                    ,pd.getString("name"),
                                    1f,
                                    ((float) pd.getDouble("price")),
                                    ((float) pd.getDouble("price"))
                                    ,R.drawable.ic_minus,R.drawable.ic_plus);

                            MainActivity.cartItemsModels.add(item);
                            adapterCartItems.notifyDataSetChanged();

                            for (int i =0;i<cartItemsModels.size();i++)
                            {
                                total = total + cartItemsModels.get(i).getTotal();
                            }
                            tvtotal.setText(String.valueOf(total));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Exception in get product", Toast.LENGTH_SHORT).show();
                    }
                }

                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void addProduct(String name, long itemid) {

        String url = Server.IP + "/addproduct?name=" + name +  "&barcode="+ itemid;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                int receiptid = Integer.parseInt(response);

                //send items to cart
                Toast.makeText(MainActivity.this, "Product Successfully added", Toast.LENGTH_SHORT).show();

                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void removeProduct(long barcode){

        String url = Server.IP + "/removeproduct?barcode=" + barcode;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                int id = Integer.parseInt(response);

                if(id == 1)
                {
                    Toast.makeText(MainActivity.this, "Product Successfully removed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Error: No such product exists", Toast.LENGTH_SHORT).show();
                }


                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

}
