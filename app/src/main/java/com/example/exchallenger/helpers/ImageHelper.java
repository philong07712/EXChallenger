package com.example.exchallenger.helpers;

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
