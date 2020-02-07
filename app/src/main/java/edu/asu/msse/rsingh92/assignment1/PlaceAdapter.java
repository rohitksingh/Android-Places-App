package edu.asu.msse.rsingh92.assignment1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private Context context;
    private List<PlaceDescription> list;
    private ListClickListener listClickListener;

    public PlaceAdapter(Context context, List<PlaceDescription> list){
        this.context = context;
        this.list = list;
        listClickListener = (ListClickListener)context;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        final PlaceDescription place = list.get(position);
        holder.name.setText(place.getName().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClickListener.itemClicked(place);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView name;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
