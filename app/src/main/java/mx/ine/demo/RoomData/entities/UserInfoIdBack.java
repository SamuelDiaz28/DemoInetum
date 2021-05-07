package mx.ine.demo.RoomData.entities;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "userInfoIdBack")
public class UserInfoIdBack {
    @PrimaryKey
    private int id;

    private String MRZ;
    private String CIC;
    private String OCR;

    private Boolean verification_mrz;

    private String img_document;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMRZ() {
        return MRZ;
    }

    public void setMRZ(String MRZ) {
        this.MRZ = MRZ;
    }

    public String getCIC() {
        return CIC;
    }

    public void setCIC(String CIC) {
        this.CIC = CIC;
    }

    public String getOCR() {
        return OCR;
    }

    public void setOCR(String OCR) {
        this.OCR = OCR;
    }

    public Boolean getVerification_mrz() {
        return verification_mrz;
    }

    public void setVerification_mrz(Boolean verification_mrz) {
        this.verification_mrz = verification_mrz;
    }

    public String getImg_document() {
        return img_document;
    }

    public void setImg_document(String img_document) {
        this.img_document = img_document;
    }
}
