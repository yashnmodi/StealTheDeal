package pentagon.stealthedeal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;
    private BottomNavigationView bottomNav;
    private FrameLayout mainFrame;
    private HomeFragment homeFragment;
    private FoodFragment foodFragment;
    private FashionFragment fashionFragment;
    private FunFragment funFragment;
    private ProfileFragment profileFragment;
    private Bundle bundle;

    public final static String ACTIVE_USER = "User Session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String activeUser = intent.getStringExtra(ACTIVE_USER);
        bundle = new Bundle();
        bundle.putString("userId",activeUser);

        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        toolbar.setTitle("Latest Deals");
        bottomNav = (BottomNavigationView) findViewById(R.id.bottomnav);
        mainFrame = (FrameLayout) findViewById(R.id.mainframe);
        homeFragment = new HomeFragment();
        foodFragment = new FoodFragment();
        fashionFragment = new FashionFragment();
        profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        funFragment = new FunFragment();

        setFragment(homeFragment);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.navhome:
                        setFragment(homeFragment);
                        toolbar.setTitle("Latest Deals");
                        return true;

                    case R.id.navfashion:
                        setFragment(fashionFragment);
                        toolbar.setTitle("Fashion Deals");
                        return true;

                    case R.id.navfood:
                        setFragment(foodFragment);
                        toolbar.setTitle("Food Deals");
                        return true;

                    case R.id.navfun:
                        setFragment(funFragment);
                        toolbar.setTitle("Entertainment Deals");
                        return true;

                    case R.id.navprofile:
                        setFragment(profileFragment);
                        toolbar.setTitle("Profile");
                        return true;

                        default:
                            return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
