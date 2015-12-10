package de.hs_mannheim.stud.raumsuche;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {

    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

        initializeButtons();
    }

    private void initializeButtons() {
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mail senden an matrikelnr@stud.hs-mannheim.de

                showConfirmationDialog();
            }
        });
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // create confirmation message
        builder.setMessage(getString(R.string.signUpConfirmationMessage))
                .setTitle(getString(R.string.signUpConfirmationMessageTitle));

        // add OK button
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SignUpActivity.this.finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
