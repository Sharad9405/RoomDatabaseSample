package com.roomdatabase.custombinding;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.roomdatabase.R;


public class BindingAdapters {


    /** This for image source of integer type **/
    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView view, int resource){

        Context context = view.getContext();

        view.setImageResource(resource);
    }
}
