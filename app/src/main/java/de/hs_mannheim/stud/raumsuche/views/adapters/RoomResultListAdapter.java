package de.hs_mannheim.stud.raumsuche.views.adapters;

import android.content.Context;
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
import android.widget.Button;
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
    private Context context;
    private List<RoomResult> results;
    private RoomQuery query;
    private BuildingFactory buildingFactory;

    private LayoutInflater inflater;

    private OnGroupActionListener onGroupAction;

    private int selectedRoomResult = 0;
    private boolean enableGroupActions = false;

    public RoomResultListAdapter(Context context, List<RoomResult> roomResult, RoomQuery query) {
        super();

        this.context = context;
        this.results = roomResult;
        this.query = query;

        inflater = LayoutInflater.from(context);

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
        final RoomResult roomResult = results.get(position);
        final Room room = roomResult.getRoom();
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
        holder.size.setText(room.getSize() + " Plätze");

        if (enableGroupActions && position == selectedRoomResult) {
            holder.notifyGroup.setVisibility(View.VISIBLE);
            holder.notifyGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onGroupAction != null) {
                        onGroupAction.onGroupNotify(roomResult);
                    }
                }
            });
        } else {
            holder.notifyGroup.setVisibility(View.GONE);
        }

        GradientDrawable shape = (GradientDrawable) holder.building.getBackground();
        shape.setColor(color);

        return view;
    }

    private SpannableStringBuilder buildPropertiesSpannable(List<String> properties, int color) {

        String propertiesAsString = TextUtils.join(", ", properties);
        SpannableStringBuilder builder = new SpannableStringBuilder(propertiesAsString);

        if (query != null) {
            int alphaColor = Color.argb(48, Color.red(color), Color.green(color), Color.blue(color));

            for (String searchedProperty : query.getProperties()) {
                int propertyIndex = propertiesAsString.indexOf(searchedProperty);

                if (propertyIndex != -1) {
                    builder.setSpan(new BackgroundColorSpan(alphaColor), propertyIndex, propertyIndex + searchedProperty.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }
        }
        return builder;
    }

    public void setSelectedRoom(int position) {
        selectedRoomResult = position;
        notifyDataSetChanged();
    }

    public void setEnableGroupActions(boolean enableGroupActions) {
        this.enableGroupActions = enableGroupActions;
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
        @Bind(R.id.room_size)
        TextView size;
        @Bind(R.id.room_notify_group)
        Button notifyGroup;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setOnGroupAction(OnGroupActionListener onGroupAction) {
        this.onGroupAction = onGroupAction;
    }

    public interface OnGroupActionListener {
        void onGroupNotify(RoomResult room);
    }
}
