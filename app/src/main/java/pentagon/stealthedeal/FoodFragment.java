package pentagon.stealthedeal;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment {

    private static final String TAG = "Errors";
    ArrayList textArray = new ArrayList();
    ArrayList idArray = new ArrayList();
    ArrayList imgArray = new ArrayList();
    ListView lv;
    FoodFragment.MyAdapter adapter;
    FirebaseDatabase database;
    DealFragment dealFragment;
    Bundle bundle;


    public FoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        lv = (ListView) view.findViewById(R.id.foodlist);

        database = FirebaseDatabase.getInstance();
        DatabaseReference dealsTable = database.getReference("deals");

        dealsTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textArray.clear();
                imgArray.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String id = postSnapshot.getKey().toString();
                    String store = postSnapshot.child("store").getValue(String.class);
                    String img = postSnapshot.child("poster").getValue(String.class);
                    String key = postSnapshot.child("keywords").getValue(String.class);
                    if(key.equals("Food")) {
                        idArray.add(id);
                        textArray.add(store);
                        imgArray.add(img);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, databaseError.getMessage());
            }
        });

        adapter = new FoodFragment.MyAdapter(getContext(), R.layout.fragment_food, textArray, imgArray);

        lv.setAdapter(adapter);

        dealFragment = new DealFragment();
        bundle = new Bundle();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getId = idArray.get(position).toString();
                bundle.putString("id", getId);
                dealFragment.setArguments(bundle);
                setFragment(dealFragment);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
    public class MyAdapter extends ArrayAdapter {
        private ArrayList text;
        private ArrayList image;
        private int resource;
        private LayoutInflater inflater;

        public MyAdapter(Context context, int resource, ArrayList text, ArrayList image) {
            super(context, resource, text);
            this.text = text;
            this.image = image;
            this.resource = resource;
            inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            FoodFragment.MyAdapter.ViewHolder holder = null;

            if (convertView == null) {
                holder = new FoodFragment.MyAdapter.ViewHolder();
                convertView = inflater.inflate(resource, null);

                holder.Texts = (TextView) convertView.findViewById(R.id.text);
                holder.Images = (ImageView)convertView.findViewById(R.id.image);

                convertView.setTag(holder);
            } else {
                holder = (FoodFragment.MyAdapter.ViewHolder) convertView.getTag();
            }
            Picasso.get().load(image.get(position).toString()).into(holder.Images);
            holder.Texts.setText(text.get(position).toString());
            return convertView;
        }

        class ViewHolder {
            private ImageView Images;
            private TextView Texts;

        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
