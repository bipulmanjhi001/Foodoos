package foodoo.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import foodoo.com.R;
import foodoo.com.model.Utils;

public class Category_ProductAdpater2 extends ArrayAdapter<Category_ProductModel2> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Category_ProductModel2> mGridData = new ArrayList<Category_ProductModel2>();

    public Category_ProductAdpater2(Context mContext, int layoutResourceId, ArrayList<Category_ProductModel2> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Category_ProductModel2> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.category_pro_name);
            holder.url=(TextView)row.findViewById(R.id.category_pro_url);
            holder.id=(TextView)row.findViewById(R.id.category_pro_id);
            holder.imageView = (ImageView) row.findViewById(R.id.category_pro_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Category_ProductModel2 item = mGridData.get(position);
        holder.titleTextView.setText(item.getName());
        holder.url.setText(item.getUrl());
        holder.id.setText(item.getId());

        Utils.fetchSvg(mContext, item.getUrl(), holder.imageView);

        return row;
    }
    private static class ViewHolder {
        TextView titleTextView;
        TextView url,id;
        ImageView imageView;
    }
}

