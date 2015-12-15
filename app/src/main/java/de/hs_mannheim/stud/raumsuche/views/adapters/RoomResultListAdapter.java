package de.hs_mannheim.stud.raumsuche.views.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hs_mannheim.stud.raumsuche.R;
import de.hs_mannheim.stud.raumsuche.managers.BuildingFactory;
import de.hs_mannheim.stud.raumsuche.models.Building;
import de.hs_mannheim.stud.raumsuche.models.Room;
import de.hs_mannheim.stud.raumsuche.models.RoomQuery;
import de.hs_mannheim.stud.raumsuche.models.RoomResult;

/**
 * Created by m.christmann on 03.12.2015.
 */
public class RoomResultListAdapter extends BaseAdapter {
    Context context;
    List<RoomResult> results;
    RoomQuery query;
    BuildingFactory buildingFactory;

    LayoutInflater inflater;

    String availabilitySingleBlock;
    String availabilityMultipleBlocks;

    public RoomResultListAdapter(Context context, List<RoomResult> roomResult, RoomQuery query) {
        super();

        this.context = context;
        this.results = roomResult;
        this.query = query;

        inflater = LayoutInflater.from(context);
        Resources res = context.getResources();

        availabilitySingleBlock = res.getString(R.string.availability_single_block);
        availabilityMultipleBlocks = res.getString(R.string.availability_multiple_blocks);

        buildingFactory = BuildingFactory.getInstance(context);
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        RoomResult room = results.get(position);

        return room.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RoomResult roomResult = results.get(position);
        Room room = roomResult.getRoom();
        Building building = buildingFactory.getBuildingByIdentifier(room.getBuilding());

        ViewHolder holder;
        View view = convertView;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.list_item_rooms, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        int color = building.getBuildingColor();

        holder.identifier.setText(room.getName());
        holder.building.setText(room.getBuilding());

        holder.availability.setText(roomResult.getAvailable());

        SpannableStringBuilder propertiesSpannable = buildPropertiesSpannable(room.getRoomProperties(), color);
        holder.properties.setText(propertiesSpannable, TextView.BufferType.SPANNABLE);

        GradientDrawable shape = (GradientDrawable) holder.building.getBackground();
        shape.setColor(color);

        return view;
    }


    private SpannableStringBuilder buildPropertiesSpannable(List<String> properties, int color) {

        String propertiesAsString = TextUtils.join(", ", properties);
        SpannableStringBuilder builder = new SpannableStringBuilder(propertiesAsString);

        int alphaColor = Color.argb(48, Color.red(color), Color.green(color), Color.blue(color));

        for (String searchedProperty : query.getProperties()) {
            int propertyIndex = propertiesAsString.indexOf(searchedProperty);

            if (propertyIndex != -1) {
                builder.setSpan(new BackgroundColorSpan(alphaColor), propertyIndex, propertyIndex + searchedProperty.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }

        return builder;
    }

    public void setResults(List<RoomResult> results) {
        this.results = results;
    }

    public void setQuery(RoomQuery query) {
        this.query = query;
    }

    static class ViewHolder {
        @Bind(R.id.room_building)
        TextView building;
        @Bind(R.id.room_identifier)
        TextView identifier;
        @Bind(R.id.room_availability)
        TextView availability;
        @Bind(R.id.room_properties)
        TextView properties;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
