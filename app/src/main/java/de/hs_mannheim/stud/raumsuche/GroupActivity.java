package de.hs_mannheim.stud.raumsuche;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hs_mannheim.stud.raumsuche.models.Group;
import de.hs_mannheim.stud.raumsuche.views.adapters.GroupListAdapter;

public class GroupActivity extends AppCompatActivity {

    @Bind(R.id.group_list)
    ListView list;

    @Bind(R.id.group_list_empty)
    TextView emptyText;

    @Bind(R.id.group_loading_progress)
    ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        initComponents();
        loadGroups();
    }

    @OnClick(R.id.group_add_button)
    public void addGroup() {

    }

    private void initComponents() {
        // Initialize group list
        GroupListAdapter adapter = new GroupListAdapter(this, new ArrayList<Group>());
        list.setAdapter(adapter);
    }

    private void loadGroups() {

    }
}
