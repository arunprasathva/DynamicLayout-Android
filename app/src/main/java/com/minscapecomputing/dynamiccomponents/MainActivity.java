package com.minscapecomputing.dynamiccomponents;

import com.viewpagerindicator.LinePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    private ViewPager viewPager;
    private ImageView imgRight;
    private ImageView imgLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        try {

            JSONArray jsonArray = new JSONArray(readFile("sample.json", this));
            setPagerAdapter(jsonArray);
        } catch (JSONException je) {
            je.printStackTrace();
        }

//        getJSON("https://raw.githubusercontent.com/arunprasathva/DynamicLayout-Android/master/app/src/main/assets/sample.json");
    }

    private void getJSON(String URL) {

        OkHttpRequest okHttpRequest = new OkHttpRequest();
        okHttpRequest.setOnResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    if (jsonArray != null) {

                        setPagerAdapter(jsonArray);
                        /*final DownloadTask downloadTask = new DownloadTask(MainActivity.this, jsonArray);
                        downloadTask.execute();

                        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true);
                            }
                        });*/
                    } else {
                        Log.e("Json2View", "Could not load valid json file");
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Request request = new Request.Builder()
                .url(URL)
                .build();

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Downloading JSON");

        okHttpRequest.httpPost(MainActivity.this, request, progressDialog, true);
    }

    private void init() {

        viewPager = (ViewPager) findViewById(R.id.pager);
        imgLeft = (ImageView) findViewById(R.id.imgLeftArrow);
        imgRight = (ImageView) findViewById(R.id.imgRightArrow);

        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Downloading File");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
    }

    private void setPagerAdapter(final JSONArray jsonArray) {

        WelcomeSlidePagerAdapter adapter = new WelcomeSlidePagerAdapter(getSupportFragmentManager(), jsonArray);
        viewPager.setAdapter(adapter);

        if (jsonArray.length() > 1)
            imgRight.setVisibility(View.VISIBLE);

        LinePageIndicator indicator = (LinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setSelectedColor(Color.BLACK);
        indicator.setUnselectedColor(Color.GRAY);
        indicator.setStrokeWidth(4 * density);
        indicator.setLineWidth(20 * density);
        indicator.setMinimumHeight((int) (8 * density));
        indicator.setCentered(false);

        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                imgLeft.setVisibility(View.VISIBLE);
                imgRight.setVisibility(View.VISIBLE);
                if (position == 0) {
                    imgLeft.setVisibility(View.GONE);
                } else if (position == jsonArray.length() - 1) {
                    imgRight.setVisibility(View.GONE);
                }
                Log.e("TAG", "position-" + position + "/" + (jsonArray.length() - 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * Helper function to load file from assets
     */
    private String readFile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets().open(fileName);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null) isr.close();
                if (fIn != null) fIn.close();
                if (input != null) input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    /**
     * Holder class that keep UI Component from the Dynamic View
     */


    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        private JSONArray jsonArray;
        private String pathToStore;

        public DownloadTask(Context context, JSONArray jsonArray) {
            this.context = context;
            this.jsonArray = jsonArray;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null) mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
            setPagerAdapter(jsonArray);
        }

        @Override
        protected String doInBackground(String... sUrls) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                String rawURL = "http://www.webpagepublicity.com/free-fonts/x/Xanadu.ttf";
                URL url = new URL(rawURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    String responseErrorCode = "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                    Log.e("TAG", "res - " + responseErrorCode);
                    return responseErrorCode;
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                String disposition = connection.getHeaderField("Content-Disposition");

                String fileName = "";

                if (disposition != null) {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10,
                                disposition.length() - 1);
                    }
                } else {
                    // extracts file name from URL
                    fileName = rawURL.substring(rawURL.lastIndexOf("/") + 1,
                            rawURL.length());
                }

                // download the file
                input = connection.getInputStream();
                pathToStore = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;
                output = new FileOutputStream(pathToStore);
                Log.e("TAG", "res - " + pathToStore);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setProgress(values[0]);
        }
    }
}
