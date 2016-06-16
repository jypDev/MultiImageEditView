
### MultiImageEditView

> Android MultiImageEditView!

### Sample

![img](https://github.com/jypDev/multiImageEditView/blob/master/screenshot/Screenshot_2016-06-16-14-38-54.png)

### Usage

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.jypdev.muitlimageeditview.view.MatrixDrawView
        android:id="@+id/matrix_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <Button
        android:id="@+id/rotate_button"
        android:layout_width="100dp"
        android:layout_height="50dp"
        android:layout_alignParentBottom="true"
        android:text="rotate"/>

    <Button
        android:id="@+id/remove_button"
        android:layout_width="100dp"
        android:layout_height="50dp"
        android:layout_alignParentBottom="true"
        android:layout_toRightOf="@+id/rotate_button"
        android:text="remove"/>

    <Button
        android:id="@+id/add_button"
        android:layout_width="100dp"
        android:layout_height="50dp"
        android:layout_alignParentBottom="true"
        android:layout_toRightOf="@+id/remove_button"
        android:text="add"/>


</RelativeLayout>
```

```java
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
```

### Setup

1.add this in your `build.gradle` file in root project

```
allprojects {
    repositories {
        ...
        maven { url "https://www.jitpack.io" }
    }
}
```

2.add the following dependency

```
dependencies {
    compile 'com.github.jypdev:multiimageeditview:1.0.0'
}
```

### License
```
The MIT License (MIT)

Copyright (c) 2016 jypDev

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
