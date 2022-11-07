package com.example.demirmu_projet_tabata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class takePictureActivity extends AppCompatActivity {

    private Button buttonImage;
    private Button btnEnregistrer;

    private ImageView imageView, imgTest;
    private EditText editDescription, editNomPhoto;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private  Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);


        this.buttonImage = (Button) this.findViewById(R.id.button_image);
        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        this.btnEnregistrer = this.findViewById(R.id.btnEnregistrer);
        editDescription = findViewById(R.id.editDescription);
        editNomPhoto = findViewById(R.id.editNomPhoto);

        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // appel de la methode captureImage() pour créer un intention vers la caméra
                captureImage();
            }
        });
        this.btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editDescription.getText()) ||
                        TextUtils.isEmpty(editNomPhoto.getText())){
                    Toast.makeText(takePictureActivity.this, "AJOUTER IMPOSSIBLE : il y a des champs vides" , Toast.LENGTH_SHORT).show();

                }else{
                    MediaStore.Images.Media.insertImage(getContentResolver(), image, String.valueOf(editNomPhoto.getText()), String.valueOf(editDescription.getText()));
                    Toast.makeText(takePictureActivity.this, "La photo '"+String.valueOf(editNomPhoto.getText()) +"' a bien été enregistré dans vos photos !" , Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void captureImage() {
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }



    // When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                image = (Bitmap) data.getExtras().get("data");
                btnEnregistrer.setEnabled(true);
                this.imageView.setImageBitmap(image);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }

    }

}