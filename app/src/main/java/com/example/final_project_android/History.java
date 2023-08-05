package com.example.final_project_android;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class History {

    @PrimaryKey(autoGenerate = true) //increment the ids for us
    @ColumnInfo(name="id")
    public long id;


    @ColumnInfo(name="OriginalCurrency")
    String OriginalCurrency;


    @ColumnInfo(name="ConvertedCurrency")
    String ConvertedCurrency;

    @ColumnInfo(name="OriginalNumber")
    String OriginalNumber;

    @ColumnInfo(name="ConvertedNumber")
    String ConvertedNumber;

    public History( String on,
                    String oc,
                    String cn,
                    String cc
                        ){

        OriginalCurrency = oc;
        ConvertedCurrency = cc;
        ConvertedNumber=cn;
        OriginalNumber=on;

    }

    public History()//for database queries
    {}
}
