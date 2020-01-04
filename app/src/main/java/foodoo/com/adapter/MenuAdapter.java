package foodoo.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import foodoo.com.R;

public class MenuAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<MenuList> mylist = new ArrayList<MenuList>();

    public MenuAdapter(ArrayList<MenuList> itemArray, Context mContext) {
        super();
        this.mContext = mContext;
        mylist = itemArray;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public String getItem(int position) {
        return mylist.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private TextView cart_qty,product_id;
        private TextView cart_name,product_size;
        private TextView cart_total_price,delete;
        private ImageView cart_image,bin;
        private LinearLayout parentLinearLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder view;
        LayoutInflater inflator = null;
        if (convertView == null) {
            view = new ViewHolder();
            try {

                inflator = ((Activity) mContext).getLayoutInflater();
                convertView = inflator.inflate(R.layout.extramealdetails, null);

                view.cart_qty= (TextView)convertView.findViewById(R.id.qty_cart);
                view.cart_name=(TextView)convertView.findViewById(R.id.cart_product);
                view.cart_total_price=(TextView)convertView.findViewById(R.id.cart_price);
                view.cart_image=(ImageView) convertView.findViewById(R.id.get_cart_image);
                view.product_size=(TextView)convertView.findViewById(R.id.product_size);
                view.product_id=(TextView)convertView.findViewById(R.id.product_id);

                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.cart_qty.setText(mylist.get(position).getCuisines());
            view.cart_name.setText(mylist.get(position).getName());
            view.cart_total_price.setText(mylist.get(position).getOpening_hours());
            view.product_size.setText(mylist.get(position).getAverage_cost());
            view.product_id.setText(mylist.get(position).getId());

            Glide.with(mContext)
                    .load(mylist.get(position).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .skipMemoryCache(true)
                    .into(view.cart_image);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
