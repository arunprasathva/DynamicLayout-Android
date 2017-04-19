package com.minscapecomputing.dynamiccomponents;

import com.viewpagerindicator.LinePageIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    private ViewPager viewPager;
    private ViewPager verticalPager;
    private ImageView imgRight;
    private ImageView imgLeft;
    private ImageView imgTopArrow;
    private ImageView imgBottomArrow;

    private boolean isHorizontal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        /*try {
            JSONObject jsonObject = new JSONObject(readFile("lambo.json", this));
            JSONArray jsonArray = jsonObject.optJSONArray("children");
            if (jsonArray != null) {
                isHorizontal = !jsonObject.optString("pager_orientation").equalsIgnoreCase("vertical");
                if (isHorizontal) {
                    verticalPager.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    setPagerAdapter(jsonArray, viewPager);
                    findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                } else {
                    verticalPager.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.GONE);
                    setPagerAdapter(jsonArray, verticalPager);
                    findViewById(R.id.indicator).setVisibility(View.GONE);
                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }*/

        getJSON("https://raw.githubusercontent.com/arunprasathva/DynamicLayout-Android/master/app/src/main/assets/lambo.json");
    }

    private void getJSON(String URL) {

        OkHttpRequest okHttpRequest = new OkHttpRequest();
        okHttpRequest.setOnResponseListener(new OnResponseListener() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.optJSONArray("children");
                    if (jsonArray != null) {
                        isHorizontal = !jsonObject.optString("pager_orientation").equalsIgnoreCase("vertical");
                        if (isHorizontal) {
                            verticalPager.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            setPagerAdapter(jsonArray, viewPager);
                            findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                        } else {
                            verticalPager.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            setPagerAdapter(jsonArray, verticalPager);
                            findViewById(R.id.indicator).setVisibility(View.GONE);
                        }
                    }
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
        verticalPager = (ViewPager) findViewById(R.id.verticalPager);
        imgLeft = (ImageView) findViewById(R.id.imgLeftArrow);
        imgRight = (ImageView) findViewById(R.id.imgRightArrow);
        imgTopArrow = (ImageView) findViewById(R.id.imgTopArrow);
        imgBottomArrow = (ImageView) findViewById(R.id.imgBottomArrow);

        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Downloading File");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
    }

    private void setPagerAdapter(final JSONArray jsonArray, final ViewPager pager) {

        WelcomeSlidePagerAdapter adapter = new WelcomeSlidePagerAdapter(getSupportFragmentManager(), jsonArray);
        pager.setAdapter(adapter);

        (isHorizontal ? imgBottomArrow : imgRight).setVisibility(View.GONE);
        if (jsonArray.length() > 1) {
            (isHorizontal ? imgRight : imgBottomArrow).setVisibility(View.VISIBLE);
        }

        LinePageIndicator indicator = (LinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
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
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });

        imgBottomArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });

        imgTopArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                (isHorizontal ? imgLeft : imgTopArrow).setVisibility(View.VISIBLE);
                (isHorizontal ? imgRight : imgBottomArrow).setVisibility(View.VISIBLE);
                if (position == 0) {
                    (isHorizontal ? imgLeft : imgTopArrow).setVisibility(View.GONE);
                } else if (position == jsonArray.length() - 1) {
                    (isHorizontal ? imgRight : imgBottomArrow).setVisibility(View.GONE);
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
            setPagerAdapter(jsonArray, viewPager);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switchLayout: {
                getJSON("https://raw.githubusercontent.com/arunprasathva/DynamicLayout-Android/master/app/src/main/assets/sample_new.json");
                break;
            }
        }
        return false;
    }
}
