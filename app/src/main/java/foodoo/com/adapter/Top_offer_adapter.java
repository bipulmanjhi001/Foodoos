package foodoo.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import foodoo.com.R;

public class Top_offer_adapter extends RecyclerView.Adapter<Top_offer_adapter.MyViewHolder> {

    private List<Top_offers> moviesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id,title;
        public ImageView path;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.top_title);
            id = (TextView) view.findViewById(R.id.top_id);
            path = (ImageView) view.findViewById(R.id.top_path);
        }
    }

    public Top_offer_adapter(Context context,List<Top_offers> moviesList) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Top_offers movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.id.setText(movie.getId());
        Picasso.get()
                .load(movie.getPath())
                .fit().centerCrop()
                .into(holder.path);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
