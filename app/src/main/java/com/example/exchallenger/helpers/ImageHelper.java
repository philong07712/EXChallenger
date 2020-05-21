package com.example.exchallenger.Helpers;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;

public class ImageHelper {

    FirebaseStorage storage;

    public ImageHelper() {
        this.storage = FirebaseStorage.getInstance();
    }

    public interface uploadImageListener
    {
        void onSuccess();
        void onCancel();
    }

}
