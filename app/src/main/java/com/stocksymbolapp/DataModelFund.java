package com.stocksymbolapp;

/**
 * Created by BrainPlow on 4/19/2018.
 */

public class DataModelFund {

    String name;
    String type;
    String version_number;
    String feature;

    public DataModelFund(String name, String type, String version_number, String feature ) {
        this.name=name;
        this.type=type;
        this.version_number=version_number;
        this.feature=feature;

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getVersion_number() {
        return version_number;
    }

    public String getFeature() {
        return feature;
    }

}