package com.laioffer.usedbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.laioffer.usedbook.Adapaters.UserAdapter;
import com.laioffer.usedbook.Entity.ChatList;
import com.laioffer.usedbook.Entity.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ControlPannel extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    TextView username;
    ImageView profile;
    NavigationView nav_left, nav_right;

    RecyclerView recyclerView;
    private List<User> mUsers;
    private UserAdapter userAdapter;

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    private List<String> usersList;





    //storage
    StorageReference storageReference;
    private static final int IMAGE_REQUEST =  1;
    private Uri imageUri;
    private StorageTask storageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_pannel);


        init();

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
            };
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

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

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

}



