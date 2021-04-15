package mx.ine.demo.Util;

public class SpinnerCountriesModel {

    private String txtCountry;

    private  Integer imgCountry;

    public SpinnerCountriesModel(){}


    public SpinnerCountriesModel(String txtCountry, Integer imgCountry){
        this.txtCountry = txtCountry;
        this.imgCountry = imgCountry;
    }

    public String getTxtCountry() {
        return txtCountry;
    }

    public Integer getImgCountry() {
        return imgCountry;
    }
}
