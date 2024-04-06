package ru.project.waygo.adapter;

import static ru.project.utils.BitMapUtils.getBitmapFromDrawable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.project.waygo.Constants;
import ru.project.waygo.R;
import ru.project.waygo.details.PointDetailsActivity;
import ru.project.waygo.details.RouteDetailsActivity;
import ru.project.waygo.fragment.LocationFragment;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<LocationFragment> fragments;
    private final Context context;

    public LocationAdapter(Context context, List<LocationFragment> fragments){
        this.fragments = fragments;
        this.context =  context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_location, parent,false);
        return new LocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        LocationFragment fragment = fragments.get(position);
        holder.name.setText(fragment.getName());
        holder.description.setText(fragment.getDescription());
        holder.routeLength.setVisibility(fragment.getTypeLocation().equals(Constants.TypeLocation.ROUTE)
                ? View.VISIBLE
                : View.INVISIBLE);
        holder.routeLength.setText(fragment.getRouteLength());
        holder.image.setImageBitmap(fragment.getImages() != null
                        && !fragment.getImages().isEmpty()
                        ? fragment.getImages().get(0)
                        : getBitmapFromDrawable(context, R.drawable.location_test));
        holder.favorite.setChecked(fragment.isFavorite());

        if(fragment.getTypeLocation().equals(Constants.TypeLocation.POINT)) {
            Intent intent = new Intent(holder.itemView.getContext(), PointDetailsActivity.class);
            intent.putExtra("id", fragment.getLocationId());
            intent.putExtra("name", fragment.getName());
            intent.putExtra("description", fragment.getDescription());
            intent.putExtra("favorite", fragment.isFavorite());
            intent.putExtra("routes", fragment.getExtra());

            holder.gotoButton.setOnClickListener(e -> {
                context.startActivity(intent);
            });
        } else {
            Intent intent = new Intent(holder.itemView.getContext(), RouteDetailsActivity.class);
            intent.putExtra("id", fragment.getLocationId());
            intent.putExtra("name", fragment.getName());
            intent.putExtra("description", fragment.getDescription());
            intent.putExtra("favorite", fragment.isFavorite());
            intent.putExtra("length", fragment.getRouteLength());
            intent.putExtra("points", fragment.getExtra());

            holder.gotoButton.setOnClickListener(e -> {
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView name;
        final TextView description;
        final ImageView image;
        final TextView routeLength;
        final ToggleButton favorite;

        final Button gotoButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_location);
            description = itemView.findViewById(R.id.descriprion);
            image = itemView.findViewById(R.id.image);
            routeLength = itemView.findViewById(R.id.route_length);
            favorite = itemView.findViewById(R.id.toggle_favorite);
            gotoButton = itemView.findViewById(R.id.button_goto);
        }
    }
}