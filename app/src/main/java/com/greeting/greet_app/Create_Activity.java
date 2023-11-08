package com.greeting.greet_app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.greeting.greet_app.Adapters.BG_Adapters;
import com.greeting.greet_app.Adapters.DataAdapter;
import com.greeting.greet_app.Adapters.Gifs_Adapters;
import com.greeting.greet_app.Adapters.StickersAdapter;
import com.greeting.greet_app.Model.SimpleColor;
import com.madrapps.pikolo.RGBColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import ir.kotlin.kavehcolorpicker.KavehColorAlphaSlider;
import top.defaults.colorpicker.ColorPickerPopup;
import top.defaults.colorpicker.ColorWheelPalette;

public class Create_Activity extends AppCompatActivity implements View.OnTouchListener {

    private int originalColor = 1;

    private ViewDialog viewDialog;
    private static final String TAG = "Touch";
    public static final int PERM_RQST_CODE = 110;
    private com.xiaopo.flying.sticker.StickerView stickerView;
    private TextSticker sticker, TextSticker;

    private EditText tv_feedback_text;
    private KavehColorAlphaSlider kavehColorAlphaSlider;
    @SuppressWarnings("unused")
    StickersAdapter stickersAdapter;

    private RGBColorPicker gradiant_colorPicker;
    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;
    DataAdapter dataAdapter1;
    int RESULT_LOAD_IMG = 100;

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
    ImageView main_img;
    Activity activity;
    Toolbar toolbar;
    String UserMobileId = "";
    ColorWheelPalette add_color_btn, add_gradiant_color;
    LinearLayout bottom_nav_card, add_gradiant_layout, nav_add_color, nav_add_text, nav_add_filters, nav_add_sticker;
    LinearLayout nav_add_img, bottom_add_layout, bottom_color_layout, bottom_add_sticker_layout, bottom_add_filters_layout;
    RecyclerView Choose_Sticker_RecyclerView, Choose_Filters_RecyclerView;
    TextView tv_title;
    RelativeLayout rv_cancel_done;
    ImageView ic_cross, ic_done;
    Gifs_Adapters quotation_adapters;

    BG_Adapters quotation_adapters_bg;

    ArrayList<String> list = new ArrayList<>();

    ArrayList<String> list_bg = new ArrayList<>();

    LinearLayout gallery_img, camera_img, color, color_btn, grediant, grediant_btn;
    RecyclerView simple_Color, grediant_Color;
    TextView text;
    ImageView ivOld;

    ImageView ivBlur;
    ImageView ivBacksheet;
    ImageView ivReliefs;
    ImageView ivBW;
    ImageView ivChange;
    ImageView ivMosaic;
    ImageView ivDark;
    ImageView ivPunch;
    ImageView ivoriginal;
    View main_view;
    LinearLayout bg_image, bg_img;
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
                        main_img.setImageBitmap(photo);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_create);
        getWindow().setStatusBarColor(getResources().getColor(R.color.teal_200));
        activity = this;
        UserMobileId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        toolbar = findViewById(R.id.toolbar);
        bg_img = findViewById(R.id.bg_img);
        bg_image = findViewById(R.id.bg_image);
        simple_image = findViewById(R.id.simple_image);
        gallery_img = findViewById(R.id.gallery_img);
        simple_Color = findViewById(R.id.simple_Color);
        main_img = findViewById(R.id.main_img);
        color = findViewById(R.id.color);
        color_btn = findViewById(R.id.color_btn);
        grediant = findViewById(R.id.grediant);
        grediant_Color = findViewById(R.id.grediant_Color);
        grediant_btn = findViewById(R.id.gredient_btn);
        kavehColorAlphaSlider = findViewById(R.id.colorAlphaSlider);
        setSupportActionBar(toolbar);
        viewDialog = new ViewDialog(this);
        In_it_List();
        Choose_Sticker_RecyclerView = findViewById(R.id.Choose_Sticker_RecyclerView);
