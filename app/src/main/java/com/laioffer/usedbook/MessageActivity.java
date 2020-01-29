package com.laioffer.usedbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laioffer.usedbook.Adapaters.MessageAdapter;
import com.laioffer.usedbook.Entity.ChatList;
import com.laioffer.usedbook.Entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<ChatList> mchat;
    RecyclerView recyclerView;


    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageActivity.this, ControlPannel.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


        init();

        final String userid = intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(userid).getValue(User.class);
                User user_current = dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);
                username.setText(user.getUsername());
//                if (user.getImageURL().equals("default")) {
//                    profile.setImageResource(R.mipmap.ic_launcher);
//                }
//                else {
//                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile);
//                }

                readMessage(firebaseUser.getUid(), userid, user.getImageURL(), user_current.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //send button click
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(), userid, msg);
                }
                else {
                    Toast.makeText(MessageActivity.this, "You can't send empty text!", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
    }





    public void init() {
        intent = getIntent();
//        profile = findViewById(R.id.profile);
        username = findViewById(R.id.username);
        text_send = findViewById(R.id.text_send);
        btn_send = findViewById(R.id.btn_send);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }


    private void sendMessage (String sender, String receiver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);

//        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chats");
    }



    private void readMessage (final String myid, final String userid, final String imageurl, final String currentImageurl) {
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    if (chatList.getReceiver().equals(myid) && chatList.getSender().equals(userid) ||
                            chatList.getReceiver().equals(userid) && chatList.getSender().equals(myid)) {
                        mchat.add(chatList);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl, currentImageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
