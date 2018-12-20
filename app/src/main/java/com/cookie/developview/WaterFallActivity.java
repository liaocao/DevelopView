package com.cookie.developview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class WaterFallActivity extends AppCompatActivity {

    private static int IMG_COUNT = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_fall);

        final WaterFallLayout waterfallLayout = ((WaterFallLayout)findViewById(R.id.waterfallLayout));
        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView(waterfallLayout);
            }
        });
    }

    public void addView(WaterFallLayout waterfallLayout) {

        Random random = new Random();
        Integer num = Math.abs(random.nextInt());
        WaterFallLayout.LayoutParams layoutParams = new WaterFallLayout.LayoutParams(WaterFallLayout.LayoutParams.WRAP_CONTENT,
                WaterFallLayout.LayoutParams.WRAP_CONTENT);

        ImageView imageView = new ImageView(this);
        if (num % IMG_COUNT == 0) {
            imageView.setImageResource(R.mipmap.pic1);
        } else if (num % IMG_COUNT == 1) {
            imageView.setImageResource(R.mipmap.pic2);
        } else if (num % IMG_COUNT == 2) {
            imageView.setImageResource(R.mipmap.pic3);
        } else if (num % IMG_COUNT == 3) {
            imageView.setImageResource(R.mipmap.pic4);
        } else if (num % IMG_COUNT == 4) {
            imageView.setImageResource(R.mipmap.pic5);
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        waterfallLayout.addView(imageView, layoutParams);

        waterfallLayout.setOnItemClickListener(new WaterFallLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int index) {
                Toast.makeText(WaterFallActivity.this, "item=" + index, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