//        Choose_Filters_RecyclerView = findViewById(R.id.Choose_Filters_RecyclerView);
        bottom_add_sticker_layout = findViewById(R.id.bottom_add_sticker_layout);
        bottom_add_filters_layout = findViewById(R.id.bottom_add_filters_layout);
        nav_add_text = findViewById(R.id.nav_add_text);
        nav_add_filters = findViewById(R.id.nav_add_filters);
        add_color_btn = findViewById(R.id.add_color_layout);
        add_gradiant_color = findViewById(R.id.add_gradiant_color);
        add_gradiant_layout = findViewById(R.id.add_gradiant_layout);
        gradiant_colorPicker = findViewById(R.id.gradiant_colorPicker);
        nav_add_sticker = findViewById(R.id.nav_add_sticker);
        ic_cross = findViewById(R.id.ic_cross);
        ic_done = findViewById(R.id.ic_done);
        bottom_nav_card = findViewById(R.id.bottom_nav_card);
        nav_add_img = findViewById(R.id.nav_add_img);
        tv_title = findViewById(R.id.tv_title);
        bottom_add_layout = findViewById(R.id.bottom_add_layout);
        rv_cancel_done = findViewById(R.id.rv_cancel_done);
        bottom_color_layout = findViewById(R.id.bottom_color_layout);
        nav_add_color = findViewById(R.id.nav_add_color);
        camera_img = findViewById(R.id.camera_img);
        main_img.setOnTouchListener(this);
        ivoriginal = findViewById(R.id.ivoriginal);
        main_view = findViewById(R.id.main_view);
        ivOld = findViewById(R.id.ivOld);
        ivBlur = (ImageView) findViewById(R.id.ivBlur);
        ivBacksheet = (ImageView) findViewById(R.id.ivBacksheet);
        ivReliefs = (ImageView) findViewById(R.id.ivReliefs);
        ivBW = (ImageView) findViewById(R.id.ivBW);
        ivMosaic = (ImageView) findViewById(R.id.ivMosaic);
        ivDark = (ImageView) findViewById(R.id.ivDark);
        ivPunch = (ImageView) findViewById(R.id.ivPunch);
        simple_image = findViewById(R.id.simple_image);
        simple_image.setLayoutManager(new GridLayoutManager(Create_Activity.this, 3));
        quotation_adapters_bg = new BG_Adapters(getApplicationContext(), list_bg, Utils.Background);
        simple_image.setAdapter(quotation_adapters_bg);
        Get_Storage();
        bg_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_add_layout.setVisibility(View.GONE);
                bg_image.setVisibility(View.VISIBLE);
                quotation_adapters_bg.setOnClickListener(new BG_Adapters.OnClickListener() {
                    @Override
                    public void onClick(int position, String model) {
                        Glide.with(Create_Activity.this).load(model).into(main_img);
                    }
                });

            }
        });
        add_color_btn.setOnClickListener(this::showColorPickerDiologe);
        ivOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_view.setBackgroundColor(0x66000000);
            }
        });
        ivBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_view.setBackgroundColor(0x66F44336);
            }
        });
        ivBacksheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_view.setBackgroundColor(0x6600BCD4);
            }
        });
        ivReliefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_view.setBackgroundColor(0x664C0B0B);
            }
        });
        ivBW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_view.setBackgroundColor(0xA64CAF50);
            }
        });
        ivMosaic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_view.setBackgroundColor(0x66FFEB3B);
            }
        });
        ivDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_view.setBackgroundColor(0x66C39F93);
            }
        });
        ivPunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_view.setBackgroundColor(0x669C27B0);
            }
        });
        ivoriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_view.setBackgroundColor(0x00000000);
            }
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
        color_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_add_layout.setVisibility(View.GONE);
                color.setVisibility(View.VISIBLE);
            }
        });

        this.simple_Color.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.simple_Color.setHasFixedSize(true);
        String[] colorNames = getResources().getStringArray(R.array.colorNames);
        ArrayList<SimpleColor> arrayList = new ArrayList<>();
        for (int i = 0; i < colorNames.length; i++) {
            SimpleColor m = new SimpleColor();
            TypedArray ta = getResources().obtainTypedArray(R.array.colors);
            int colorToUse = ta.getResourceId(i, 0);
            m.setColorToUse(colorToUse);
            arrayList.add(m);
        }
        DataAdapter dataAdapter = new DataAdapter(this, arrayList);
        this.simple_Color.setAdapter(dataAdapter);

        add_gradiant_color.setOnClickListener(view -> {
            showGradiantDioloue();
        });
        grediant_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_add_layout.setVisibility(View.GONE);
                grediant.setVisibility(View.VISIBLE);
                grediant_Color.setLayoutManager(new LinearLayoutManager(Create_Activity.this, RecyclerView.HORIZONTAL, false));
                grediant_Color.setHasFixedSize(true);
                ArrayList<SimpleColor> arrayList = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    SimpleColor m = new SimpleColor();
                    TypedArray ta = getResources().obtainTypedArray(R.array.frag_home_ids);
                    int colorToUse = ta.getResourceId(i, 0);
                    m.setColorToUse(colorToUse);
                    arrayList.add(m);
                }
                dataAdapter1 = new DataAdapter(Create_Activity.this, arrayList);
                grediant_Color.setAdapter(dataAdapter1);
                dataAdapter1.setOnClickListener(new DataAdapter.OnClickListener() {
                    @Override
                    public void onClick(int position, SimpleColor model) {
                        main_img.setImageResource(arrayList.get(position).getColorToUse());
                    }
                });
            }
        });
        dataAdapter.setOnClickListener(new DataAdapter.OnClickListener() {
            @Override
            public void onClick(int position, SimpleColor model) {
                main_img.setImageResource(arrayList.get(position).getColorToUse());
            }
        });
        findViewById(R.id.tv_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testLock();
                Toast.makeText(activity, "Saved Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Set_Layout_Gone();
        nav_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_nav_card.setVisibility(View.GONE);
                bottom_color_layout.setVisibility(View.GONE);
                bottom_add_sticker_layout.setVisibility(View.GONE);
                bottom_add_filters_layout.setVisibility(View.GONE);
                tv_title.setText(getString(R.string.Add));
                bottom_add_layout.setVisibility(View.VISIBLE);
                rv_cancel_done.setVisibility(View.VISIBLE);
            }
        });
        nav_add_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_nav_card.setVisibility(View.GONE);
                bottom_add_layout.setVisibility(View.GONE);
                bottom_add_sticker_layout.setVisibility(View.GONE);
                bottom_add_filters_layout.setVisibility(View.GONE);
                tv_title.setText(getString(R.string.Color));
                bottom_color_layout.setVisibility(View.VISIBLE);
                rv_cancel_done.setVisibility(View.VISIBLE);
            }
        });
        nav_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_nav_card.setVisibility(View.GONE);
                tv_title.setText(getString(R.string.Text));
                bottom_color_layout.setVisibility(View.GONE);
                bottom_add_layout.setVisibility(View.GONE);
                rv_cancel_done.setVisibility(View.GONE);
                bottom_add_sticker_layout.setVisibility(View.GONE);
                bottom_add_filters_layout.setVisibility(View.GONE);
                Show_Add_Text_Dialog();
            }
        });
        stickersAdapter = new StickersAdapter(Create_Activity.this, list);
        Choose_Sticker_RecyclerView.setAdapter(stickersAdapter);
        nav_add_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_add_sticker_layout.setVisibility(View.VISIBLE);
                bottom_nav_card.setVisibility(View.GONE);
                tv_title.setText(getString(R.string.Stickers));
                bottom_color_layout.setVisibility(View.GONE);
                bottom_add_layout.setVisibility(View.GONE);
                rv_cancel_done.setVisibility(View.VISIBLE);
                bottom_add_filters_layout.setVisibility(View.GONE);
                Choose_Sticker_RecyclerView.setVisibility(View.VISIBLE);
            }
        });
        stickersAdapter.setOnClickListener(new StickersAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(activity, "as" + position, Toast.LENGTH_SHORT).show();
                loadSticker(list.get(position));
            }
        });

        nav_add_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_nav_card.setVisibility(View.GONE);
                tv_title.setText(getString(R.string.Filters));
                bottom_color_layout.setVisibility(View.GONE);
                bottom_add_layout.setVisibility(View.GONE);
                rv_cancel_done.setVisibility(View.VISIBLE);
                bottom_add_sticker_layout.setVisibility(View.GONE);
                bottom_add_filters_layout.setVisibility(View.VISIBLE);
