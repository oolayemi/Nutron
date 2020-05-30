package com.stylet.nutron.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class PatientId {

    @Exclude
    private String PatientId;

    public <T extends PatientId> T withId(@NonNull final String id) {
        this.PatientId = id;
        return (T) this;
    }

}

