package com.weedeo.user.Utils;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.weedeo.user.R;
import com.weedeo.user.sharedpreference.SharedPreferenceData;
import com.weedeo.user.ui.NavigationDrawer.NavigationDrawerActivity;
import com.weedeo.user.ui.login.LoginActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static androidx.core.content.FileProvider.getUriForFile;
import static com.weedeo.user.Utils.Constants.KEY_ACCESS_TOKEN;
import static com.weedeo.user.Utils.Constants.KEY_USER_ID;

/**
 * Created By Athul on 12-10-2019.
 */
public class AppUtils {

    public static Dialog showProgressDialog(Context context){
        Dialog m_Dialog = new Dialog(context);
        m_Dialog.setContentView(R.layout.custom_progress_view);
        m_Dialog.setCancelable(false);
        m_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return m_Dialog;
    }

    public static boolean isUserLoggedIn(Context context){
        SharedPreferenceData preferenceData = new SharedPreferenceData(context);
        String userIf = preferenceData.getString(KEY_USER_ID);
        String token = preferenceData.getString(KEY_ACCESS_TOKEN);
        if (userIf.length()>0 && token.length()>0) {
            //user logged in
            return true;
        } else {
            //user not logged in
            return false;
        }
    }

    public static boolean rename(File from, File to) {
        return from.getParentFile().exists() && from.exists() && from.renameTo(to);
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public static int getMonthIneger(String month) {
        SimpleDateFormat parser = new SimpleDateFormat("MMM", Locale.getDefault());
        Date date;
        try {
            date = parser.parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.MONTH);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return 0;
    }


    public static File moveFile(File file, File dir) throws IOException {
        File newFile = new File(dir, file.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            file.delete();
            return newFile;
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }

    }


    public static Uri getCacheImagePath(Context mContext,String fileName) {
        File path = new File(mContext.getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(mContext, mContext.getPackageName() + ".provider", image);
    }

    public static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public static long reduceDateByYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -year);
        return cal.getTimeInMillis();
    }

    public static String getDeviceDensity(Context context){
        String deviceDensity = "";
        switch (context.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                deviceDensity =  0.75 + " ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                deviceDensity =  1.0 + " mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                deviceDensity =  1.5 + " hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                deviceDensity =  2.0 + " xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                deviceDensity =  3.0 + " xxhdpi";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                deviceDensity =  4.0 + " xxxhdpi";
                break;
            default:
                deviceDensity = "Not found";
        }
        return deviceDensity;
    }

    public static boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString().trim();
        return TextUtils.isEmpty(str);
    }


    /**
     * Method for validating the email pattern
     *
     * @param email Email address
     * @return True-valid address else return false.
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}