//                quotation_adapters = new Gifs_Adapters(activity, list);
//                Choose_Filters_RecyclerView.setAdapter(quotation_adapters);
            }
        });
        ic_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set_Layout_Gone();
            }
        });
        ic_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set_Layout_Gone();
            }


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

    private void showGradiantDioloue() {
        Set_Layout_Gone();
        add_gradiant_layout.setVisibility(View.VISIBLE);
        gradiant_colorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {
                super.onColorSelected(color);
                TextSticker = (com.xiaopo.flying.sticker.TextSticker) stickerView.getCurrentSticker();
                TextSticker.setTextColor(color);
                stickerView.invalidate();
            }
        });
        findViewById(R.id.btnGColorChoose).setOnClickListener(view -> {
            add_gradiant_layout.setVisibility(View.GONE);
            TextSticker.setTextColor(originalColor);
            stickerView.invalidate();
        });
    }

    public  int adjustColorOpacity(int color, float opacity) {
        int alpha = Math.round(Color.alpha(color) * opacity);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb(alpha, red, green, blue);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showColorPickerDiologe(View view) {
        kavehColorAlphaSlider.setOnAlphaChangedListener(aFloat -> {
            main_view.setAlpha(aFloat);
            TextSticker = (com.xiaopo.flying.sticker.TextSticker) stickerView.getCurrentSticker();
            TextSticker.setTextColor(adjustColorOpacity(originalColor,aFloat));
            stickerView.invalidate();
            Log.i(TAG, "showColorPickerDiologe: VALUE OF FLOAT : -" + Math.abs(aFloat));
        });
        new ColorPickerPopup.Builder(this)
                .initialColor(Color.RED)
                .enableBrightness(true)
                .enableAlpha(true)
                .okTitle("Choose")
                .cancelTitle("Cencel")
                .showIndicator(true)
                .showValue(false)
                .build()
                .show(view, new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        if (TextSticker != null) {
                            TextSticker = (com.xiaopo.flying.sticker.TextSticker) stickerView.getCurrentSticker();
                            TextSticker.setTextColor(color);
                            originalColor = color;
                            stickerView.invalidate();
                            kavehColorAlphaSlider.setSelectedColor(color);
                        }
                    }
                });
        ;
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

    private void Set_Layout_Gone() {
        bottom_nav_card.setVisibility(View.VISIBLE);
        rv_cancel_done.setVisibility(View.GONE);
        bottom_add_layout.setVisibility(View.GONE);
        bottom_color_layout.setVisibility(View.GONE);
        bottom_add_sticker_layout.setVisibility(View.GONE);
        bottom_add_filters_layout.setVisibility(View.GONE);
        color.setVisibility(View.GONE);
        bg_image.setVisibility(View.GONE);
        grediant.setVisibility(View.GONE);
        tv_title.setText(getString(R.string.Create));
//        testLock();
    }

    private void Show_Add_Text_Dialog() {
        View v = activity.getLayoutInflater().inflate(R.layout.add_text_dialog, null);
        Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(v);
        v.findViewById(R.id.ic_cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Set_Layout_Gone();
            }
        });
        v.findViewById(R.id.ic_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                tv_feedback_text = v.findViewById(R.id.tv_feedback_text);
                testAdd(tv_feedback_text.getText().toString() + "");
                Log.i(TAG, "onClick: " + tv_feedback_text.getText().toString());
                Set_Layout_Gone();
            }
        });
        dialog.show();
        //   dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //  dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
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

    private void In_it_List() {
        viewDialog.showDialog();
        String Category = getIntent().getStringExtra(Utils.CAT_TYPE);
        String child = getIntent().getStringExtra(Utils.TAB_NAME);
        try {
            if (Category == null) {
                Log.i(TAG, "In_it_List: Family Wishes Pending");
            } else {
                Log.i(TAG, "In_it_List: TEST CAT - >" + Category);
                Log.i(TAG, "In_it_List: TEST NAME ->" + child);
                StorageReference ref = FirebaseStorage.getInstance().getReference(Category).child(child).child("Stickers");
                ref.listAll().addOnSuccessListener(listResult -> {
                            for (StorageReference item : listResult.getItems()) {
                                item.getDownloadUrl().addOnSuccessListener(uri -> {
                                            list.add(uri.toString());
                                        })
                                        .addOnFailureListener(runnable -> {

                                        });
                            }
                            stickersAdapter.notifyDataSetChanged();
                            viewDialog.hideDialog();
                        })
                        .addOnFailureListener(runnable -> {

                        });

            }
        } catch (Exception ex) {
            Log.e(TAG, "In_it_List: ERROR FROM CREATE ACTIVTY " + ex);
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Create_Activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
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


    private void loadSticker(String drawable_image) {


        Glide.with(this)
                .load(drawable_image)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        // This method is called when the image is successfully loaded
                        stickerView.addSticker(new DrawableSticker(resource));
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        // This method is called when the image loading is canceled
                    }
                });


