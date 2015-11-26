package com.enterpaper.comepenny.util;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.enterpaper.comepenny.R;

/**
 * Created by Kim on 2015-11-26.
 */
public class ColorPickerDialog extends Dialog implements SeekBar.OnSeekBarChangeListener {

    public Activity activity;
    public Dialog dialog;

    Button btnCancel,btnSelect;
    View colorView;
    SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    TextView redToolTip, greenToolTip, blueToolTip;
    EditText codHex;
    private int red, green, blue;
    int seekBarLeft;
    Rect thumbRect;
    int id;


    public ColorPickerDialog(Activity activity, int id) {
        super(activity);
        this.id = id;

        this.activity = activity;
        this.red = 0;
        this.green = 0;
        this.blue = 0;
    }

    public ColorPickerDialog(Activity activity) {
        super(activity);

        this.activity = activity;
        this.red = 0;
        this.green = 0;
        this.blue = 0;
    }

    public ColorPickerDialog(Activity activity, int r, int g, int b) {
        super(activity);

        this.activity = activity;

        if(0 <= r && r <=255)
            this.red = r;
        else
            this.red = 0;

        if(0 <= r && r <=255)
            this.green = g;
        else
            this.green = 0;

        if(0 <= r && r <=255)
            this.blue = b;
        else
            this.green = 0;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_color_picker);
        // dialog 밖 터치시 꺼지지 않게게
        setCanceledOnTouchOutside(false);

        initializeLayout();

        setListener();


    }

    private void initializeLayout(){
        colorView = findViewById(R.id.colorView);

        redSeekBar = (SeekBar)findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar)findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar)findViewById(R.id.blueSeekBar);

        seekBarLeft = redSeekBar.getPaddingLeft();

        redToolTip = (TextView)findViewById(R.id.redToolTip);
        greenToolTip = (TextView)findViewById(R.id.greenToolTip);
        blueToolTip = (TextView)findViewById(R.id.blueToolTip);

        codHex = (EditText)findViewById(R.id.codHex);

        btnSelect = (Button)findViewById(R.id.btnColorPickerDialogSelect);
        btnCancel = (Button)findViewById(R.id.btnColorPickerDialogCancel);
    }

    private void setListener(){
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);

        colorView.setBackgroundColor(Color.rgb(red, green, blue));

        codHex.setText(String.format("%02x%02x%02x", red, green, blue));

        codHex.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            updateColorView(v.getText().toString());
                            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(codHex.getWindowToken(), 0);

                            return true;
                        }
                        return false;
                    }
                });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClose();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, activity, AppWidgetMain.class);
                intent.setAction("CLICK");
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,id);
                intent.putExtra("rgb", getColor());
                activity.sendBroadcast(intent);
                dismiss();
                activity.finish();
            }
        });
    }


    /**
     * Method that syncrhonize the color between the bars, the view and the HEC code text.
     *
     * @param s HEX Code of the color.
     */
    private void updateColorView(String s) {
        if(s.matches("-?[0-9a-fA-F]+")){
            int color = (int)Long.parseLong(s, 16);
            red = (color >> 16) & 0xFF;
            green = (color >> 8) & 0xFF;
            blue = (color >> 0) & 0xFF;

            colorView.setBackgroundColor(Color.rgb(red, green, blue));
            redSeekBar.setProgress(red);
            greenSeekBar.setProgress(green);
            blueSeekBar.setProgress(blue);
        }
        else{
            codHex.setError("incorrect");
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus){

        thumbRect = redSeekBar.getThumb().getBounds();

        redToolTip.setX(seekBarLeft + thumbRect.left);
        if (red<10)
            redToolTip.setText("  "+red);
        else if (red<100)
            redToolTip.setText(" "+red);
        else
            redToolTip.setText(red+"");

        thumbRect = greenSeekBar.getThumb().getBounds();

        greenToolTip.setX(seekBarLeft + thumbRect.left);
        if (green<10)
            greenToolTip.setText("  "+green);
        else if (red<100)
            greenToolTip.setText(" "+green);
        else
            greenToolTip.setText(green+"");

        thumbRect = blueSeekBar.getThumb().getBounds();

        blueToolTip.setX(seekBarLeft + thumbRect.left);
        if (blue<10)
            blueToolTip.setText("  "+blue);
        else if (blue<100)
            blueToolTip.setText(" "+blue);
        else
            blueToolTip.setText(blue+"");

    }

    /**
     * Method called when the user change the value of the bars. This sync the colors.
     *
     * @param seekBar SeekBar that has changed
     * @param progress The new progress value
     * @param fromUser If it coem from User
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.redSeekBar) {

            red = progress;
            thumbRect = seekBar.getThumb().getBounds();

            redToolTip.setX(seekBarLeft + thumbRect.left);

            if (progress<10)
                redToolTip.setText("  " + red);
            else if (progress<100)
                redToolTip.setText(" "+red);
            else
                redToolTip.setText(red+"");

        }
        else if (seekBar.getId() == R.id.greenSeekBar) {

            green = progress;
            thumbRect = seekBar.getThumb().getBounds();

            greenToolTip.setX(seekBar.getPaddingLeft()+thumbRect.left);
            if (progress<10)
                greenToolTip.setText("  "+green);
            else if (progress<100)
                greenToolTip.setText(" "+green);
            else
                greenToolTip.setText(green+"");

        }
        else if (seekBar.getId() == R.id.blueSeekBar) {

            blue = progress;
            thumbRect = seekBar.getThumb().getBounds();

            blueToolTip.setX(seekBarLeft + thumbRect.left);
            if (progress<10)
                blueToolTip.setText("  "+blue);
            else if (progress<100)
                blueToolTip.setText(" "+blue);
            else
                blueToolTip.setText(blue+"");

        }

        colorView.setBackgroundColor(Color.rgb(red, green, blue));

        //Setting the inputText hex color
        codHex.setText(String.format("%02x%02x%02x", red, green, blue));

    }


    // 다이얼로그 닫을 때
    private void dialogClose(){
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, activity, AppWidgetMain.class);
        intent.setAction("CLICK");
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        intent.putExtra("rgb", Color.rgb(0, 0, 0));
        activity.sendBroadcast(intent);
        dismiss();
        activity.finish();
    }

    @Override
    public void onBackPressed() {
        dialogClose();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getColor(){
        return Color.rgb(red, green, blue);
    }}
