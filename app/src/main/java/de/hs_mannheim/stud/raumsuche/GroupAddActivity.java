package de.hs_mannheim.stud.raumsuche;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hs_mannheim.stud.raumsuche.models.User;
import de.hs_mannheim.stud.raumsuche.views.adapters.GroupAddListAdapter;

public class GroupAddActivity extends AppCompatActivity {

    @Bind(R.id.group_add_name_input)
    EditText nameInput;

    @Bind(R.id.group_add_user_input)
    EditText userInput;

    @Bind(R.id.group_add_user_list)
    ListView userList;

    private GroupAddListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);
        ButterKnife.bind(this);

        initComponents();
    }

    private void initComponents() {
        adapter = new GroupAddListAdapter(this, new ArrayList<User>());
        userList.setAdapter(adapter);

    }


    @OnClick(R.id.group_add_user_addbutton)
    public void addUser() {
        String studentId = userInput.getText().toString();

        if(studentId != null && !studentId.isEmpty()) {
            User addedUser = new User();
            addedUser.setMtklNr(studentId);

            List<User> users = adapter.getUsers();

            users.add(addedUser);
            adapter.notifyDataSetChanged();
        }
    }
}
