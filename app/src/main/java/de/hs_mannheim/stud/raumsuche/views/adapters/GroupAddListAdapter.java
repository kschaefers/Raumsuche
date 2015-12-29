package de.hs_mannheim.stud.raumsuche.views.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hs_mannheim.stud.raumsuche.R;
import de.hs_mannheim.stud.raumsuche.managers.UserManager;
import de.hs_mannheim.stud.raumsuche.models.Group;
import de.hs_mannheim.stud.raumsuche.models.User;

/**
 * Created by Martin on 12/16/15.
 */
public class GroupAddListAdapter extends BaseAdapter {

    Resources res;
    LayoutInflater inflater;

    Group group;
    List<User> users;
    User myUser;


    public GroupAddListAdapter(Context context, Group group) {
        this.res = context.getResources();
        this.group = group;
        this.users = group.getUsers();

        this.inflater = LayoutInflater.from(context);

        UserManager manager = UserManager.getInstance(context);
        this.myUser = manager.getUser();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final User user = users.get(i);

        ViewHolder holder;
        View view = convertView;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.list_item_group_form, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if (user.getMtklNr().equals(myUser.getMtklNr())) {
            holder.name.setText(res.getText(R.string.you));
        } else {
            holder.name.setText(user.getDisplayName());
        }

        if (user.getName() != null && !user.getName().isEmpty()) {
            holder.studentid.setVisibility(View.VISIBLE);
            holder.studentid.setText(user.getMtklNr());
        } else {
            holder.studentid.setVisibility(View.GONE);
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                users.remove(user);
                notifyDataSetChanged();
            }
        });

        if (myUser.getMtklNr().equals(group.getOwner())) {
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }

        if (user.getMtklNr().equals(group.getOwner())) {
            holder.owner.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            holder.owner.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public List<User> getUsers() {
        return users;
    }

    static class ViewHolder {
        @Bind(R.id.group_form_user_item_deletebutton)
        ImageButton deleteButton;
        @Bind(R.id.group_form_user_item_name)
        TextView name;
        @Bind(R.id.group_form_user_item_studentid)
        TextView studentid;
        @Bind(R.id.group_form_user_item_owner)
        TextView owner;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
