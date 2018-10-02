package pentagon.stealthedeal;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button logOut;
    private TextView nameBox;
    private TextView prefrences;
    private TextView changePwd;
    private TextView gender;
    private TextView fun;
    private TextView food;
    private TextView fashion;
    private TextView dev;
    private LinearLayout ll;
    private FirebaseDatabase database;
    private DatabaseReference userTable;
    private String curentPwd;
    private TextView mailBox;
    public EditText oldPwd;
    public EditText newPwd;
    private AlertDialog alertDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameBox = (TextView) view.findViewById(R.id.namebox);
        mailBox = (TextView) view.findViewById(R.id.mailbox);
        prefrences = (TextView) view.findViewById(R.id.preferences);
        gender =(TextView) view.findViewById(R.id.sex);
        fun =(TextView) view.findViewById(R.id.funlabel);
        food =(TextView) view.findViewById(R.id.foodlabel);
        fashion =(TextView) view.findViewById(R.id.fashionlabel);
        dev = (TextView) view.findViewById(R.id.developer);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        ll.setVisibility(View.GONE);
        changePwd = (TextView) view.findViewById(R.id.changepwd);
        logOut = (Button) view.findViewById(R.id.logoutbtn);

        database = FirebaseDatabase.getInstance();
        final String userId[] = getArguments().getString("userId").split("@");
        userTable = database.getReference("users").child(userId[0]);
        userTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue(String.class);
                String mail = dataSnapshot.child("Email").getValue(String.class);
                curentPwd = dataSnapshot.child("Password").getValue(String.class);
                nameBox.setText(name);
                mailBox.setText(mail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        });

        prefrences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userTable.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String sex = dataSnapshot.child("Gender").getValue(String.class);
                        String funAns = dataSnapshot.child("Fun").getValue(String.class);
                        String foodAns = dataSnapshot.child("Food").getValue(String.class);
                        String fashionAns = dataSnapshot.child("Fashion").getValue(String.class);
                        gender.setText("Gender:\t\t"+sex);
                        fun.setText("Fun:\t\t\t"+funAns);
                        food.setText("Food:\t\t"+foodAns);
                        fashion.setText("Fashion:\t\t"+fashionAns);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(ll.getVisibility() == View.GONE)
                    ll.setVisibility(View.VISIBLE);
                else
                    ll.setVisibility(View.GONE);
            }
        });

        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LayoutInflater inflater = getActivity().getLayoutInflater();

                alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.app_name)
                        .setView(inflater.inflate(R.layout.change_password, null))
                        .setMessage("Change password here")
                        .setPositiveButton("Change", null)
                        .setNegativeButton("Cancel", null)
                        .create();
                alertDialog.show();

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newPwd = (EditText) alertDialog.findViewById(R.id.newPwd);
                        oldPwd = (EditText) alertDialog.findViewById(R.id.oldPwd);
                        String pwdo = oldPwd.getText().toString();
                        String pwdn = newPwd.getText().toString();
                        if(pwdo.equals(curentPwd)){
                            userTable.child("Password").setValue(pwdn);
                            Toast.makeText(getContext(), "Password changed successfully.", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }else {
                            Toast.makeText(getContext(), "Incorect password. Try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                Toast.makeText(getActivity(),"Logged out successfully!", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/yashnmodi/"));
                startActivity(browserIntent);
            }
        });

        return view;
    }

    private void openDialog() {

    }
}
