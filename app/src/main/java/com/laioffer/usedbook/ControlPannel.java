package com.laioffer.usedbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.laioffer.usedbook.Adapaters.UserAdapter;
import com.laioffer.usedbook.Entity.Book;
import com.laioffer.usedbook.Entity.ChatList;
import com.laioffer.usedbook.Entity.User;
import com.laioffer.usedbook.Fragments.MainFragment;
import com.laioffer.usedbook.Fragments.PostFragment;
//import com.laioffer.usedbook.Fragments.PostFragment;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ControlPannel extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    TextView username;
    ImageView profile;
    NavigationView nav_left, nav_right;
    EditText search;

    RecyclerView recyclerView;
    private List<User> mUsers;
    private UserAdapter userAdapter;

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    private List<String> usersList;


    private ImageView menu;
    private ImageView chat;


    private RadioGroup searchSetting;
    private EditText searchBox;
    private Button searchButton;
    private String searchColumn;
    private String userId;



    //storage
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask storageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_pannel);


        init();
        //search book
        userId = FirebaseAuth.getInstance().getUid();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBox.getText().toString();
                if (query.length() == 0) {
                    Toast.makeText(getBaseContext(), "Tell us a little more on the book...", Toast.LENGTH_SHORT).show();
                    return;
                }

                int id = searchSetting.getCheckedRadioButtonId();
                searchColumn = id == R.id.search_title_radio_button ? "intitle:" : "inauthor:";

                Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                intent.putExtra(getString(R.string.query_intent_key), query);
                intent.putExtra(getString(R.string.column_intent_key), searchColumn);
                intent.putExtra(getString(R.string.user_name_intent_key), userId);

                startActivity(intent);
            }
        });
        //chat window     search
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //pop up drawerLayout by click
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });


        //upload profile
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile.setImageResource(R.drawable.upload_img);
                } else {
                    Glide.with(getApplicationContext())
                            .load(user.getImageURL())
                            .apply(RequestOptions.circleCropTransform())
                            .into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatList chatList = snapshot.getValue(ChatList.class);

                    if (chatList.getReceiver().equals(firebaseUser.getUid())) {
                        usersList.add(chatList.getSender());
                    }
                    if (chatList.getSender().equals((firebaseUser.getUid()))) {
                        usersList.add(chatList.getReceiver());
                    }
                }

                readChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nav_left.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.drawer_return) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(ControlPannel.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                return true;
            }

            ;
        });


        //init LocationTracker
        final LocationTracker mLocationTracker = new LocationTracker(this);
        mLocationTracker.getLocation();


        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                final TextView location_textview = (TextView) nav_left.getHeaderView(0).findViewById(R.id.location);

                mLocationTracker.getLocation();
                final double longitude = mLocationTracker.getLongitude();
                final double latitude = mLocationTracker.getLatitude();

                location_textview.setText("Lat=" + new DecimalFormat(".##").
                        format(latitude) + ",Lon=" + new DecimalFormat(".##").
                        format(longitude));
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


//        //set map view
//        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, MainFragment.newInstance()).commit();


    }

    private void searchUser(String toString) {
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("Username")
                .startAt(toString)
                .endAt(toString + "\uf8ff");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void init() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        drawerLayout = findViewById(R.id.drawer_layout);
        nav_left = drawerLayout.findViewById(R.id.nav_view_left);
        nav_right = drawerLayout.findViewById(R.id.nav_view_right);

        username = nav_left.getHeaderView(0).findViewById(R.id.username);
        profile = nav_left.getHeaderView(0).findViewById(R.id.profile);
        recyclerView = nav_right.getHeaderView(0).findViewById(R.id.chat_item);
        search = nav_right.getHeaderView(0).findViewById(R.id.search_text);

        //search bar
        menu = findViewById(R.id.profile_out);
        chat = findViewById(R.id.chat_out);
        searchBox = findViewById(R.id.search_content);

        searchSetting = findViewById(R.id.search_setting_radio_group);
        searchSetting.check(R.id.search_title_radio_button);
        searchButton = findViewById(R.id.search_button);


        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        ViewPager viewPager = findViewById(R.id.content_frame);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new MainFragment(), "Map");
//        viewPagerAdapter.addFragment(new PostFragment(), "MyBook");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }


    private void readChat() {
        mUsers = new ArrayList<>();
        final Set<User> helperSet = new HashSet<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                helperSet.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (String id : usersList) {
                        if (user.getId().equals(id)) {
                            helperSet.add(user);
                        }
                    }
                }
                for (User user : helperSet) {
                    mUsers.add(user);
                }

                userAdapter = new UserAdapter(getApplicationContext(), mUsers, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //storage method openImage
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = ControlPannel.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(ControlPannel.this);
        pd.setMessage("Uploading");
        pd.show();


        if (imageUri != null) {
            final StorageReference fileRefer = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            storageTask = fileRefer.putFile(imageUri);
            storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRefer.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageURL", mUri);
                        reference.updateChildren(hashMap);

                        pd.dismiss();
                    } else {
                        Toast.makeText(ControlPannel.this, "Failed", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ControlPannel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(ControlPannel.this, "No Image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (storageTask != null && storageTask.isInProgress()) {
                Toast.makeText(ControlPannel.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


    // status control
    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}
