package brooksm4.lsbu.ac.uk.imagefilterapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mukesh.image_processing.ImageProcessor;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button button;
    static final int CAM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //The below block of code sets up the spinner button
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Apply_Filter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        button =  findViewById(R.id.button);

        // The below Listener creates an intent that saves an image to memory once an image has been captured
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUEST);
            }
        });

    }

    private Matrix getMatrix(){

        File file = getFile();
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                break;
        }

        return matrix;
    }

    //The below block of code establishes a path and creates a folder to save the image
    private File getFile(){
        File folder = new File ("sdcard/Image_Filter_App");
        //The below "if" loop checks for any duplicates if not then it makes a directory.
        if (!folder.exists()){
            folder.mkdir();
        }
        File image_file = new File(folder,"/cam_image.jpg");
        return image_file;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getItemAtPosition(position).equals("Apply Filter")){

            //Do Nothing
        }

        if(parent.getItemAtPosition(position).equals("Black Filter")){

            ImageView imageView = findViewById(R.id.image_view);
            ImageProcessor imageProcessor = new ImageProcessor();
            File file = getFile();
            Matrix matrix = getMatrix();

            if (file.exists()) {

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
                Bitmap rotated = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
                rotated = imageProcessor.applyBlackFilter(rotated);
                imageView.setImageBitmap(rotated);

                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),text,Toast.LENGTH_LONG).show();

            }
        }

        if(parent.getItemAtPosition(position).equals("Grey Scale")){

            ImageView imageView = findViewById(R.id.image_view);
            ImageProcessor imageProcessor = new ImageProcessor();
            File file = getFile();
            Matrix matrix = getMatrix();


            if (file.exists()) {

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
                Bitmap rotated = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
                imageView.setImageBitmap(rotated);
                rotated = imageProcessor.doGreyScale(rotated);
                imageView.setImageBitmap(rotated);

                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),text,Toast.LENGTH_LONG).show();

            }
        }

        if(parent.getItemAtPosition(position).equals("Engrave")){

            ImageView imageView = findViewById(R.id.image_view);
            ImageProcessor imageProcessor = new ImageProcessor();
            File file = getFile();
            Matrix matrix = getMatrix();

            if (file.exists()) {

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
                Bitmap rotated = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
                imageView.setImageBitmap(rotated);
                rotated = imageProcessor.engrave(rotated);
                imageView.setImageBitmap(rotated);

                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),text,Toast.LENGTH_LONG).show();

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        //Do Nothing

    }

    /*The below block of code retrieves the saved image and creates a bitmap of the image and
    displays it in the imageView prior to filtering.
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final ImageView imageView = findViewById(R.id.image_view);
        File file = getFile();
        Matrix matrix = getMatrix();

        //As long as the file exist the below "if" loop will decode,scale and display an image in the imageview.
        if(file.exists()){
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
            Bitmap rotated = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
            imageView.setImageBitmap(rotated);

        }
    }

}