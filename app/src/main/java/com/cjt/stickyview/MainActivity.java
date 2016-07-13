package com.cjt.stickyview;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private SeekBar mSeekBar;
    private Switch mSwitch;
    private StickyView mStickyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSeekBar= (SeekBar) findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Toast.makeText(MainActivity.this,"progress "+progress,Toast.LENGTH_SHORT).show();
                mStickyView.setRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSwitch= (Switch) findViewById(R.id.switch_style);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
//                    Toast.makeText(MainActivity.this,"Open",Toast.LENGTH_SHORT).show();
                    mStickyView.setPaintStyle(Paint.Style.FILL);
                }else{
//                    Toast.makeText(MainActivity.this,"Close",Toast.LENGTH_SHORT).show();
                    mStickyView.setPaintStyle(Paint.Style.STROKE);
                }
            }
        });
        mStickyView= (StickyView) findViewById(R.id.circularview);
    }
}
