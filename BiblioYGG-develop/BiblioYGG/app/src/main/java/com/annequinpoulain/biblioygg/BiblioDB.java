package com.annequinpoulain.biblioygg;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Livre.class, User.class}, version = 1)
public abstract class BiblioDB extends RoomDatabase {

    public abstract BiblioDAO pdao();
}
