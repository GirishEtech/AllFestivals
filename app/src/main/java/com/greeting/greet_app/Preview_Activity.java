package com.greeting.greet_app;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greeting.greet_app.Adapters.SlidersAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Preview_Activity extends AppCompatActivity implements View.OnTouchListener {
    int RESULT_LOAD_IMG = 100;
    private static final String TAG = "Touch";
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    Activity activity;
    String Type = "";
    String Link = "";
    TextView tv_title;
    ImageView img_preview;
    CardView carview;
    Bitmap bitmap;
    byte[] byteimg = null;
    File gif_file = null;
    boolean isdownload;
    DatabaseReference UserSavedref;
    ArrayList<String> Saved_list = new ArrayList<>();
    TextView download;
    LinearLayout add_frame_image;
    ImageView main_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        activity = this;
        UserSavedref = FirebaseDatabase.getInstance().getReference(Utils.UserSavedItems);

        Type = getIntent().getStringExtra("type");
        Link = getIntent().getStringExtra("Link");
        //   Toast.makeText(activity, ""+Type, Toast.LENGTH_SHORT).show();
        isdownload = getIntent().getBooleanExtra("isdownload", false);
        main_img = findViewById(R.id.main_img);
        add_frame_image = findViewById(R.id.add_frame_image);
        carview = findViewById(R.id.carview);
        img_preview = findViewById(R.id.img_preview);
        download = findViewById(R.id.download);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(Type + " Preview");
        viewPager2 = findViewById(R.id.viewPagerImageSlider);

        ArrayList<String> previewList = Stash.getArrayList("preview_list", String.class);

        List<String> sliderItems = new ArrayList<>();
        for (int i = 0; i < previewList.size(); i++) {
            sliderItems.add(previewList.get(i).toString());
        }


        if (Type.equals(Utils.Frames)){
            //main_img.setOnTouchListener(this);
        }
        Log.d("data", "Slider Items" + sliderItems.toString());
        viewPager2.setAdapter(new SlidersAdapter(sliderItems, viewPager2, Preview_Activity.this));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        int position = Stash.getInt("position");
        viewPager2.setCurrentItem(position);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (previewList.size() <= position) {
                    viewPager2.setClipToPadding(true);
                    viewPager2.setClipChildren(true);
                    Log.e(TAG, "onPageSelected: Pos ->" + position);
                } else {
                    Log.i(TAG, "onPageSelected: Array Size ->" + previewList.size());
                    Link = previewList.get(position);
                }
            }

        });
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        ArrayList<ResturantModel> resturantModelArrayList = Stash.getArrayList(Config.favourite, ResturantModel.class);
//        resturantModelArrayList.add(current_resturantModel);
//        Stash.put(Config.favourite, resturantModelArrayList);
            }
        });
        if (Type.equals(Utils.Frames)) {
            add_frame_image.setVisibility(View.VISIBLE);
        } else {
            add_frame_image.setVisibility(View.GONE);
        }
        add_frame_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

            }
        });
        if (Type.equals(Utils.Gifs)) {
            Load_Gif();
            Glide.with(activity).asGif().load(Link).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_preview);
        } else {
            Glide.with(activity).asBitmap().load(Link).diskCacheStrategy(DiskCacheStrategy.ALL).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    img_preview.setImageBitmap(resource);
                    bitmap = resource;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byteimg = byteArrayOutputStream.toByteArray();
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }
            });
        }
        Get_Saved_Items();
        findViewById(R.id.ic_nav_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.llDownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Saved_list.contains(Link)) {
                    Toast.makeText(activity, "Already Saved", Toast.LENGTH_SHORT).show();
                    return;
                }
                String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                String Key = UserSavedref.push().getKey();
                UserSavedref.child(android_id).child(Type).child(Key).setValue(Link);
                Saved_list.add(Link);
                if (!checkPermission()) {
                    getpermission();
                } else {
                    try {
                        if (!Type.equals(Utils.Gifs)) {
                            Glide.with(Preview_Activity.this).asBitmap().load(Link).into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onLoadStarted(@Nullable Drawable placeholder) {

                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {

                                }

                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    bitmap = resource;
                                    download_img(bitmap);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }

                            });
                        } else {
                            Load_Gif(GetFileName());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onClick: ERROR", e);
                    }
                }
            }
        });
        findViewById(R.id.llShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Type.equals(Utils.Gifs)) {
                    share_img();
                } else {
                    share_gif();
                }

            }
        });
        /*if (isdownload){
            findViewById(R.id.llDownload).setVisibility(View.GONE);
            findViewById(R.id.llShare).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!Type.equals(Utils.Gifs)){
                        share_downloaded_img(bitmap);
                    }else {
                        share_download_gif();
                    }

                }
            });
        }else {

        }*/
    }

    private void loadShareFile(){
        Glide.with(Preview_Activity.this).asBitmap().load(Link).into(new CustomTarget<Bitmap>() {
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                bitmap = resource;
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }

        });
    }
    private void Get_Saved_Items() {
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        UserSavedref.child(android_id).child(Type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.e("Key", dataSnapshot.getKey());
                        Saved_list.add(dataSnapshot.getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Load_Gif() {
        Glide.with(activity).asFile()
                .load(Link)
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(new Target<File>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        //  storeImage(resource);
                        //  img_preview.setImageURI(Uri.fromFile(resource));
                        gif_file = resource;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }
                });
    }

    private void Load_Gif(File File, Boolean loaded) {
        Glide.with(activity).asFile()
                .load(Link)
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(new Target<File>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        gif_file = resource;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }
                });
    }

    private void Load_Gif(File File) {
        Glide.with(activity).asFile()
                .load(Link)
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(new Target<File>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        //  storeImage(resource);
                        //  img_preview.setImageURI(Uri.fromFile(resource));
                        gif_file = resource;
                        storeGif(gif_file);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }
                });
    }

    private void storeGif(File image) {
        File file = GetFileName();
        if (file == null) {
            return;
        }
        try {
            FileOutputStream output = new FileOutputStream(file);
            FileInputStream input = new FileInputStream(image);

            FileChannel inputChannel = input.getChannel();
            FileChannel outputChannel = output.getChannel();

            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            output.close();
            input.close();
            Snackbar.make(findViewById(R.id.rv_main), (CharSequence) "Downloaded At " + file.getPath(), Snackbar.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File GetFileName() {
        String Name = "";

        if (Type.equals(Utils.Gifs)) {
            Name = Type + "_" + System.currentTimeMillis() + ".gif";

        } else {
            Name = Type + "_" + System.currentTimeMillis() + ".png";
        }
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + getString(R.string.app_name) + "/" + Type);
        if (!root.exists()) {
            root.mkdirs();
        }
        File file = new File(root, Name);
        return file;
    }

    private void share_downloaded_img(Bitmap imgbitmap) {
        Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                "com.greeting.greet_app.fileprovider", new File(Link));
        Intent shareintent = new Intent(Intent.ACTION_SEND);
        shareintent.setType("image/*");
        shareintent.putExtra(Intent.EXTRA_STREAM, uri);
        shareintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareintent, "share"));
        Snackbar.make(findViewById(R.id.rv_main), "Shared", Snackbar.LENGTH_LONG).show();
    }

    private void share_img() {
        loadShareFile();
        new Handler(getMainLooper()).postDelayed(() -> {
            runOnUiThread(()->{
                File imgfolder = new File(getCacheDir(), "images");
                Uri uri = null;
                try {
                    imgfolder.mkdir();
                    File file = new File(imgfolder, "shared_img.png");
                    FileOutputStream stream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    stream.flush();
                    stream.close();
                    uri = FileProvider.getUriForFile(getApplicationContext(),
                            "com.greeting.greet_app.fileprovider", file);

                    Intent shareintent = new Intent(Intent.ACTION_SEND);
                    shareintent.setType("image/*");
                    shareintent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareintent, "share"));
                    Snackbar.make(findViewById(R.id.rv_main), "Shared", Snackbar.LENGTH_LONG).show();

                } catch (IOException e) {

                }

            });
        },1500);

    }

    private void share_gif() {
        Load_Gif(GetFileName(), true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                File imgfolder = new File(getCacheDir(), "images");
                if (imgfolder == null) {
                    return;
                }
                runOnUiThread(() -> {
                    try {
                        imgfolder.mkdir();
                        File file = new File(imgfolder, "gifshared_img.gif");
                        FileOutputStream output = new FileOutputStream(file);
                        FileInputStream input = new FileInputStream(gif_file);

                        FileChannel inputChannel = input.getChannel();
                        FileChannel outputChannel = output.getChannel();

                        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                        output.close();
                        input.close();
                        Uri uri = null;
                        uri = FileProvider.getUriForFile(getApplicationContext(),
                                "com.greeting.greet_app.fileprovider", file);
                        Intent shareintent = new Intent(Intent.ACTION_SEND);
                        shareintent.setType("image/*");
                        shareintent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareintent, "share"));
                        Snackbar.make(findViewById(R.id.rv_main), "Shared", Snackbar.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

            }
        }, 1500);

    }

    private void share_download_gif() {
        try {
            Uri uri = null;
            uri = FileProvider.getUriForFile(getApplicationContext(),
                    "com.greeting.greet_app.fileprovider", new File(Link));
            Intent shareintent = new Intent(Intent.ACTION_SEND);
            shareintent.setType("image/*");
            shareintent.putExtra(Intent.EXTRA_STREAM, uri);
            shareintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareintent, "share"));
            Snackbar.make(findViewById(R.id.rv_main), "Shared", Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void download_img(Bitmap imgbitmap) {
        File file = GetFileName();
        try {
            FileOutputStream stream = new FileOutputStream(file);
            imgbitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
            Snackbar.make(findViewById(R.id.rv_main), (CharSequence) "Downloaded At " + file.getPath(), Snackbar.LENGTH_SHORT).show();

        } catch (IOException e) {
            Snackbar.make(findViewById(R.id.rv_main), (CharSequence) "Error In Download", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void share() {
        if (byteimg == null) {
            Toast.makeText(activity, "Image Not Found", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(getCacheDir().toString(), "img.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteimg);
            fos.close();
            //  Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), this.bitmap, Type+"/img", (String) null));
            Uri uri1 = Uri.fromFile(file);
            Intent shareintent = new Intent("android.intent.action.SEND");
            shareintent.setType("image/*");
            shareintent.putExtra("android.intent.extra.STREAM", uri1);
            startActivity(Intent.createChooser(shareintent, "Select App"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkPermission() {
        String exread = Manifest.permission.READ_EXTERNAL_STORAGE;
        String exwrite = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, exread) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                exwrite) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void getpermission() {
        String exread = Manifest.permission.READ_EXTERNAL_STORAGE;
        String exwrite = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{exread, exwrite}, 200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "PERMISSION_GRANTED",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                main_img.setImageBitmap(selectedImage);
               viewPager2.setOnTouchListener((view, motionEvent) -> {
                   view.setEnabled(false);
                   return true;
               });
                img_preview.setEnabled(false);
                main_img.setOnTouchListener(Preview_Activity.this);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Preview_Activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(Preview_Activity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

     @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;
        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); /*
                     * create the transformation in the matrix
                     * of points
                     */
                    // Load the image you want to overlay

                    Bitmap overLay = Bitmap.createBitmap(bitmap);
                } else if (mode == ZOOM) {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f) {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist;
                        /*
                         * setting the scaling of the matrix...if scale > 1 means
                         * zoom in...if scale < 1 means zoom out
                         */
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }


    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount()) sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Event", sb.toString());
    }

    public Bitmap uriToBitmap(Uri uri) throws IOException {
        ContentResolver resolver = getContentResolver();
        InputStream inputStream = resolver.openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        if (inputStream != null) {
            inputStream.close();
        }
        return bitmap;
    }

    private File createTempFile(Context context, String fileName) throws IOException {
        File cacheDir = context.getCacheDir();
        File tempFile = new File(cacheDir, fileName);
        tempFile.createNewFile();
        return tempFile;
    }

}