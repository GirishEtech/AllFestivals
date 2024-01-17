package com.greeting.greet_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.greeting.greet_app.Adapters.ColorListAdapter;
import com.greeting.greet_app.Adapters.DataAdapter;
import com.greeting.greet_app.Adapters.FilterAdapter;
import com.greeting.greet_app.Adapters.Gifs_Adapters;
import com.greeting.greet_app.Adapters.GradiantColorAdapter;
import com.greeting.greet_app.Adapters.StickersAdapter;
import com.greeting.greet_app.Model.FilterData;
import com.greeting.greet_app.Model.SimpleColor;
import com.rtugeek.android.colorseekbar.AlphaSeekBar;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.defaults.colorpicker.ColorWheelPalette;

public class Create_Activity extends AppCompatActivity implements View.OnTouchListener, ColorListner, SimpleColorListner, FilterAdapter.FilterClickListner {

    private Uri fileUri;
    ;
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                }
            });
    ImageView it_image, it_gradiant, it_color, it_gallary, it_bg;

    TextView txt_image, txt_gradiant, txt_color, txt_gallary, txt_bg, txtOpacity;
    top.defaults.colorpicker.ColorWheelPalette color_bg, GradiantColorBg;

    MyColorPicker bgPicker, TextPicker;
    ConstraintLayout layoutGif;
    private int originalColor = 1;
    private ColorBox colorTextGradiant, colorBgGradiant;
    private ViewDialog viewDialog;
    private static final String TAG = "Touch";
    public static final int PERM_RQST_CODE = 110;
    public static final int CAMERA_REQUEST = 192;
    private com.xiaopo.flying.sticker.StickerView stickerView;
    private TextSticker sticker;
    private com.greeting.greet_app.sticker.TextSticker TextSticker;
    private Boolean isColor = true;
    private EditText tv_feedback_text;
    private FilterAdapter filterAdapter;
    private AlphaSeekBar kavehColorAlphaSlider, kavehColorAlphaBackground;
    @SuppressWarnings("unused")
    StickersAdapter stickersAdapter;

    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;
    DataAdapter dataAdapter1;
    GradiantColorAdapter gradiantColorAdapter;
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
    ImageView main_img, ic_backBtn, tempImage;
    Activity activity;
    Toolbar toolbar;
    String UserMobileId = "";
    ColorWheelPalette add_color_btn, add_gradiant_color;
    LinearLayout bottom_nav_card, add_gradiant_layout, nav_add_color, nav_add_text, nav_add_filters, nav_add_sticker, colorForbg;
    LinearLayout nav_add_img, bottom_add_layout, bottom_color_layout, bottom_add_sticker_layout, bottom_add_filters_layout;
    RecyclerView Choose_Sticker_RecyclerView, Choose_Filters_RecyclerView, gradiantList, filterList;
    TextView tv_title, tv_opacity_per;
    ConstraintLayout rv_cancel_done, seekLayout;
    ImageView ic_cross, ic_done, ic_crossSticker, ic_doneSticker, ic_crossFilter, ic_doneFilter;
    Gifs_Adapters quotation_adapters;

    BG_Adapters quotation_adapters_bg;

    ArrayList<String> list = new ArrayList<>();

    ArrayList<String> list_bg = new ArrayList<>();

    ConstraintLayout gallery_img, camera_img, color_btn, grediant_btn;
    RecyclerView simple_Color, grediant_Color, listColors;
    TextView text, tv_opacity_perBg;
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
    LinearLayout bg_image, grediant;

    ConstraintLayout bg_img;
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

                        setLayoutParams();
                        main_img.setImageBitmap(photo);
                    }
                }
            });

    @SuppressLint({"MissingInflatedId", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_create);
        getWindow().setStatusBarColor(getResources().getColor(R.color.teal_200));
        activity = this;
        UserMobileId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        toolbar = findViewById(R.id.toolbar);

        it_bg = findViewById(R.id.it_bg);
        it_image = findViewById(R.id.it_image);
        it_color = findViewById(R.id.it_color);
        it_gallary = findViewById(R.id.it_galary);
        it_gradiant = findViewById(R.id.it_gradiant);

        txt_bg = findViewById(R.id.txt_bg);
        txt_image = findViewById(R.id.txt_image);
        txt_color = findViewById(R.id.txt_color);
        txt_gallary = findViewById(R.id.txt_galary);
        txt_gradiant = findViewById(R.id.txt_gradiant);
        txtOpacity = findViewById(R.id.txtOpacity);


        filterList = findViewById(R.id.filterList);
        List<FilterData> data = getFilterList();
        filterAdapter = new FilterAdapter(data, this);
        filterList.setAdapter(filterAdapter);
        color_bg = findViewById(R.id.color_bg);
        GradiantColorBg = findViewById(R.id.Gradiant_bgPalate);
        colorForbg = findViewById(R.id.colorForbg);
        bg_img = findViewById(R.id.bg_img);
        bg_image = findViewById(R.id.bg_image);
        simple_image = findViewById(R.id.simple_image);
        gallery_img = findViewById(R.id.gallery_img);
        simple_Color = findViewById(R.id.simple_Color);
        main_img = findViewById(R.id.main_img);
        tempImage = findViewById(R.id.tempImage);
        layoutGif = findViewById(R.id.Layoutgif);
        color_btn = findViewById(R.id.color_btn);
        ic_crossFilter = findViewById(R.id.ic_crossFilter);
        ic_doneFilter = findViewById(R.id.ic_doneFilter);
        ic_backBtn = findViewById(R.id.ic_nav_menu);
        grediant = findViewById(R.id.grediant);
        grediant_Color = findViewById(R.id.grediant_Color);
        listColors = findViewById(R.id.listColors);
        gradiantList = findViewById(R.id.listGradiant);
        grediant_btn = findViewById(R.id.gredient_btn);
        seekLayout = findViewById(R.id.seekMain);
        tv_opacity_perBg = findViewById(R.id.tv_opacity_perBg);
        kavehColorAlphaSlider = findViewById(R.id.alphaSeekBar);
        kavehColorAlphaBackground = findViewById(R.id.alphaSeekBarBackground);
        setSupportActionBar(toolbar);
        viewDialog = new ViewDialog(this);
        In_it_List();
        Choose_Sticker_RecyclerView = findViewById(R.id.Choose_Sticker_RecyclerView);
//        Choose_Filters_RecyclerView = findViewById(R.id.Choose_Filters_RecyclerView);
        bottom_add_sticker_layout = findViewById(R.id.bottom_add_sticker_layout);
        bottom_add_filters_layout = findViewById(R.id.bottom_add_filters_layout);
        nav_add_text = findViewById(R.id.nav_add_text);
        tv_opacity_per = findViewById(R.id.tv_opacity_per);
        nav_add_filters = findViewById(R.id.nav_add_filters);
        add_color_btn = findViewById(R.id.add_color_layout);
        add_gradiant_color = findViewById(R.id.add_gradiant_color);
        nav_add_sticker = findViewById(R.id.nav_add_sticker);
        ic_cross = findViewById(R.id.ic_cross);
        ic_done = findViewById(R.id.ic_done);
        ic_crossSticker = findViewById(R.id.ic_crossSticker);
        ic_doneSticker = findViewById(R.id.ic_doneSticker);
        bottom_nav_card = findViewById(R.id.bottom_nav_card);
        nav_add_img = findViewById(R.id.nav_add_img);
        tv_title = findViewById(R.id.tv_title);
        bottom_add_layout = findViewById(R.id.bottom_add_layout);
        rv_cancel_done = findViewById(R.id.handleDiologeBG);
        bottom_color_layout = findViewById(R.id.bottom_color_layout);
        nav_add_color = findViewById(R.id.nav_add_color);
        camera_img = findViewById(R.id.camera_img);
        main_img.setOnTouchListener(this);
        colorTextGradiant = new ColorBox(this, this, this, "TEXTCOLOR");
        colorBgGradiant = new ColorBox(this, this, this, "GRADIANTBACK");
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
        simple_image.setLayoutManager(new LinearLayoutManager(Create_Activity.this, LinearLayoutManager.HORIZONTAL, false));
        quotation_adapters_bg = new BG_Adapters(getApplicationContext(), list_bg, Utils.Background);
        simple_image.setAdapter(quotation_adapters_bg);
        //main_img.setBackgroundColor(getColor(R.color.white));
        Glide.with(this)
                .asGif().load(R.raw.bg_gif)
                .into(tempImage);
        TextPicker = new MyColorPicker(this, this, this, "TEXT");
        bgPicker = new MyColorPicker(this, this, this, "BACKGROUND");
        Get_Storage();
        ic_backBtn.setOnClickListener(view -> {
            onBackPressed();
            Log.i(TAG, "onCreate: BackPress Clicked");
        });
        bg_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grediant.setVisibility(View.GONE);
                colorForbg.setVisibility(View.GONE);
                bg_image.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
                txtOpacity.setVisibility(View.GONE);
                setDefaultColor("BG");
                quotation_adapters_bg.setOnClickListener((position, model) -> {
                    setLayoutParams();
                    Glide.with(Create_Activity.this).load(model).into(main_img);
                });
                // setLeftRightClick(simple_image);
            }
        });
        add_color_btn.setOnClickListener(this::showColorPickerDiologe);


        ic_crossFilter.setOnClickListener(v -> {
            Set_Layout_Gone();
        });
        ic_doneFilter.setOnClickListener(v -> {
            Set_Layout_Gone();
        });
        ic_crossSticker.setOnClickListener(v -> {
            Set_Layout_Gone();
        });
        ic_doneSticker.setOnClickListener(v -> {
            Set_Layout_Gone();
        });
        camera_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    permission();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        gallery_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                setDefaultColor("GALLARY");
                grediant.setVisibility(View.GONE);
                bg_image.setVisibility(View.GONE);
                colorForbg.setVisibility(View.GONE);
                seekLayout.setVisibility(View.GONE);
                txtOpacity.setVisibility(View.GONE);
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
        color_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grediant.setVisibility(View.GONE);
                bg_image.setVisibility(View.GONE);
                colorForbg.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.VISIBLE);
                txtOpacity.setVisibility(View.VISIBLE);
                setDefaultColor("COLOR");
                // this is for Custom Color wheel
                color_bg.setOnClickListener(view1 -> {
                    bgPicker.showDialog();
                });
                //setLeftRightClick(colorForbg);
            }
        });
        this.simple_Color.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.simple_Color.setHasFixedSize(true);
        String[] colorNames = getResources().getStringArray(R.array.colorNames);
        ArrayList<SimpleColor> arrayList = new ArrayList<>();
        for (int i = 0; i < colorNames.length; i++) {
            SimpleColor m = new SimpleColor();
            TypedArray ta = getResources().obtainTypedArray(R.array.colors);
            m.setColor(ta.getColor(i, 0));
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
                GradiantColorBg.setOnClickListener(view1 -> {
                    isColor = false;
                    colorBgGradiant.showDialog();
                });
                setDefaultColor("GRADIANT");
                //bottom_add_layout.setVisibility(View.VISIBLE);
                grediant.setVisibility(View.VISIBLE);
                bg_image.setVisibility(View.GONE);
                colorForbg.setVisibility(View.GONE);
                seekLayout.setVisibility(View.VISIBLE);
                txtOpacity.setVisibility(View.VISIBLE);
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
                        setLayoutParams();
                        main_img.setImageResource(model.getColorToUse());
                        setAlphaForBackground();
                    }
                });
                //setLeftRightClick(grediant);
            }
        });
        dataAdapter.setOnClickListener(new DataAdapter.OnClickListener() {
            @Override
            public void onClick(int position, SimpleColor model) {
                setLayoutParams();
                main_img.setImageResource(arrayList.get(position).getColorToUse());
                setAlphaForBackground();
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
                seekLayout.setVisibility(View.GONE);
                txtOpacity.setVisibility(View.GONE);
            }
        });
        nav_add_color.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                bottom_nav_card.setVisibility(View.GONE);
                bottom_add_layout.setVisibility(View.VISIBLE);
                bottom_add_sticker_layout.setVisibility(View.GONE);
                bottom_add_filters_layout.setVisibility(View.GONE);
                tv_title.setText(getString(R.string.Color));
                bottom_color_layout.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
                setGradiantColorList();
                setColorsList();
            }
        });
        findViewById(R.id.ic_crossColor).setOnClickListener(v -> {
            Set_Layout_Gone();
        });
        findViewById(R.id.ic_doneColor).setOnClickListener(v -> {
            Set_Layout_Gone();
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
                defaultClick();
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
                defaultClick();
            }
        });
        stickersAdapter.setOnClickListener(new StickersAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                setLayoutParams();
                stickerView.setBackgroundColor(getColor(R.color.bg_color));
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
                defaultClick();
            }
        });
        defaultClick();
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

