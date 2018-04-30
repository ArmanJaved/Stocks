package com.stocksymbolapp;

import android.os.Bundle;

/**
 * Created by BrainPlow on 8/25/2017.
 */

public class Watch {

    public String restId;
    public String restname;
    public String restamount;
    public String date;

    public Watch()
    {

    }

    public String getRestId() {
        return restId;
    }

    public String getRestname() {
        return restname;
    }

    public String getRestamount() {
        return restamount;
    }

    public String getDate() {
        return date;
    }

    public Watch(String restId, String restname, String restamount, String date) {
        this.date = date;
        this.restId = restId;
        this.restname = restname;
        this.restamount = restamount;
    }


}
