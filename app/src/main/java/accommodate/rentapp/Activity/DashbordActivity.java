package accommodate.rentapp.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import accommodate.rentapp.Fragments.ChatsFragment;
import accommodate.rentapp.Fragments.ExploreFragment;
import accommodate.rentapp.Fragments.HomeFragment;
import accommodate.rentapp.Fragments.MyPostFragment;
import accommodate.rentapp.Fragments.ProfilesFragment;
import accommodate.rentapp.Fragments.ShortlistedFragment;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;
import accommodate.rentapp.Utils.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class DashbordActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, BottomNavigationView.OnNavigationItemSelectedListener {


    Activity activity;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        activity = this;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        setNavigationDrawer();
        findIds();


    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
        }
        return true;
    }


    private void signOut() {

        Globaldata.SignOut(activity);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    private void findIds() {
        frameLayout = (FrameLayout) findViewById(R.id.fragment);
        this.bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (PreferenceManager.getSelectHouseType().equalsIgnoreCase(Datakey.Owner)) {
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation_owner);
            loadFragment(new ChatsFragment());
        } else if (PreferenceManager.getSelectHouseType().equalsIgnoreCase(Datakey.Tenant)) {
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation_dashbord);
            loadFragment(new HomeFragment());
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    public void setNavigationDrawer() {

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        toolbar.setTitle(getResources().getString(R.string.app_name) + " (" + PreferenceManager.getSelectHouseType() + ")");


        View headerView = navigationView.getHeaderView(0);

        ImageView userphoto = headerView.findViewById(R.id.userphoto);
        TextView username = headerView.findViewById(R.id.username);
        TextView email_id = headerView.findViewById(R.id.email_id);


        Glide.with(activity).load(PreferenceManager.getUserprofile()).error(R.drawable.ic_user).into(userphoto);
        username.setText(PreferenceManager.getUsername());
        email_id.setText(PreferenceManager.getUserEmail());


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.id_logout) {
                    signOut();
                } else if (item.getItemId() == R.id.id_home) {
                    loadFragment(new MyPostFragment());
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.id_home) {
            loadFragment(new HomeFragment());
        } else if (item.getItemId() == R.id.id_Chat) {
            loadFragment(new ChatsFragment());
        } else if (item.getItemId() == R.id.id_explore) {
            loadFragment(new ExploreFragment());
        } else if (item.getItemId() == R.id.id_shortlist) {
            loadFragment(new ShortlistedFragment());
        } else if (item.getItemId() == R.id.id_profile) {
            loadFragment(new ProfilesFragment());
        } else if (item.getItemId() == R.id.id_mypost) {
            loadFragment(new MyPostFragment());
        }
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}