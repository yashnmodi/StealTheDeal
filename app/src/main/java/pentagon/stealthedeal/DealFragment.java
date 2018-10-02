package pentagon.stealthedeal;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DealFragment extends Fragment {

    private static final String TAG = "Error";
    private String dealId;
    FirebaseDatabase database;
    DatabaseReference dealTable;
    String dealData[];
    Toolbar toolbar;
    ImageView imageView;
    TextView descText;
    TextView validityText;
    TextView address;
    Button callShop;
    Button mapBtn;

    public DealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_deal, container, false);

        dealId = getArguments().getString("id");
        database = FirebaseDatabase.getInstance();
        dealTable = database.getReference("deals").child(dealId);
        dealData = new String[7];   //seq- store, poster, validity, description, address, callnum, keywords

        toolbar = (Toolbar) getActivity().findViewById(R.id.maintoolbar);
        imageView = (ImageView) view.findViewById(R.id.dealimg);
        descText = (TextView) view.findViewById(R.id.description);
        validityText = (TextView) view.findViewById(R.id.validity);
        address = (TextView) view.findViewById(R.id.address);
        callShop = (Button) view.findViewById(R.id.callshop);
        mapBtn = (Button) view.findViewById(R.id.locate);

        dealTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dealData[0] = dataSnapshot.child("store").getValue().toString();
                dealData[1] = dataSnapshot.child("poster").getValue().toString();
                dealData[2] = dataSnapshot.child("description").getValue().toString();
                dealData[3] = dataSnapshot.child("validity").getValue().toString();
                dealData[4] = dataSnapshot.child("address").getValue().toString();
                dealData[5] = dataSnapshot.child("phnnum").getValue().toString();
                dealData[6] = dataSnapshot.child("keywords").getValue().toString();
                toolbar.setTitle(dealData[0]);
                Picasso.get().load(dealData[1]).into(imageView);
                descText.setText("Offer details:\n\t\t\t"+dealData[2]);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date endDate = formatter.parse(dealData[3]);
                    validityText.setText("Offer Expiry Date:\n\t\t\t"+endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                address.setText("Address:\n\t\t\t"+dealData[4]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, databaseError.getMessage() );
            }
        });

        callShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+dealData[5]));
                startActivity(intent);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String geolocation = "geo:0,0?q="+Uri.encode(dealData[4]);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(geolocation));
                startActivity(intent);
            }
        });

        return view;
    }
}
