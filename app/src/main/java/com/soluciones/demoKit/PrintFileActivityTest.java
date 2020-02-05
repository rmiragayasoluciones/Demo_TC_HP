package com.soluciones.demoKit;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.soluciones.demoKit.Dialogs.DetallePdfPreviewDialog2;

public class PrintFileActivityTest extends AppCompatActivity {
    private static final String TAG = "PrintFileActivityTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_file_test);

        getScreenSize();
        Button scan = findViewById(R.id.printBtn);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo poner accion (abrir dialog fragment)
                openFragment();
            }
        });

//        new LoadPrintCapabilitiesTask(this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void openFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetallePdfPreviewDialog2 dialog = new DetallePdfPreviewDialog2();
        dialog.show(fragmentManager, "test FullScreen Frag");
    }

    private void getScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e("Width", "" + width);
        Log.e("height", "" + height);
    }


}
