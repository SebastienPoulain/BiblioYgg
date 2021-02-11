package com.annequinpoulain.biblioygg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SingletonSession {

    private static SingletonSession instance;
    private boolean activateRecyclerButtons = false;
    private int posModif = -1;
    private User user = new User("", "", "", "","");

    private boolean loginDialogConnexion = true;
    private Context context;

    private Retrofit.Builder builder = new Retrofit.Builder();

    private InterfaceServeur server = RetrofitInstance.getInstance().create(InterfaceServeur.class);

    private BiblioDB pdb;
    private BiblioDAO pdao;


    private SingletonSession() {
    }

    public static SingletonSession Instance() {
        if (instance == null) {
            instance = new SingletonSession();
        }
        return instance;
    }

    public void initContext(Context context){
        this.context = context.getApplicationContext();
    }

    public Context getContext(){
        return context;
    }

    public List<Livre> getLivres() {
        return pdao.getLivres();
    }

    public List<Livre> getLivresDispo() {
        return pdao.getLivresDispo('D');
    }

    public Livre getLivreById(int id) {
        return pdao.getLivreById(id);
    }

    public List<Livre> getLivresByUserEmail(String email) {
        return pdao.getLivresByUserEmail(email.trim());
    }

    public void addLivre(String titreLivre, double prixLivre, String auteur, String cat, String desc, String imgpath) {
        Livre livre = new Livre(titreLivre.trim(), prixLivre, auteur.trim(), cat.trim(), desc.trim(), 'D', imgpath);
        pdao.addLivre(livre);

        Call<ResponseBody> call = server.livreDATA("add", titreLivre, prixLivre, auteur, cat, desc, 'D', imgpath, user.getEmail(), "");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void rmLivre(String title){
        pdao.delLivreByTitleEmail(title, user.getEmail());

        Call<ResponseBody> call = server.livreDATA("delete", "", 0, "", "", "", 'D', "", user.getEmail(), title);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void modLivre(int id, String nom, double prix, String auteur, String cat, String desc, char stat, String title){
        pdao.updateLivre(id, nom.trim(), prix, auteur.trim(), cat.trim(), desc.trim(), stat);

        Call<ResponseBody> call = server.livreDATA("update", nom, prix, auteur, cat, desc, stat, "", user.getEmail(), title);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void delAllRooms(){
        pdao.delAllLivres();
        pdao.delAllUsers();
    }

    public void serverToRoom(){
        Call<List<Utilisateur>> callU = server.getUsers();

        callU.enqueue(new Callback<List<Utilisateur>>() {
            @Override
            public void onResponse(Call<List<Utilisateur>> call, Response<List<Utilisateur>> response) {
                List<Utilisateur> apiResponse = response.body();

                for (int i = 0; i < apiResponse.size(); i++){
                    User temp = new User(apiResponse.get(i).getNom(), apiResponse.get(i).getPrenom(), apiResponse.get(i).getEmail(), apiResponse.get(i).getTel(), apiResponse.get(i).getPassword());
                    temp.setPassNoHash(apiResponse.get(i).getPassword());

                    pdao.addUser(temp);
                }

            }

            @Override
            public void onFailure(Call<List<Utilisateur>> call, Throwable t) {

            }
        });


        Call<List<LivreRetrofit>> call = server.getLivres("livres", "", "");

        call.enqueue(new Callback<List<LivreRetrofit>>() {
            @Override
            public void onResponse(Call<List<LivreRetrofit>> call, Response<List<LivreRetrofit>> response) {
                List<LivreRetrofit> apiResponse = response.body();

                for (int i = 0; i < apiResponse.size(); i++){
                    Livre temp = new Livre(apiResponse.get(i).getNom(), apiResponse.get(i).getPrix(), apiResponse.get(i).getAuteur(), apiResponse.get(i).getCat(), apiResponse.get(i).getDescription(), apiResponse.get(i).getStat(), apiResponse.get(i).getImgpath());
                    temp.setEmail(apiResponse.get(i).getEmail());

                    //Toast.makeText(context, getUserByEmail(temp.getEmail()).getPrenom(), Toast.LENGTH_LONG).show();



                    pdao.addLivre(temp);
                }
            }

            @Override
            public void onFailure(Call<List<LivreRetrofit>> call, Throwable t) {

            }
        });

    }

    public boolean etatConnexion(){
        if (user.getId() == 0)
            return false;
        else
            return true;
    }

    public void connexion(String email, String pass){
        if (pdao.getUserLogin(email.trim(), getSha256Hash(pass)).isEmpty())
            user = new User("", "", "","","");
        else
            user = pdao.getUserLogin(email.trim(), getSha256Hash(pass)).get(0);
    }

    public void deconnexion(){
        user.setId(0);
        user.setNom("");
        user.setPrenom("");
        user.setEmail("");
        user.setTel("");
        user.setPassword("");
    }

    public int getPosModif(){
        return posModif;
    }

    public void setPosModif(int pos){
        this.posModif = pos;
    }


    public boolean getLoginDialogConnexion() {
        return loginDialogConnexion;
    }

    public void setLoginDialogConnexion(boolean loginDialogConnexion) {
        this.loginDialogConnexion = loginDialogConnexion;
    }

    public boolean getActivateRecyclerButton(){
        return activateRecyclerButtons;
    }

    public void setActivateRecyclerButtons(boolean activate){
        activateRecyclerButtons = activate;
    }

    public static String getSha256Hash(String password) {
        try {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            digest.reset();
            return bin2hex(digest.digest(password.getBytes()));
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }

    public void initPdao(Context context){
        initContext(context);

        pdb = Room.databaseBuilder(context, BiblioDB.class, "BiblioDB").allowMainThreadQueries().build();
        pdao = pdb.pdao();
    }

    public void addUser(String nom, String prenom, String email,String tel, String passwd){
        User temp = new User(nom.trim(), prenom.trim(), email.trim(),tel.trim(), passwd);
        pdao.addUser(temp);

        Call<ResponseBody> call = server.userDATA(email.trim(), getSha256Hash(passwd), nom.trim(), prenom.trim(), tel.trim());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public User getUser(){
        return user;
    }

    public User getUserByEmail(String email){
        return pdao.getUserByEmail(email.trim());
    }

    public User getUserById(int id){
        //return pdao.getUserById(id);


            return pdao.getUserById(id);

/*
        Call<Utilisateur> call = server.getUserbyid(id);

        try
        {
            Response<Utilisateur> response = call.execute();
            Utilisateur apiResponse = response.body();

            return apiResponse;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;*/
    }

    public boolean userExists(String email){
        if (pdao.userExists(email.trim()) > 0)
            return true;
        return false;

        /*
        Call<String> call = server.userExists(email.trim());

        try
        {
            Response<String> response = call.execute();
            String apiResponse = response.body();

            return Boolean.parseBoolean(apiResponse);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return true;
*/
    }

    public boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public boolean isValidTel(String tel)
    {
        String emailRegex = "\\(([2-9][0-8][0-9])\\)\\s([2-9][0-9]{2})\\W([0-9]{4})";

        Pattern pat = Pattern.compile(emailRegex);
        if (tel == null)
            return false;
        return pat.matcher(tel).matches();
    }

    public boolean isInternetConnection(){

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }

    public void upload(File file)
    {
        RequestBody requete = RequestBody.create(MediaType.parse("text/plain"), "upload");

        MediaType mediaType = MediaType.parse("image/*");
        RequestBody fichier_requete =RequestBody.create(mediaType,file);

        MultipartBody.Part part_fichier = MultipartBody.Part.createFormData("photo",
                file.getName(),
                fichier_requete);

        //InterfaceServeur serveur = RetrofitInstance.getInstance().create((InterfaceServeur.class));
        Call<ResponseBody> call = server.upload(requete, part_fichier);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }


}


