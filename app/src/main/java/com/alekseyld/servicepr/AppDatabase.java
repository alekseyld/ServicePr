package com.alekseyld.servicepr;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Alekseyld on 30.06.2016.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase";

    public static final int VERSION = 1;
}