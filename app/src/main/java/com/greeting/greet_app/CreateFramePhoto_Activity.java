package com.greeting.greet_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.greeting.greet_app.Adapters.BG_Adapters;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateFramePhoto_Activity extends AppCompatActivity implements View.OnTouchListener, ColorListner,BG_Adapters.OnClickListener {

    private ViewDialog viewDialog;
    private static final String TAG = "Touch";
    public static final int PERM_RQST_CODE = 110;
    private com.xiaopo.flying.sticker.StickerView stickerView;
    private TextSticker sticker;
    private com.greeting.greet_app.sticker.TextSticker TextSticker;

    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;
    int RESULT_LOAD_IMG = 100;

    FrameLayout temp_layout;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    TextView tv_done;
    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    ImageView main_img, ic_backBtn,tempImage;
    Activity activity;
    Toolbar toolbar;
    String UserMobileId = "";

    BG_Adapters quotation_adapters_bg;


    ArrayList<String> list_bg = new ArrayList<>();

    LinearLayout gallery_img, camera_img, bg_imageLayout;
    View main_view;
    LinearLayout bg_image;
    RecyclerView simple_image;
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        tempImage.setImageBitmap(photo);
                        tempImage.setOnTouchListener(CreateFramePhoto_Activity.this);
                    }
                }
            });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_create_framephoto);
        getWindow().setStatusBarColor(getResources().getColor(R.color.teal_200));
        activity = this;
        UserMobileId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        toolbar = findViewById(R.id.toolbar);
        bg_image = findViewById(R.id.bg_image);
        simple_image = findViewById(R.id.simple_image);
        bg_imageLayout = findViewById(R.id.bg_img);
        gallery_img = findViewById(R.id.gallery_img);
        main_img = findViewById(R.id.main_img);
        ic_backBtn = findViewById(R.id.ic_nav_menu);
        tempImage = findViewById(R.id.temp_image);
        tv_done = findViewById(R.id.tv_done);
        setSupportActionBar(toolbar);
        viewDialog = new ViewDialog(this);
        camera_img = findViewById(R.id.camera_img);
        main_view = findViewById(R.id.main_view);
        quotation_adapters_bg = new BG_Adapters(getApplicationContext(), list_bg, Utils.Background);
        simple_image.setAdapter(quotation_adapters_bg);
        Get_Storage();
        quotation_adapters_bg.setOnClickListener(this);
        String model = getIntent().getStringExtra("Link");
        Glide.with(CreateFramePhoto_Activity.this).load(model).into(main_img);
        ic_backBtn.setOnClickListener(view -> {
            onBackPressed();
            Log.i(TAG, "onCreate: BackPress Clicked");
        });

        camera_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                permission();
            }
        });
        gallery_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
        bg_imageLayout.setOnClickListener(view -> {
            bg_image.setVisibility(View.VISIBLE);
        });
        findViewById(R.id.tv_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testLock();
                Toast.makeText(activity, "Saved Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        tv_done.setOnClickListener(view -> {
            stickerView.setLocked(true);
            Bitmap bitmap = stickerView.createBitmap();
            download_img(bitmap);
        });
        stickerView = (com.xiaopo.flying.sticker.StickerView) findViewById(R.id.sticker_view);
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());


        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon));

        //default icon layout
        //stickerView.configDefaultIcons();

        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);

        sticker = new TextSticker(this);

        sticker.setDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.sticker_transparent_background));
        sticker.setText("Hello, world!");
        sticker.setTextColor(Color.BLACK);
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        sticker.resizeText();


        stickerView.setOnStickerOperationListener(new com.xiaopo.flying.sticker.StickerView.OnStickerOperationListener() {

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                //stickerView.removeAllSticker();
                if (sticker instanceof TextSticker) {
//                    ((TextSticker) sticker).setTextColor(Color.RED);
                    stickerView.replace(sticker);
                    stickerView.invalidate();
                }
                Log.d(TAG, "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
            }


            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_RQST_CODE);
        } else {
//            loadSticker();
        }

    }




    private void permission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 2);
            permission();
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                someActivityResultLauncher.launch(takePictureIntent);
            }
            Toast.makeText(this, "Permission is Granted", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.del, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public boolean checkPermission() {
        String exread = Manifest.permission.READ_EXTERNAL_STORAGE;
        String exwrite = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, exread) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, exwrite) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void getpermission() {
        String exread = Manifest.permission.READ_EXTERNAL_STORAGE;
        String exwrite = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{exread, exwrite}, 201);
        }
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_RQST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            loadSticker();
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
                tempImage.setImageBitmap(selectedImage);
                tempImage.setOnTouchListener(this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(CreateFramePhoto_Activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {

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



    public void testReplace(View view) {
        if (stickerView.replace(sticker)) {
            Toast.makeText(CreateFramePhoto_Activity.this, "Replace Sticker successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CreateFramePhoto_Activity.this, "Replace Sticker failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void testLock() {
        stickerView.setLocked(!stickerView.isLocked());
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        
    }

    public void testRemove(View view) {
        if (stickerView.removeCurrentSticker()) {
            Toast.makeText(CreateFramePhoto_Activity.this, "Remove current Sticker successfully!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(CreateFramePhoto_Activity.this, "Remove current Sticker failed!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void testRemoveAll(View view) {
        stickerView.removeAllStickers();
    }

    public void reset(View view) {
        stickerView.removeAllStickers();
//        loadSticker();
    }

    public void testAdd(String text) {
        if (text.length() > 1) {

            TextSticker = new com.greeting.greet_app.sticker.TextSticker(this);
            TextSticker.setText("" + text);
            TextSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
            TextSticker.resizeText();
            stickerView.addSticker(TextSticker);
        }
    }


    private void Get_Storage() {
        list_bg.clear();
        try {
            ArrayList<String> previewList = Stash.getArrayList("preview_list", String.class);
            for (int i = 0; i < previewList.size(); i++) {
                list_bg.add(previewList.get(i).toString());
            }
            quotation_adapters_bg.notifyDataSetChanged();
        } catch (Exception exception) {
            Log.e(TAG, "Get_Storage: Exception", exception);
            //viewDialog.hideDialog();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setDrawable(int colors[]) {
        Log.i(TAG, "setDrawable: Listner is Click");
        if (TextSticker != null) {
            TextSticker.setGradientColor(colors[0], colors[1]);
        }
        stickerView.invalidate();
    }

    public com.greeting.greet_app.sticker.TextSticker getSelected() {
        return (com.greeting.greet_app.sticker.TextSticker) stickerView.getCurrentSticker();
    }


    @Override
    public void onClick(int position, String model) {
        bg_image.setVisibility(View.VISIBLE);
        Glide.with(CreateFramePhoto_Activity.this).load(model).into(main_img);
    }
    private File GetFileName() {
        String Name = "";
            Name = "Frames" + "_" + System.currentTimeMillis() + ".png";
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + getString(R.string.app_name) + "/" + "Frames");
        if (!root.exists()) {
            root.mkdirs();
        }
        File file = new File(root, Name);
        return file;
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
            Toast.makeText(this, "Frame Saved SuccessFully \n "+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                runOnUiThread(() -> {
                    onBackPressed();
                });
            },700);

        } catch (IOException e) {
            Snackbar.make(findViewById(R.id.tempview), (CharSequence) "Error In Download", Snackbar.LENGTH_SHORT).show();
        }
    }
}