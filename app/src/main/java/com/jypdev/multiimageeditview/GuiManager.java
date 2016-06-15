package com.jypdev.multiimageeditview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by JY-park on 16. 4. 5..
 */
public class GuiManager {

    private static GuiManager _Instance;

    private Button button;

    public static GuiManager getInstance(){
        if(_Instance==null){
            _Instance = new GuiManager();
        }
        return _Instance;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }


}
