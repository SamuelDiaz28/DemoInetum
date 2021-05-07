package mx.ine.demo.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mx.ine.demo.R;

public class CommonMethods {


    private static final String TAG = "Presenter-CommonMethods";

    public CommonMethods() {

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public String convertDate(String date, int opc) {
        String respose = null;
        String valid_until = date;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(valid_until);
            Log.i("SRT DATE", strDate.toString());
            cal.setTime(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (opc) {

            case 1:
                String year2 = String.valueOf(cal.getWeekYear());
                respose = "12" + "/" + "31" + "/" + year2;
                break;

            case 2:
                int year = cal.getWeekYear();
                respose = String.valueOf(year);
                break;

            case 3:

                String[] parts = valid_until.split("/");
                if (parts.length == 3)
                    respose = parts[1] + "/" + parts[0] + "/" + parts[2];
                else
                    respose = "Invalid Date";
                break;
        }

        return respose;
    }

    public boolean validityDate(String date) throws ParseException {
        boolean response = true;


        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yy");
        Date strDate = sdf.parse(date);


        if (new Date().after(strDate))
            response = false;
        else if (new Date().before(strDate))
            response = true;

        return response;
    }


    public void showSuccessDialog(String msg, Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_success_dialog,null
        );

        builder.setView(view);
        ((TextView) view.findViewById(R.id.txtMessage)).setText(msg);

        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnAcceptDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void showWarningDialog(String msg, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.layout_warning_dialog,null
        );

        builder.setView(view);
        ((TextView) view.findViewById(R.id.txtMessage)).setText(msg);

        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnAcceptDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void showErrorDialog(String msg, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.layout_error_dialog,null
        );

        builder.setView(view);
        ((TextView) view.findViewById(R.id.txtMessage)).setText(msg);

        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnAcceptDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public String getEncoded64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

}
