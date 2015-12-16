package de.hs_mannheim.stud.raumsuche.views.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hs_mannheim.stud.raumsuche.R;
import de.hs_mannheim.stud.raumsuche.managers.UserManager;
import de.hs_mannheim.stud.raumsuche.models.User;

/**
 * Created by Martin on 12/16/15.
 */
public class GroupAddListAdapter extends BaseAdapter {

    Resources res;
    LayoutInflater inflater;

    List<User> users;
    User myUser;


    public GroupAddListAdapter(Context context, List<User> users) {
        this.res = context.getResources();
        this.users = users;

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
        User user = users.get(i);

        ViewHolder holder;
        View view = convertView;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.list_item_group_add, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if (user.getMtklNr().equals(myUser.getMtklNr())) {
            holder.name.setText(res.getText(R.string.you));
        } else {
            holder.name.setText(user.getDisplayName());
        }

        if(user.getName() != null && !user.getName().isEmpty()) {
            holder.studentid.setText(user.getMtklNr());
        }

        return view;
    }

    public List<User> getUsers() {
        return users;
    }

    static class ViewHolder {
        @Bind(R.id.group_add_user_name)
        TextView name;
        @Bind(R.id.group_add_user_studentid)
        TextView studentid;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