//        Drawable bubble = ContextCompat.getDrawable(this, R.drawable.bubble);
//        stickerView.addSticker(
//                new com.greeting.greet_app.sticker.TextSticker(getApplicationContext(), bubble)
//                        .setText("Sticker\n")
//                        .setMaxTextSize(14)
//                        .resizeText()
//                );
    }


    public void testReplace(View view) {
        if (stickerView.replace(sticker)) {
            Toast.makeText(Create_Activity.this, "Replace Sticker successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Create_Activity.this, "Replace Sticker failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void testLock() {
        stickerView.setLocked(!stickerView.isLocked());
    }

    public void testRemove(View view) {
        if (stickerView.removeCurrentSticker()) {
            Toast.makeText(Create_Activity.this, "Remove current Sticker successfully!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(Create_Activity.this, "Remove current Sticker failed!", Toast.LENGTH_SHORT)
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
            TextSticker = new TextSticker(this);
            TextSticker.setText("" + text);
            TextSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
            TextSticker.resizeText();
            stickerView.addSticker(TextSticker);
        }
    }

    public void testAdd(View view) {
        final TextSticker sticker = new TextSticker(this);
        sticker.setText("Hello, world!");
        sticker.setTextColor(Color.BLUE);
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
//        sticker.resizeText();

        sticker.setMaxTextSize(12);
        stickerView.addSticker(sticker);
    }

    private void Get_Storage() {
        list_bg.clear();
        StorageReference listRef = FirebaseStorage.getInstance().getReference(Utils.Background);
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.e("Path", "" + prefix.getName());
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            Log.e("Path", "" + item.getPath());
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    list_bg.add(uri.toString());
                                    quotation_adapters_bg.notifyDataSetChanged();
                                    Log.e("url", uri.toString());
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }

}