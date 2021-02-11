package com.annequinpoulain.biblioygg;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserLivres extends AppCompatActivity {
    Button btAjouter,btPhoto;
    EditText etPrix,etTitre,etAuteur,etCat,etDesc,etStat;
    Switch statut;
    char stat;
    TextView tvTitre;
    AlertDialog.Builder builder;
    Intent intent;
    ImageView image;
    private int REQUEST_CODE = 1;
    Bitmap bitmap;
    String currentPhotoPath;
    File fichierPhoto;
    String photoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_livres);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Ajout d'un livre");

        invalidateOptionsMenu();

        image = findViewById(R.id.image);
        //btChoisir = findViewById(R.id.btChoisir);
        btPhoto = findViewById(R.id.btPhoto);
        etAuteur = findViewById(R.id.etAuteur);
        etCat = findViewById(R.id.etCat);
        etDesc = findViewById(R.id.etDesc);
        statut = findViewById(R.id.statut);
        btAjouter = findViewById(R.id.btAjouter);
        etPrix = findViewById(R.id.etPrix);
        etTitre = findViewById(R.id.etTitre);
        tvTitre = findViewById(R.id.textView);
        builder = new AlertDialog.Builder(this);

        btPhoto.setVisibility(View.INVISIBLE);
        photoPath = "";

        if(verifierPermissions())
        {
            lancerProgramme();
        }


        if(SingletonSession.Instance().getPosModif() != -1)
        {
            Livre livre = SingletonSession.Instance().getLivreById(SingletonSession.Instance().getPosModif());
            toolbar.setTitle("Modification d'un livre");
            tvTitre.setText("Modifier le livre \"" + livre.getNom() + "\"");
            btAjouter.setText("Modifier");
            etTitre.setText(livre.getNom());
            etPrix.setText(String.valueOf(livre.getPrix()));
            etAuteur.setText(livre.getAuteur());
            etCat.setText(livre.getCat());
            etDesc.setText(livre.getDescription());

            if(livre.getStat() == 'R'){
                statut.setChecked(true);
            }
            else{
                statut.setChecked(false);
            }

            btPhoto.setVisibility(View.INVISIBLE);

        }
/*
        btChoisir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Choisissez une image"),REQUEST_CODE);
            }
        });*/

        btAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (statut.isChecked())
                    stat = 'R';
                else
                    stat = 'D';

                if(etPrix.getText().toString().equals("") || etTitre.getText().toString().trim().equals("") || etAuteur.getText().toString().equals("") || etCat.getText().toString().equals("") || etDesc.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Vous devez remplir tous les champs", Toast.LENGTH_LONG).show();
                }
                else if (SingletonSession.Instance().getPosModif() != -1){

                    builder.setTitle("Confirmer la modification");
                    builder.setMessage("Voulez-vous vraiement modifier le livre \"" + SingletonSession.Instance().getLivreById(SingletonSession.Instance().getPosModif()).getNom() + "\" ?");
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SingletonSession.Instance().modLivre(SingletonSession.Instance().getPosModif(), etTitre.getText().toString(), Double.parseDouble(etPrix.getText().toString()), etAuteur.getText().toString().trim(), etCat.getText().toString().trim(), etDesc.getText().toString().trim(), stat, SingletonSession.Instance().getLivreById(SingletonSession.Instance().getPosModif()).getNom());
                            Toast.makeText(getApplicationContext(), "Le livre a été modifié", Toast.LENGTH_SHORT).show();
                            SingletonSession.Instance().setPosModif(-1);
                            /*intent = new Intent(getApplicationContext(), GestionLivresActivity.class);
                            startActivity(intent);*/
                            finish();
                        }
                    });
                    builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "Modification annulée", Toast.LENGTH_SHORT).show();
                            SingletonSession.Instance().setPosModif(-1);
                            /*intent = new Intent(getApplicationContext(), GestionLivresActivity.class);
                            startActivity(intent);*/
                            finish();
                        }
                    });
                    builder.create().show();
                }
                else
                {

                    btAjouter.setEnabled(false);
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    if (image.getDrawable() != null){
                                        SingletonSession.Instance().upload(fichierPhoto);
                                        photoPath = "img/" + fichierPhoto.getName();
                                    }

                                    SingletonSession.Instance().addLivre(etTitre.getText().toString().trim(), Double.parseDouble(etPrix.getText().toString()), etAuteur.getText().toString().trim(), etCat.getText().toString().trim(), etDesc.getText().toString().trim(), photoPath);
                                    Toast.makeText(getApplicationContext(), "Le livre a été ajouté", Toast.LENGTH_SHORT).show();
                                    /*intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);*/
                                    finish();

                                }

                            }, 1500);

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.login).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.accueil).setVisible(false);
        menu.findItem(R.id.back).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(fichierPhoto.getAbsolutePath());
            image.setImageBitmap(bitmap);
        }


    }
    public void lancerProgramme()
    {
        btPhoto.setVisibility(View.VISIBLE);

        btPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,10);*/
                //dispatchTakePictureIntent();

                lancerCamera();

            }
        });
    }

    public boolean verifierPermissions()
    {
        String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        List<String> listePermissionsADemander = new ArrayList<>();

        for(int i=0; i< permissions.length; i++)
        {
            if(ContextCompat.checkSelfPermission(this,permissions[i]) != PackageManager.PERMISSION_GRANTED)
            {
                listePermissionsADemander.add(permissions[i]);
            }
        }

        if(listePermissionsADemander.isEmpty())
            return true;
        else
        {
            ActivityCompat.requestPermissions(this, listePermissionsADemander.toArray(new String[listePermissionsADemander.size()]),1111 );

            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int nbPermissionsRefusees = 0;

        for(int i = 0; i<grantResults.length; i++ )
        {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                nbPermissionsRefusees++;
            }
        }

        if(nbPermissionsRefusees > 0)
            Toast.makeText(this, "Veuillez accepter les permissions", Toast.LENGTH_LONG).show();
        else
            lancerProgramme();

    }

    private void lancerCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            fichierPhoto = null;
            try {
                fichierPhoto = creationFichierPhoto();
            } catch (IOException ex) {

            }

            if (fichierPhoto != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.annequinpoulain.biblioygg.fileprovider",
                        fichierPhoto);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File creationFichierPhoto() throws IOException {
        String imageFileName = "JPEG_temp";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* dossier */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }
}