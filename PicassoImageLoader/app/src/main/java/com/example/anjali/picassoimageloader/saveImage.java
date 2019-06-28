package com.example.anjali.picassoimageloader;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.lang.ref.WeakReference;


public class saveImage implements Target {
    private Context context;
    private WeakReference<AlertDialog> alertDialogWeakReference;
    private WeakReference<ContentResolver> contentResolverWeakReference;
    private String name;
    private String desc;
    public saveImage(Context context,AlertDialog alertDialog,ContentResolver contentResolver,String name,String desc){
        this.context=context;
        this.alertDialogWeakReference=new WeakReference<AlertDialog>(alertDialog);
        this.contentResolverWeakReference=new WeakReference<ContentResolver>(contentResolver);
        this.name=name;
        this.desc=desc;
    }
    @Override
   /* public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        ContentResolver r=contentResolverWeakReference.get();
        AlertDialog dialog=alertDialogWeakReference.get();
        if(r!=null)
            MediaStore.Images.Media.insertImage(r,bitmap,name,desc);
        dialog.dismiss();
        //open gallery after download
        Intent intent=new Intent();
        intent.setType("image/");
        intent.setAction(intent.ACTION_GET_CONTENT);
        context.startActivity(Intent.createChooser(intent,"VIEW PICTURE"));
    }
*/
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        ContentResolver r = contentResolverWeakReference.get();
        AlertDialog alertDialog = alertDialogWeakReference.get();
        if (r != null)
            MediaStore.Images.Media.insertImage(r, bitmap, name, desc);
        alertDialog.dismiss();
        Toast.makeText(context, "Download succeed ", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}