package tw.org.iii.brad.brad20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

import ir.sohreco.androidfilechooser.ExternalStorageNotAvailableException;
import ir.sohreco.androidfilechooser.FileChooser;

public class MainActivity extends AppCompatActivity
    implements FileChooser.ChooserListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);
        }else{
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        init();
    }
    private void init(){
    }

    public void test1(View view) {
        FileChooser.Builder builder = new FileChooser.Builder(
                FileChooser.ChooserType.FILE_CHOOSER, this)
                .setMultipleFileSelectionEnabled(false)
                .setFileFormats(new String[] {".jpg"})
                .setListItemsTextColor(R.color.colorPrimary)
                .setPreviousDirectoryButtonIcon(R.drawable.ic_prev_dir)
                .setDirectoryIcon(R.drawable.ic_directory)
                .setFileIcon(R.drawable.ic_file)
                // And more...
                ;

        try {
            FileChooser fileChooserFragment = builder.build();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fileChooserFragment)
                    .commit();

        } catch (ExternalStorageNotAvailableException e) {
            e.printStackTrace();
        }

    }

    public void upload(View view) {
        new Thread(){
            @Override
            public void run() {
                doUpload();
            }
        }.start();
    }

    private void doUpload(){
        try {
            MultipartUtility mu = new MultipartUtility(
                    "http://10.0.103.39:8080/Brad/Brad07",
                    "",
                    "UTF-8");
            mu.addFormField("prefix", "android");
            mu.addFilePart("upload", uploadFile);
            mu.finish();
            Log.v("brad", "Upload OK");
        } catch (IOException e) {
            Log.v("brad", "Upload failure");
        }
    }

    File uploadFile;

    @Override
    public void onSelect(String path) {
        uploadFile = new File(path);
        Log.v("brad", path);
    }
}
