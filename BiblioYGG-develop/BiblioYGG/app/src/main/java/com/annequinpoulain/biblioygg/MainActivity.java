package com.annequinpoulain.biblioygg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.Objects;
import com.google.android.material.navigation.NavigationView;


import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements loginDialog.LoginDialogListener, NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private AdapterListe adapter;
    private DrawerLayout drawer;
    loginDialog loginDialog;

    List<Livre> listeL;
    EditText etRecherche;
    NavigationView navigationView;
    Menu nav_Menu;
    View headerView;
    TextView tvUsername, tvEmail;
    androidx.appcompat.widget.Toolbar toolbar;

    InterfaceServeur server = RetrofitInstance.getInstance().create(InterfaceServeur.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Accueil - BiblioYGG");
        setSupportActionBar(toolbar);
        etRecherche = findViewById(R.id.recherche);
        SingletonSession.Instance().initPdao(this);

        if (SingletonSession.Instance().isInternetConnection()) {
            SingletonSession.Instance().delAllRooms();
            SingletonSession.Instance().serverToRoom();
        }

        navigationView = findViewById(R.id.nav_view);
        nav_Menu = navigationView.getMenu();
        headerView = navigationView.getHeaderView(0);

        nav_Menu.findItem(R.id.nav_logout).setVisible(false);
        nav_Menu.findItem(R.id.nav_gestion_livres).setVisible(false);

        tvUsername = headerView.findViewById(R.id.tvUsername);
        tvEmail = headerView.findViewById(R.id.tvEmail);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //invalidateOptionsMenu();

        adapter = new AdapterListe(SingletonSession.Instance().getLivres());

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        recyclerView = findViewById(R.id.livreListe);

                        listeL = SingletonSession.Instance().getLivresDispo();

                        if (!SingletonSession.Instance().getLivresDispo().isEmpty()) {
                            adapter = new AdapterListe(listeL);
                            recyclerView.setAdapter(adapter);
                        }


                        SingletonSession.Instance().setActivateRecyclerButtons(false);


                        recyclerView.setHasFixedSize(true);

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                    }
                },
                1000);



        //Toast.makeText(this, String.valueOf(SingletonSession.Instance().getUserId()), Toast.LENGTH_SHORT).show();

        etRecherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                rechercher(s.toString());
            }

		});

    }

    private void rechercher(String text){
        ArrayList <Livre> listeNom = new ArrayList<>();

        for (Livre item : SingletonSession.Instance().getLivres()){
            if(item.getNom().toLowerCase().contains(text.toLowerCase())){
                listeNom.add(item);
            }
        }
        adapter.filtrerListe(listeNom);
    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        SingletonSession.Instance().setActivateRecyclerButtons(false);

        if (SingletonSession.Instance().etatConnexion())
        {
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
            nav_Menu.findItem(R.id.nav_gestion_livres).setVisible(true);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);

            tvUsername.setText(SingletonSession.Instance().getUser().getPrenom() + " " +SingletonSession.Instance().getUser().getNom());
            tvEmail.setText(SingletonSession.Instance().getUser().getEmail());
        }

        super.onResume();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.back).setVisible(false);

        if (SingletonSession.Instance().etatConnexion()) {
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.logout).setVisible(true);
            menu.findItem(R.id.gestionLivre).setVisible(true);
        }
        else
        {
            menu.findItem(R.id.login).setVisible(true);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.gestionLivre).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.login:
                openDialog();
                break;

            case R.id.logout:
                SingletonSession.Instance().deconnexion();
                invalidateOptionsMenu();
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.gestionLivre:
                intent = new Intent(this, GestionLivresActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case R.id.nav_accueil:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment()).commit();
                SingletonSession.Instance().setActivateRecyclerButtons(false);
                recyclerView.setVisibility(VISIBLE);
                adapter = new AdapterListe(SingletonSession.Instance().getLivresDispo());
                recyclerView.setAdapter(adapter);
                toolbar.setTitle("Accueil - BiblioYGG");

                break;

            case R.id.nav_location:

                recyclerView.setVisibility(GONE);
                LocationFragment fragment = new LocationFragment();
                SingletonSession.Instance().setActivateRecyclerButtons(true);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                toolbar.setTitle("Localisation - BiblioYGG");

                break;

            case R.id.nav_gestion_livres:
                recyclerView.setVisibility(GONE);
                GestionLivresFragment frag = new GestionLivresFragment();

                SingletonSession.Instance().setActivateRecyclerButtons(true);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();

                toolbar.setTitle("Gestion des livres - BiblioYGG");

                break;

            case R.id.nav_login:
                openDialog();
                break;

            case R.id.nav_logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment()).commit();
                recyclerView.setVisibility(VISIBLE);

                SingletonSession.Instance().deconnexion();

                Toast.makeText(getApplicationContext(), "Déconnexion réussie", Toast.LENGTH_LONG);

                tvUsername.setText("Bienvenue");
                tvEmail.setText("Veuillez vous connecter");

                nav_Menu.findItem(R.id.nav_login).setVisible(true);
                nav_Menu.findItem(R.id.nav_gestion_livres).setVisible(false);
                nav_Menu.findItem(R.id.nav_logout).setVisible(false);

                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11)
        {
            adapter.onActivityResult(requestCode,1);
        }


    }


    public void openDialog(){
        loginDialog = new loginDialog();
        loginDialog.show(getSupportFragmentManager(), "Login Dialog");
    }

    @Override
    public void login(String nom, String prenom, String email, String tel, String password, String password2) {

        if (SingletonSession.Instance().getLoginDialogConnexion()) {

            SingletonSession.Instance().connexion(email, password);

            if (SingletonSession.Instance().etatConnexion()) {
                Toast.makeText(getApplicationContext(), "Bienvenue "+ SingletonSession.Instance().getUser().getPrenom() + " " + SingletonSession.Instance().getUser().getNom(), Toast.LENGTH_LONG).show();

                nav_Menu.findItem(R.id.nav_login).setVisible(false);
                nav_Menu.findItem(R.id.nav_gestion_livres).setVisible(true);
                nav_Menu.findItem(R.id.nav_logout).setVisible(true);

                tvUsername.setText(SingletonSession.Instance().getUser().getPrenom() + " " +SingletonSession.Instance().getUser().getNom());
                tvEmail.setText(SingletonSession.Instance().getUser().getEmail());

            } else if (email.trim().equals("") || password.equals("")) {
                Toast.makeText(getApplicationContext(), "Veuillez entrer votre adresse email et votre mot de passe", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Email ou mot de passe invalides", Toast.LENGTH_LONG).show();
            }
            //invalidateOptionsMenu();
        }
        else
        {
            if (nom.trim().equals("") || prenom.trim().equals("") || email.trim().equals("") || tel.equals("") || password.equals("")) {
                Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
            }
            else if (!SingletonSession.Instance().isValidEmail(email.trim()))
            {
                Toast.makeText(getApplicationContext(), "Le format de l'adresse email entrée est incorrecte", Toast.LENGTH_LONG).show();
            }
            else if (!SingletonSession.Instance().isValidTel(tel.trim())){
                Toast.makeText(getApplicationContext(), "Le format du numéro de téléphone entré est incorrect", Toast.LENGTH_LONG).show();
            }
            else if (SingletonSession.Instance().userExists(email)){
                Toast.makeText(getApplicationContext(), "Adresse Email déjà utilisée, veuillez en utilisez une autre", Toast.LENGTH_LONG).show();
            }
            else if (!password.equals(password2)){
                Toast.makeText(getApplicationContext(), "Les deux mots de passe doivent concorder", Toast.LENGTH_LONG).show();
            }
            else if (password.equals(password2)){
                SingletonSession.Instance().addUser(nom, prenom, email,tel, password);
                Toast.makeText(getApplicationContext(), "Création de compte réussie, vous pouvez maintenant vous connecter", Toast.LENGTH_LONG).show();
            }
        }
    }
}
