package com.jypdev.example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jypdev.muitlimageeditview.model.ImageItem;
import com.jypdev.muitlimageeditview.view.MatrixDrawView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    MatrixDrawView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (MatrixDrawView) findViewById(R.id.matrix_view);

        view.addImageItem(new ImageItem(BitmapFactory.decodeResource(getResources(), R.drawable.sensor)));
        view.addImageItem(new ImageItem(BitmapFactory.decodeResource(getResources(), R.drawable.icon)));
        view.addImageItem(new ImageItem(BitmapFactory.decodeResource(getResources(), R.drawable.sparrow_icon)));
        view.addImageItem(new ImageItem(BitmapFactory.decodeResource(getResources(), R.drawable.chrome_icon)));

        view.setRotateImage(BitmapFactory.decodeResource(getResources(), R.drawable.rotate),30);
        Button rotateButton = (Button) findViewById(R.id.rotate_button);
        Button removeButton = (Button) findViewById(R.id.remove_button);
        Button addButton = (Button) findViewById(R.id.add_button);
        Button getButton = (Button) findViewById(R.id.getbitmap_button);
        getButton.setOnClickListener(this);
        rotateButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
        addButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id==R.id.rotate_button){
            view.setRotateMode();
        }else if(id==R.id.remove_button){
            view.removeImageItem();
        }else if(id==R.id.add_button){
            view.addImageItem(new ImageItem(BitmapFactory.decodeResource(getResources(), R.drawable.icon)));
        }else if(id==R.id.getbitmap_button){
            Bitmap bitmap = view.getPicture();
            FileOutputStream fos;
            try {
                fos = new FileOutputStream("/sdcard/capture.png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
