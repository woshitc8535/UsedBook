package com.laioffer.usedbook.Fragments;

import androidx.fragment.app.Fragment;


public class PostFragment extends Fragment {
//    private static final int REQUEST_CAPTURE_IMAGE = 100;
//    private static final int REQUEST_EXTERNAL_STORAGE = 1;
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };
//
//    DatabaseReference reference;
//
//    private TextView currentTime;
//    private EditText title, price, description;
//    private ImageView book_img;
//
//
//    private Button post;
//    private RecyclerView recyclerView;
//    private BookAdapter bookAdapter;
//    private List<Book> mBook;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_post, container, false);
//
//
//        Date current = Calendar.getInstance().getTime();
//        String str = current.toString();
//        currentTime = view.findViewById(R.id.time_bar);
//        currentTime.setText(str);
//
//        //init
//        book_img = view.findViewById(R.id.book_img);
//        title = view.findViewById(R.id.book_title);
//        price = view.findViewById(R.id.book_price);
//        description = view.findViewById(R.id.book_description);
//        post = view.findViewById(R.id.btn_post);
//
//
//        recyclerView = view.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setNestedScrollingEnabled(false);
//        mBook = new ArrayList<>();
//
//        readBook();
//
//        //upload post
//        post.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String text_bookName = title.getText().toString();
//                String text_bookPrice = price.getText().toString();
//                String text_decription = description.getText().toString();
//
//
//                if (TextUtils.isEmpty(text_bookName) || TextUtils.isEmpty(text_bookPrice) || TextUtils.isEmpty(text_decription)) {
//                    Toast.makeText(getContext(), "All Fields are needed1", Toast.LENGTH_SHORT).show();
//                } else {
//                    uploadBook(text_bookName, text_bookPrice, text_decription);
//                    Toast.makeText(getContext(), "Successful upload", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//        return view;
//    }
//
//    private void readBook() {
//        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mBook.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Book book = snapshot.getValue(Book.class);
//                    if (book.getSellerId().equals(firebaseUser.getUid())) {
//                        mBook.add(book);
//                    }
//                }
//                bookAdapter = new BookAdapter(getContext(), mBook);
//                recyclerView.setAdapter(bookAdapter);
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void uploadBook(String text_bookName, String text_bookPrice, String text_decription) {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        String userid = firebaseUser.getUid();
//        reference = FirebaseDatabase.getInstance().getReference();
//
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("title", text_bookName);
//        hashMap.put("sellerId", userid);
//        hashMap.put("price", text_bookPrice);
//        hashMap.put("description", text_decription);
//        hashMap.put("imgUrl", "https://www.google.com/imgres?imgurl=https%3A%2F%2Fstorage.googleapis.com%2Fgd-wagtail-prod-assets%2Fimages%2Fevolving_google_identity_2x.max-4000x2000.jpegquality-90.jpg&imgrefurl=https%3A%2F%2Fdesign.google%2Flibrary%2Fevolving-google-identity%2F&tbnid=qZcGMELPKtbGvM&vet=12ahUKEwiOhd2v78jnAhVpBDQIHVd1BxMQMygAegUIARCLAg..i&docid=4aQ0r7NfC0dsEM&w=2432&h=1216&q=google&hl=en&authuser=0&ved=2ahUKEwiOhd2v78jnAhVpBDQIHVd1BxMQMygAegUIARCLAg");
//
//        reference.child("Books").push().setValue(hashMap);
//    }
//

}
