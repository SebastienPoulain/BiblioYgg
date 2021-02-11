package com.annequinpoulain.biblioygg;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BiblioDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long addLivre(Livre livre);

    @Query("UPDATE tbl_livres SET nom = :titre, prix = :prix, auteur = :auteur, cat = :cat, description = :desc, stat = :stat WHERE id = :id")
    public void updateLivre(int id, String titre, double prix, String auteur, String cat, String desc, char stat);

    @Query("DELETE FROM tbl_livres WHERE id = :id")
    public void delLivre(int id);

    @Query("DELETE FROM tbl_livres WHERE nom = :nom AND email = :email")
    public void delLivreByTitleEmail(String nom, String email);

    @Query("SELECT * FROM tbl_livres")
    public List<Livre> getLivres();

    @Query("SELECT * FROM tbl_livres WHERE id = :id")
    public Livre getLivreById(int id);

    @Query("SELECT * FROM tbl_livres WHERE email = lower(:email)")
    public List<Livre> getLivresByUserEmail(String email);

    @Query("SELECT * FROM tbl_livres WHERE stat = :stat")
    public List<Livre> getLivresDispo(char stat);

    @Query("DELETE FROM tbl_livres")
    public void delAllLivres();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long addUser(User user);

    @Update
    public void updateUser(User user);

    @Delete
    public void delUser(User user);

    @Query("SELECT * FROM users WHERE email = lower(:email) AND password = :passwd")
    public List<User> getUserLogin(String email, String passwd);

    @Query("SELECT COUNT(id) FROM users WHERE email = lower(:email)")
    public int userExists(String email);

    @Query("SELECT * FROM users WHERE email = lower(:email)")
    public User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :id")
    public User getUserById(int id);

    @Query("DELETE FROM users")
    public void delAllUsers();

}
