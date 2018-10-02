package pentagon.stealthedeal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity{

    private Button signinBtn;
    private EditText et1;
    private EditText et2;
    private TextView signupLink;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mauthlistener;

    public final static String ACTIVE_USER = "User Session";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mauth = FirebaseAuth.getInstance();

        signinBtn = (Button) findViewById(R.id.signInBtn);
        et1 = (EditText) findViewById(R.id.username);
        et2 = (EditText) findViewById(R.id.password);
        signupLink = (TextView) findViewById(R.id.signuplink);

        mauthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(ACTIVE_USER, et1.getText().toString());
                    startActivity(intent);
                }
            }
        };

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignin();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(mauthlistener);
    }

    public void startSignin()
    {
        String email = et1.getText().toString();
        String password = et2.getText().toString();

        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this, "Please enter a valid Email and Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mauth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(!task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity.this,"Authentication Failed!",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Succesfully Logged In!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
}