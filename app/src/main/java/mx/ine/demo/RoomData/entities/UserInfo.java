package mx.ine.demo.RoomData.entities;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "userInfo")
public class UserInfo {
    @PrimaryKey
    private int id;
    private String surname_and_give_names;
    private String birth_day;
    private String address;
    private String validity_date;
    private String voter_key;
    private String curp;
    private String issue_year;
    private String registry_year;

    private String img_document;


    private boolean validity_document;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname_and_give_names() {
        return surname_and_give_names;
    }

    public void setSurname_and_give_names(String surname_and_give_names) {
        this.surname_and_give_names = surname_and_give_names;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getValidity_date() {
        return validity_date;
    }

    public void setValidity_date(String validity_date) {
        this.validity_date = validity_date;
    }

    public String getVoter_key() {
        return voter_key;
    }

    public void setVoter_key(String voter_key) {
        this.voter_key = voter_key;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getIssue_year() {
        return issue_year;
    }

    public void setIssue_year(String issue_year) {
        this.issue_year = issue_year;
    }

    public String getRegistry_year() {
        return registry_year;
    }

    public void setRegistry_year(String registry_year) {
        this.registry_year = registry_year;
    }

    public boolean isValidity_document() {
        return validity_document;
    }

    public void setValidity_document(boolean validity_document) {
        this.validity_document = validity_document;
    }

    public String getImg_document() {
        return img_document;
    }

    public void setImg_document(String img_document) {
        this.img_document = img_document;
    }
}
