package bldg5.jj.pgnbase.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player {
    @SerializedName("_id")
    @Expose
    private int _id;
    @SerializedName("Name")
    @Expose
    private String name;

    public int getId() {
        return _id;
    }
    public void setId(int id) {
        _id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String strName) {
        name = strName;
    }
}
