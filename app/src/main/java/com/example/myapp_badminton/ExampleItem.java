package com.example.myapp_badminton;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ExampleItem implements Parcelable {
    private Bitmap imageResource;
    private String text1;
    private String text2;
    /*databaseConnectionAdapter datahelper;
    SQLiteDatabase db;*/



    public ExampleItem(Bitmap imageResource, String text1, String text2) {
        try {
            this.imageResource = imageResource;
            this.text1 = text1;
            this.text2 = text2;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ExampleItem(Parcel in) {
        try {
            imageResource = in.readParcelable(Bitmap.class.getClassLoader());
            text1 = in.readString();
            text2 = in.readString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final Creator<ExampleItem> CREATOR = new Creator<ExampleItem>() {
        @Override
        public ExampleItem createFromParcel(Parcel in) {
            return new ExampleItem(in);
        }

        @Override
        public ExampleItem[] newArray(int size) {
            return new ExampleItem[size];
        }
    };


    public Bitmap getImageResource() {
        try {
            return imageResource;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getText1() {
        return text1;
    }
    public String getText2() {
        return text2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeParcelable(imageResource, flags);
            dest.writeString(text1);
            dest.writeString(text2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

