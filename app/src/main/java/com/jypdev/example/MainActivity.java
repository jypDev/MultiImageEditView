package com.jypdev.example;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jypdev.muitlimageeditview.model.ImageItem;
import com.jypdev.muitlimageeditview.view.MatrixDrawView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    MatrixDrawView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (MatrixDrawView) findViewById(R.id.matrix_view);

        view.addImageItem(new ImageItem(BitmapFactory.decodeResource(getResources(), R.drawable.sensor)));
        view.addImageItem(new ImageItem(BitmapFactory.decodeResource(getResources(), R.drawable.icon)));

        Button rotateButton = (Button) findViewById(R.id.rotate_button);
        Button removeButton = (Button) findViewById(R.id.remove_button);
        Button addButton = (Button) findViewById(R.id.add_button);
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
            view.setRemoveMode();
        }else if(id==R.id.add_button){
            view.addImageItem(new ImageItem(BitmapFactory.decodeResource(getResources(), R.drawable.icon)));
        }
    }
}
