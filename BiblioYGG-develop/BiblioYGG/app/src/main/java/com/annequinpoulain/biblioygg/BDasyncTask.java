package com.annequinpoulain.biblioygg;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class BDasyncTask extends Activity {

public class BDaddUser extends AsyncTask<Void, Integer, Integer> {

    private String nom, prenom, email,tel, pass;

    public BDaddUser(String nom, String prenom, String email,String tel, String pass){
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.pass = pass;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Integer doInBackground(Void...voids) {
        try{
            SingletonSession.Instance().addUser(nom, prenom, email,tel, pass);
        }
        catch (Exception e){
            Toast.makeText(SingletonSession.Instance().getContext(), "erreur", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    @Override
    protected void onProgressUpdate(Integer... values)    {
    }

    @Override
    protected void onPostExecute(Integer integer) {
    }
}


    public class Connexion extends AsyncTask<Void, Integer, Integer> {

        private String email, pass;
        private Context context;

        public Connexion(Context context, String email, String pass){
            this.email = email;
            this.pass = pass;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(Void...voids) {
            try{
                SingletonSession.Instance().connexion(email, pass);
            }
            catch (Exception e){
                Toast.makeText(SingletonSession.Instance().getContext(), "erreur", Toast.LENGTH_SHORT).show();
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values)    {
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (SingletonSession.Instance().etatConnexion()) {
                Toast.makeText(context, "Bienvenue John Doe", Toast.LENGTH_LONG).show();
            } else if (email.equals("") || pass.equals("")) {
                Toast.makeText(context, "Veuillez entrer votre adresse email et votre mot de passe", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Veuillez entrer les informations correspondant au compte de john doe", Toast.LENGTH_LONG).show();
            }
        }
    }


    public class UserExists extends AsyncTask<Void, Integer, Integer> {

        private String email;

        public UserExists(String email){
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(Void...voids) {
            try{
                Thread.sleep(1000);
                SingletonSession.Instance().userExists(email);
            }
            catch (Exception e){
                Toast.makeText(SingletonSession.Instance().getContext(), "erreur", Toast.LENGTH_SHORT).show();
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values)    {
        }

        @Override
        protected void onPostExecute(Integer integer) {
        }
    }

}