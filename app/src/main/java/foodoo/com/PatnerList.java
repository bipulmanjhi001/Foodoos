package foodoo.com;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import foodoo.com.adapter.MenuAdapter;
import foodoo.com.adapter.MenuList;
import foodoo.com.api.URLs;
import foodoo.com.model.VolleySingleton;

public class PatnerList extends AppCompatActivity {

    ProgressBar progressBar;
    ListView listView;
    MenuAdapter menuAdapter;
    ArrayList<MenuList> menuLists;
    private static final String SHARED_PREF_NAME = "foodoopref";
    String token,pro_id;
    ImageView back_from_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patner_list);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pro_id = bundle.getString("menuList_id");
        }

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = prefs.getString("token", null);
        progressBar=(ProgressBar)findViewById(R.id.p_pro);

        listView=(ListView)findViewById(R.id.P_list);
        PartnerList();
        menuLists=new ArrayList<MenuList>();

        back_from_cart=(ImageView)findViewById(R.id.back_from_cart);
        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(PatnerList.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void PartnerList(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_partnerlist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("partners");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    String cuisines= itemslist.getString("cuisines");
                                    String opening_hours= itemslist.getString("opening_hours");
                                    String average_cost= itemslist.getString("average_cost");
                                    String image= itemslist.getString("image");

                                    MenuList menuList=new MenuList();
                                    menuList.setId(id);
                                    menuList.setName(name);
                                    menuList.setImage(image);
                                    menuList.setCuisines(cuisines);
                                    menuList.setOpening_hours(opening_hours);
                                    menuList.setAverage_cost(average_cost);
                                    menuLists.add(menuList);

                                }
                            }else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        try {
                            progressBar.setVisibility(View.GONE);
                            menuAdapter = new MenuAdapter(menuLists, PatnerList.this);
                            listView.setAdapter(menuAdapter);
                            menuAdapter.notifyDataSetChanged();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
            {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("category_id",pro_id);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(PatnerList.this, Dashboard.class);
        startActivity(intent);
        finish();
        return;
    }
}
