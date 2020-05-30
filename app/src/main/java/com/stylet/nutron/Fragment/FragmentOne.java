package com.stylet.nutron.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.stylet.nutron.Adapter.UserAdapter;
import com.stylet.nutron.Model.Patient;
import com.stylet.nutron.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentOne extends Fragment {


    public static FragmentOne newInstance() {
        return new FragmentOne();
    }

    UserAdapter userAdapter;
    List<Patient> users_list;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        users_list = new ArrayList<>();
        
        readUsers();

        return view;
    }

    private void readUsers() {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        //firebaseFirestore.collection("Users/" + )
    }


}
