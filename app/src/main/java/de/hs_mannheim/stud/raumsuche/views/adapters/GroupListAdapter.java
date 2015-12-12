package de.hs_mannheim.stud.raumsuche.views.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hs_mannheim.stud.raumsuche.R;
import de.hs_mannheim.stud.raumsuche.managers.BuildingFactory;
import de.hs_mannheim.stud.raumsuche.managers.UserManager;
import de.hs_mannheim.stud.raumsuche.models.Building;
import de.hs_mannheim.stud.raumsuche.models.Group;
import de.hs_mannheim.stud.raumsuche.models.Room;
import de.hs_mannheim.stud.raumsuche.models.RoomResult;
import de.hs_mannheim.stud.raumsuche.models.User;

/**
 * Created by Martin on 12/12/15.
 */
public class GroupListAdapter extends BaseAdapter{

    private Resources res;

    private List<Group> groups;
    private User user;

    private LayoutInflater inflater;

    public GroupListAdapter(Context context, List<Group> groups){
        super();

        this.res = context.getResources();
        this.groups = groups;
        this.inflater = LayoutInflater.from(context);

        UserManager manager = UserManager.getInstance(context);
        this.user = manager.getUser();
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        Group group = groups.get(position);

        return group.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Group group = groups.get(position);

        ViewHolder holder;
        View view = convertView;

        if(view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.list_item_groups, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.name.setText(group.getName());

        User owner = group.getOwner();

        if(user.getMtklNr().equals(owner.getMtklNr())) {
            holder.owner.setVisibility(View.VISIBLE);
        } else {
            holder.owner.setVisibility(View.GONE);
        }

        String userString = buildUsersString(group.getUsers());
        holder.users.setText(userString);

        return view;
    }

    private String buildUsersString(List<User> users) {
        StringBuilder userStringBuilder = new StringBuilder();

        if(users.size() > 3) {
            int displayedUserCount = 0, index = 0;

            do {
                User participant = users.get(index);

                if(!user.getMtklNr().equals(participant.getMtklNr())) {
                    userStringBuilder.append(participant.getDisplayName());
                    userStringBuilder.append(", ");
                }

            } while (displayedUserCount < 2);

            userStringBuilder.append("+");
            userStringBuilder.append(users.size() - 2);
        } else {

            for(User participant : users) {
                if(!user.getMtklNr().equals(participant.getMtklNr())) {
                    userStringBuilder.append(participant.getDisplayName());
                    userStringBuilder.append(", ");
                }
            }

            userStringBuilder.append(res.getString(R.string.you));
        }

        return userStringBuilder.toString();
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.group_item_name)
        TextView name;
        @Bind(R.id.group_item_users)
        TextView users;
        @Bind(R.id.group_item_owner)
        TextView owner;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