//        stickerView.setBackgroundColor(Color.WHITE);
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

    private void defaultClick() {
        ic_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set_Layout_Gone();
                setDefaultColor();
            }
        });
        ic_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set_Layout_Gone();
                setDefaultColor();
            }
        });
    }

    private void setLeftRightClick(View view1) {
        bottom_add_layout.setVisibility(View.GONE);
        view1.setVisibility(View.VISIBLE);
        ic_cross.setOnClickListener(view -> {
            if (view1.getVisibility() == View.VISIBLE) {
                rv_cancel_done.setVisibility(View.VISIBLE);
                bottom_add_layout.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);
                seekLayout.setVisibility(View.GONE);
            } else {
                bottom_add_layout.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
                rv_cancel_done.setVisibility(View.GONE);
                bottom_nav_card.setVisibility(View.VISIBLE);
            }

        });
        ic_done.setOnClickListener(view -> {
            bottom_add_layout.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            rv_cancel_done.setVisibility(View.GONE);
            bottom_nav_card.setVisibility(View.VISIBLE);
        });
    }

    private void setAlphaForBackground() {
        kavehColorAlphaBackground.setOnAlphaChangeListener((progress, alpha) -> {
            int percentage;
            if (progress == 0) {
                percentage = 1;
            } else {
                int max = kavehColorAlphaBackground.getMaxProgress();
                percentage = (int) ((int) progress / (float) max * 100.0f);
            }

            tv_opacity_perBg.setText("" + percentage + "%");
            main_img.setImageAlpha(alpha);
        });
    }

    private void showGradiantDioloue() {
        Set_Layout_Gone();
        TextSticker = getSelected();
        isColor = false;
        colorTextGradiant.showDialog();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showColorPickerDiologe(View view) {
        isColor = true;
        TextSticker = getSelected();
        setSeekBarOposity();
        TextPicker.showDialog();
    }

    public void setSeekBarOposity() {
        kavehColorAlphaSlider.setOnAlphaChangeListener((progress, alpha) -> {
            if (TextSticker != null) {
                int percentage;
                if (progress == 0) {
                    percentage = 1;
                } else {
                    int max = kavehColorAlphaSlider.getMaxProgress();
                    percentage = (int) ((int) progress / (float) max * 100.0f);
                }

                Log.d(TAG, "showColorPickerDiologe: Percentage " + percentage);
                tv_opacity_per.setText("" + percentage + "%");
                if (isColor) {
                    TextSticker.setTextAlpha(originalColor, alpha);
                    stickerView.invalidate();
                } else {
                    Log.d(TAG, "showColorPickerDiologe: Condition is  True and Progress :" + percentage);
                    TextSticker.setGradientOpacity(alpha);
                    stickerView.invalidate();
                }
            }
        });
    }


    private void permission() throws IOException {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 2);
            permission();
        } else {
            try {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } catch (Exception ex) {
                Log.e("PROJECT_TEST", ex.getMessage());
            }

        }
    }

    private void Set_Layout_Gone() {
        bottom_nav_card.setVisibility(View.VISIBLE);
        rv_cancel_done.setVisibility(View.GONE);
        bottom_add_layout.setVisibility(View.GONE);
        bottom_color_layout.setVisibility(View.GONE);
        bottom_add_sticker_layout.setVisibility(View.GONE);
        bottom_add_filters_layout.setVisibility(View.GONE);
        colorForbg.setVisibility(View.GONE);
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
                setLayoutParams();
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
                StorageReference ref = FirebaseStorage.getInstance().getReference(Utils.categoryModel.getPath() + "/" + Utils.Stickers);
                Log.i(TAG, "In_it_List: REFERENCE PATH" + ref.getPath());
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
        if (reqCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                main_img.setImageBitmap(photo);
                setLayoutParams();
            }
            catch (Exception exception){
                Log.i(TAG, "onActivityResult: Exception"+exception);
            }

        } else if (reqCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                main_img.setImageBitmap(selectedImage);
                setLayoutParams();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Create_Activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(Create_Activity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
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
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
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

            TextSticker = new com.greeting.greet_app.sticker.TextSticker(this);
            TextSticker.setText("" + text);
            TextSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
            TextSticker.resizeText();
            stickerView.addSticker(TextSticker);
        }
    }


    private void Get_Storage() {
        list_bg.clear();
        String Category = getIntent().getStringExtra(Utils.CAT_TYPE);
        String child = getIntent().getStringExtra(Utils.TAB_NAME);
        Log.i(TAG, "Get_Storage: Category : -> " + Category + "\n" + "Child ->" + child);
        try {
            StorageReference listRef = FirebaseStorage.getInstance().getReference(Utils.categoryModel.getPath() + "/" + Utils.Background);
            if (listRef != null && listRef.getStorage() != null) {
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
                                viewDialog.hideDialog();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                viewDialog.hideDialog();
                            }
                        });
            }
        } catch (Exception exception) {
            Log.e(TAG, "Get_Storage: Exception", exception);
            //viewDialog.hideDialog();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setTxtColor(int[] colors) {
        if (TextSticker != null) {
            TextSticker = (com.greeting.greet_app.sticker.TextSticker) stickerView.getCurrentSticker();
            TextSticker.setGradientColor(colors[0], colors[1]);
            kavehColorAlphaSlider.setAlphaValue(kavehColorAlphaSlider.getMaxProgress());
            setSeekBarOposity();
        }
        stickerView.invalidate();
    }

    @Override
    public void setBackgroundColor(GradientDrawable drawable) {
        setLayoutParams();
        main_img.setImageDrawable(drawable);
        kavehColorAlphaBackground.setAlphaValue(kavehColorAlphaBackground.getMaxProgress());
        setAlphaForBackground();
    }

    public com.greeting.greet_app.sticker.TextSticker getSelected() {
        try {
            return (com.greeting.greet_app.sticker.TextSticker) stickerView.getCurrentSticker();
        } catch (Exception e) {
            Log.e(TAG, "getSelected: ERROR BUT NO PROBLEM", e);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setGradiantColorList() {
        setSeekBarOposity();
        gradiantList.setLayoutManager(new LinearLayoutManager(Create_Activity.this, RecyclerView.HORIZONTAL, false));
        gradiantList.setHasFixedSize(true);
        ArrayList<SimpleColor> arrayList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            SimpleColor m = new SimpleColor();
            TypedArray ta = getResources().obtainTypedArray(R.array.frag_home_ids);
            int colorToUse = ta.getResourceId(i, 0);
            m.setColorToUse(colorToUse);
            arrayList.add(m);
        }
        gradiantColorAdapter = new GradiantColorAdapter(Create_Activity.this, arrayList);
        gradiantList.setAdapter(gradiantColorAdapter);
        gradiantColorAdapter.setOnClickListener((position, model) ->
        {
            isColor = false;
            TextSticker = getSelected();
            if (TextSticker != null) {
                int[] arr = model.getColors();
                int startColor = arr != null ? arr[0] : 0;
                int endColor = arr != null ? arr[1] : 0;
                TextSticker.setGradientColor(startColor, endColor);
                stickerView.invalidate();
            }
            Log.d(TAG, "setGradiantColorList: Gradiant Color :-" + model);
        });
    }

    public void setColorsList() {
        TextSticker = getSelected();
        this.listColors.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.listColors.setHasFixedSize(true);
        String[] colorNames = getResources().getStringArray(R.array.colorNames);
        ArrayList<SimpleColor> arrayList = new ArrayList<>();
        for (int i = 0; i < colorNames.length; i++) {
            SimpleColor m = new SimpleColor();
            TypedArray ta = getResources().obtainTypedArray(R.array.colors);
            int colorToUse = ta.getResourceId(i, 0);
            m.setColor(ta.getColor(i, 0));
            m.setColorToUse(colorToUse);
            arrayList.add(m);
        }
        ColorListAdapter dataAdapter = new ColorListAdapter(this, arrayList);
        this.listColors.setAdapter(dataAdapter);
        dataAdapter.setOnClickListener((position, model) -> {
            if (TextSticker != null) {
                Log.i(TAG, "setColorsList: Color is " + model.getColorToUse());
                TextSticker.setTextColor(model.getColor());
                originalColor = model.getColor();
                setSeekBarOposity();
                stickerView.invalidate();
            }
        });
    }

    void setLayoutParams() {
        if (layoutGif.getVisibility() == View.VISIBLE) {
            layoutGif.setVisibility(View.GONE);
            main_img.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setDrawable(GradientDrawable drawable) {
        int percentage;
        setLayoutParams();

        int max = kavehColorAlphaBackground.getMaxProgress();
        percentage = (int) ((int) max / (float) max * 100.0f);
        tv_opacity_perBg.setText("" + percentage + "%");
        kavehColorAlphaBackground.setAlphaValue(drawable.getAlpha());
        main_img.setImageDrawable(drawable);
        setAlphaForBackground();
    }

    @Override
    public void setTextStickerColor(int color) {
        int percentage;
        if (TextSticker != null) {
            //Toast.makeText(activity, "text color "+color, Toast.LENGTH_SHORT).show();
            TextSticker.setTextColor(color);
            originalColor = color;
            int max = kavehColorAlphaSlider.getMaxProgress();
            kavehColorAlphaSlider.setAlphaValue(kavehColorAlphaSlider.getMaxProgress());
            percentage = (int) ((int) max / (float) max * 100.0f);
            tv_opacity_perBg.setText("" + percentage + "%");
            stickerView.invalidate();
        }
    }

    private void setDefaultColor(String TYPE) {
        if (TYPE == "IMAGE") {
            setYellow(it_gallary, txt_gallary);
            setWhiteColor(it_color, txt_color);
            setWhiteColor(it_bg, txt_bg);
            setWhiteColor(it_gradiant, txt_gradiant);
            setWhiteColor(it_image, txt_image);
        } else if (TYPE == "BG") {
            setYellow(it_bg, txt_bg);
            setWhiteColor(it_color, txt_color);
            setWhiteColor(it_gallary, txt_gallary);
            setWhiteColor(it_gradiant, txt_gradiant);
            setWhiteColor(it_image, txt_image);
        } else if (TYPE == "GALLARY") {
            setYellow(it_gallary, txt_gallary);
            setWhiteColor(it_color, txt_color);
            setWhiteColor(it_bg, txt_bg);
            setWhiteColor(it_gradiant, txt_gradiant);
            setWhiteColor(it_image, txt_image);
        } else if (TYPE == "COLOR") {
            setYellow(it_color, txt_color);
            setWhiteColor(it_gallary, txt_gallary);
            setWhiteColor(it_bg, txt_bg);
            setWhiteColor(it_gradiant, txt_gradiant);
            setWhiteColor(it_image, txt_image);
        } else if (TYPE == "GRADIANT") {
            setYellow(it_gradiant, txt_gradiant);
            setWhiteColor(it_gallary, txt_gallary);
            setWhiteColor(it_bg, txt_bg);
            setWhiteColor(it_color, txt_color);
            setWhiteColor(it_image, txt_image);
        }
    }

    private void setYellow(ImageView bg, TextView txt) {
        bg.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
        txt.setTextColor(getResources().getColor(R.color.yellow));
    }

    private void setWhiteColor(ImageView bg, TextView txt) {
        bg.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        txt.setTextColor(getResources().getColor(R.color.white));
    }

    private void setDefaultColor() {
        setWhiteColor(it_gradiant, txt_gradiant);
        setWhiteColor(it_gallary, txt_gallary);
        setWhiteColor(it_bg, txt_bg);
        setWhiteColor(it_color, txt_color);
        setWhiteColor(it_image, txt_image);
    }

    @Override
    public void onFilterClick(int color) {
        if (main_img.getVisibility() != View.VISIBLE) {
        } else {
            setLayoutParams();
            main_view.setBackgroundColor(color);
        }
    }

    private List<FilterData> getFilterList() {
        List<FilterData> data = new ArrayList<>();
        data.add(new FilterData(0x66000000, R.drawable.abc));
        data.add(new FilterData(0x66F44336, R.drawable.abc));
        data.add(new FilterData(0x6600BCD4, R.drawable.abc));
        data.add(new FilterData(0x664C0B0B, R.drawable.abc));
        data.add(new FilterData(0xA64CAF50, R.drawable.abc));
        data.add(new FilterData(0x66FFEB3B, R.drawable.abc));
        data.add(new FilterData(0x66C39F93, R.drawable.abc));
        data.add(new FilterData(0x669C27B0, R.drawable.abc));
        data.add(new FilterData(0x00000000, R.drawable.abc));
        return data;
    }
}
