package com.mayank.doodleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DoodleImagesActivity extends AppCompatActivity {


    RecyclerView doodleRecycler;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<ImagesModal,ProductViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodle_images);

        doodleRecycler = findViewById(R.id.doodleRecycler);
        fillTheRecycler();
    }




    private void fillTheRecycler() {

        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("images");

        FirestoreRecyclerOptions<ImagesModal> options = new FirestoreRecyclerOptions.Builder<ImagesModal>()
                .setQuery(query, ImagesModal.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ImagesModal, ProductViewHolder>(options) {

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(DoodleImagesActivity.this).inflate(R.layout.row_images_dodle, parent,false);

                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ProductViewHolder holder, int i, ImagesModal model) {

//                Log.d("position", "onBindViewHolder: "+holder.getAdapterPosition());

               holder.txt_email.setText(model.getEmail());

                Glide.with(DoodleImagesActivity.this).load(model.getImage()).into(holder.img_doodle);

            }
        };

        doodleRecycler.setAdapter(adapter);
        doodleRecycler.setLayoutManager(new LinearLayoutManager(DoodleImagesActivity.this));

    }

    private class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView img_doodle;
        TextView txt_email;
        ProductViewHolder(View view) {
            super(view);

            img_doodle = view.findViewById(R.id.img_doodle);
            txt_email = view.findViewById(R.id.txt_email);


        }


    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }



}