package pentagon.stealthedeal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity{

    private EditText f1;
    private EditText f2;
    private EditText f3;
    private EditText f4;
    private Button signUp;
    private CheckBox funCB, foodCB, fashionCB;
    private RadioGroup rbg;
    private String gender;
    private CheckBox tnc;
    private TextView signInLink;
    private DatabaseReference userTable;
    private FirebaseDatabase onedb97;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        onedb97 = FirebaseDatabase.getInstance();
        userTable = onedb97.getReference("users");
        mAuth = FirebaseAuth.getInstance();
        f1 = findViewById(R.id.newUserName);
        f2 = findViewById(R.id.newPassword);
        f3 = findViewById(R.id.conPassword);
        f4 = findViewById(R.id.newName);
        rbg=(RadioGroup) findViewById(R.id.radioGender);
        funCB = (CheckBox)findViewById(R.id.checkBox1);
        foodCB = (CheckBox)findViewById(R.id.checkBox2);
        fashionCB = (CheckBox)findViewById(R.id.checkBox3);
        tnc = findViewById(R.id.tnc);
        signUp = findViewById(R.id.signupbtn);
        signInLink = findViewById(R.id.signinlink);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignup();
            }
        });

        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void startSignup() {
        final String email = f1.getText().toString();
        final String[] uname = email.split("@");
        final String password = f2.getText().toString();
        String conpwd = f3.getText().toString();
        final String name = f4.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(SignupActivity.this, "Please enter Email ID or password", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(conpwd)) {
                if (tnc.isChecked()) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(SignupActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                                    DatabaseReference row = userTable.child(uname[0]);
                                    row.child("Email").setValue(email);
                                    row.child("Password").setValue(password);
                                    row.child("Name").setValue(name);
                                    row.child("Gender").setValue(gender);
                                    if(funCB.isChecked())
                                        row.child("Fun").setValue("Yes");
                                    else
                                        row.child("Fun").setValue("No");
                                    if(foodCB.isChecked())
                                        row.child("Food").setValue("Yes");
                                    else
                                        row.child("Food").setValue("No");
                                    if(fashionCB.isChecked())
                                        row.child("Fashion").setValue("Yes");
                                    else
                                        row.child("Fashion").setValue("No");
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                                        Log.e("Account Create: failed" + task.getException(), String.valueOf(task));
                                    } else {
                                        Log.e("task", String.valueOf(task));

                                    }
                                }
                            });
                } else
                    Toast.makeText(SignupActivity.this, "Please accept terms and condition!", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(SignupActivity.this, "Password do not match.", Toast.LENGTH_SHORT).show();
        }
    } //end of fn

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioMale:
                if (checked)
                    gender = "Male";
                    break;
            case R.id.radioFemale:
                if (checked)
                    gender = "Female";
                    break;
        }
    }
}
