package com.example.final_project_android;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Represents a historical currency conversion entry.
 */
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

    /**
     * Constructs a new History object with provided currency conversion details.
     *
     * @param on Original number value.
     * @param oc Original currency code.
     * @param cn Converted number value.
     * @param cc Converted currency code.
     */
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

    /**
     * Default constructor for database queries.
     */
    public History()//for database queries
    {}
}
