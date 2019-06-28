package com.example.anjali.picassoimageloader;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    TextView nameTxt;
    Button btn;
    Uri file;
    File in;
    int a;
    long i=1;
    String name,fname="";
    ListView list_view;
    ArrayAdapter<File> adapter;
    ArrayList<File> list = new ArrayList<File>();
    ArrayList<File> list_items = new ArrayList<File>();
    int cnt = 0;
    private static final int PERMISSION_REQUEST_CODE = 1000;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img = findViewById(R.id.img);
        nameTxt = findViewById(R.id.nameTxt);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
             //  File fi = new File(downloadsFolder,"/app/");
              // fi.mkdir();
                if (downloadsFolder.isDirectory()) {
                    String[] children = downloadsFolder.list();
                    for (int x = 0; x < children.length; x++) {
                        fname = children[x];
                        File fil = new File(fname);
                        ImageFileFilter fx = new ImageFileFilter(fil);
                        if(fx.accept(fil))
                        {
                            fname = children[x];
                            break;
                        }
                    }
                }
                file = Uri.fromFile(new File(downloadsFolder, fname));
                in = new File(file.getPath());
                name = in.getName();

                if (file.toString() != null && file.toString().length() > 0) {
                    Picasso.with(MainActivity.this).load(file).placeholder(R.drawable.placeholder).into(img);
                    nameTxt.setText(file.toString());
                } else {
                    Toast.makeText(MainActivity.this, "Empty URI", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long byteAvailable = stat.getBlockCountLong() * stat.getBlockCountLong();
            final long memAvailable = byteAvailable / 1048576;

            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "you should grant permission", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSION_REQUEST_CODE);
                    return;
                } else {
                    openDialog(memAvailable);
                    if (a == 1) {
                        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
                        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
                        File path;
                        if(isSDSupportedDevice && isSDPresent)
                        {
                            // SD card is present
                            path=Environment.getExternalStorageDirectory();
                        }
                        else {

                            //SD card is not present
                            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        }
                        // create new folder
                        File dir = new File(path + "/Model" + i + "/");
                        dir.mkdir();
                        File file1 = new File(dir, "a.png");
                        File file2 = new File(dir, "b.jpg");
                        File file3 = new File(dir, "c.jpeg");
                        try {
                            copyFile(in, file1);
                            copyFile(in, file2);
                            copyFile(in, file3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    else
                    {
                        delete_image1(i);
                        delete_image2(fname);
                    }
                    i++;
                }
            }
        });
    }

    public void openDialog(long mem) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("Available memory is: "+mem+", DO YOU WANT TO SAVE?");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        TextView msg = new TextView(this);
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                a=0;
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                a=1;
            }
        });


        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) cancelBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.RED);
        cancelBT.setLayoutParams(negBtnLP);
    }


    public  void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public void delete_image1(long i) {

        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
        File path;
        if (isSDSupportedDevice && isSDPresent) {
            // SD card is present ,so delete from sd card
            path = Environment.getExternalStorageDirectory();
        } else {
            // sd card not present so delete from directory pictures
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }
        // create new folder
        File dir = new File(path + "/Model" + i + "/");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int x = 0; x < children.length; x++) {
                new File(dir, children[x]).delete();
            }
        }
        dir.delete();
    }
    public void delete_image2(String fname)
    {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File ff = new File(path,fname);
        if (ff.exists()) {
            if (ff.delete()) {
                System.out.println("file Deleted ");
            } else {
                System.out.println("file not Deleted ");
            }
        }
    }

    public void function1(View view) {
        // RelativeLayout r=(RelativeLayout) this.<View>findViewById(R.id.img);
        //r.setBackgroundResource(0);
        img.setImageResource(0);
        list_view = (ListView) findViewById(R.id.list_view);
        final File picture_folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if(picture_folder.isDirectory())
        {
            File[] files = picture_folder.listFiles();
            for(int x = 0;x< files.length;x++)
            {
                list.add(files[x]);
            }
        }
        adapter = new ArrayAdapter<File>(this,android.R.layout.simple_list_item_1,list);
        list_view.setAdapter(adapter);
        list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list_view.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater  inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.my_context_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.delete_id:
                        for(File m : list_items)
                        {
                            adapter.remove(m);
//                       if (m.isDirectory())
//                       {
//                           String[] children = m.list();
//                           for (int x = 0;x < children.length; x++)
//                           {
//                               new File(m, children[x]).delete();
//                           }
//                           m.delete();
//                       }
                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            if(path.isDirectory() )
                            {
                                File child[]=path.listFiles();
                                for(int x=0;x<child.length;x++)
                                {
                                    int cmp = child[x].compareTo(m);
                                    if(cmp==1)
                                        child[x].delete();
                                }
                            }
                            m.delete();
                        }
                        Toast.makeText(getBaseContext(),cnt+"item removed",Toast.LENGTH_SHORT).show();
                        cnt = 0;
                        mode.finish();
                        return true;
                    default:
                        break; }
                return false;
            }
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                cnt+=1;
                mode.setTitle(cnt+"item selected");
                list_items.add(list.get(position));
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    public class ImageFileFilter implements FileFilter
    {
        File file;
        private final String[] okFileExtensions =  new String[] {"jpg", "png","jpeg"};

        public ImageFileFilter(File newfile)
        {
            this.file=newfile;
        }

        public boolean accept(File file)
        {
            for (String extension : okFileExtensions) {
                if (file.getName().toLowerCase().endsWith(extension)) {
                    return true;
                }
            }
            return false;
        }
    }

}