package foodoo.com.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
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
import java.util.List;
import java.util.Map;
import foodoo.com.PatnerList;
import foodoo.com.R;
import foodoo.com.adapter.Category_ProductAdpater;
import foodoo.com.adapter.Category_ProductAdpater2;
import foodoo.com.adapter.Category_ProductModel;
import foodoo.com.adapter.Category_ProductModel2;
import foodoo.com.adapter.Top_offer_adapter;
import foodoo.com.adapter.Top_offers;
import foodoo.com.api.URLs;
import foodoo.com.model.ExpandableHeightGridView;
import foodoo.com.model.SliderUtils;
import foodoo.com.model.ViewPagerAdapter;
import foodoo.com.model.VolleySingleton;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private ImageView[] dots;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    List<SliderUtils> sliderImg;
    ViewPagerAdapter viewPagerAdapter;
    private static final String SHARED_PREF_NAME = "foodoopref";
    String token;
    Category_ProductAdpater category_productAdpater;
    Category_ProductAdpater2 category_productAdpater2;
    ArrayList<Category_ProductModel> category_productModels;
    ArrayList<Category_ProductModel2> category_productModels2;
    ExpandableHeightGridView expandableHeightGridView,expandableHeightGridView2;
    ProgressBar progressBar_pro,progressBar_pro2;
    Top_offer_adapter adapter;
    ArrayList<Top_offers> top_offers;
    RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = prefs.getString("token", null);

        category_productModels = new ArrayList<Category_ProductModel>();
        category_productModels2=new ArrayList<Category_ProductModel2>();
        top_offers=new ArrayList<Top_offers>();

        expandableHeightGridView=(ExpandableHeightGridView)root.findViewById(R.id.grid_imagelist_pro);
        expandableHeightGridView2=(ExpandableHeightGridView)root.findViewById(R.id.top_imagelist_pro);
        recyclerView=(RecyclerView)root.findViewById(R.id.top_offers_re);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        expandableHeightGridView.setExpanded(true);
        expandableHeightGridView2.setExpanded(true);

        expandableHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Category_ProductModel menuList= (Category_ProductModel) parent.getItemAtPosition(position);
                String menuList_id=menuList.getId();
                Intent intent= new Intent(getActivity(), PatnerList.class);
                intent.putExtra("menuList_id",menuList_id);
                startActivity(intent);

            }
        });

        sliderImg = new ArrayList<>();
        viewPager = (ViewPager)root.findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout)root.findViewById(R.id.layoutDots);
        CallProductList2();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                try {
                    for (int i = 0; i < dotscount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        progressBar_pro=(ProgressBar)root.findViewById(R.id.progressBar_pro);
        progressBar_pro2=(ProgressBar)root.findViewById(R.id.top_progressBar_pro);
        progressBar_pro.setVisibility(View.VISIBLE);

        return root;
    }

    public void sendRequest(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_BANNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray userJson = obj.getJSONArray("banners");
                    for (int i = 0; i < userJson.length(); i++) {
                        JSONObject itemslist = userJson.getJSONObject(i);
                        SliderUtils sliderUtils = new SliderUtils();
                        sliderUtils.setSliderImageUrl(itemslist.getString("path"));
                        sliderImg.add(sliderUtils);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                viewPagerAdapter = new ViewPagerAdapter(sliderImg, getActivity());
                viewPager.setAdapter(viewPagerAdapter);
                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];
                for(int i = 0; i < dotscount; i++){
                    dots[i] = new ImageView(getActivity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotspanel.addView(dots[i], params);
                }
                dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",token);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        Top_offer();
    }

    private void CallProductList(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_productcategory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("category");
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    String photo = itemslist.getString("image");

                                    Category_ProductModel productModel = new Category_ProductModel(id, name, photo);
                                    category_productModels.add(productModel);
                                }
                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            category_productAdpater = new Category_ProductAdpater(getActivity(),R.layout.category_product_list,category_productModels);
                            expandableHeightGridView.setAdapter(category_productAdpater);
                            category_productAdpater.notifyDataSetChanged();
                            progressBar_pro.setVisibility(View.GONE);
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
             {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",token);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        sendRequest();
    }

    private void CallProductList2(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_getheader,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("headers");
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    String photo = itemslist.getString("image");

                                    Category_ProductModel2 productModel = new Category_ProductModel2(id, name, photo);
                                    category_productModels2.add(productModel);
                                }
                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            category_productAdpater2 = new Category_ProductAdpater2(getActivity(),R.layout.category_product_list2,category_productModels2);
                            expandableHeightGridView2.setAdapter(category_productAdpater2);
                            category_productAdpater2.notifyDataSetChanged();
                            progressBar_pro2.setVisibility(View.GONE);
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
            {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",token);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        CallProductList();
    }

    private void Top_offer(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_offers,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("offers");
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("title");
                                    String photo = itemslist.getString("path");

                                    Top_offers top_offer = new Top_offers(id, name, photo);
                                    top_offers.add(top_offer);
                                }
                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            adapter = new Top_offer_adapter(getActivity(),top_offers);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
            {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",token);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}