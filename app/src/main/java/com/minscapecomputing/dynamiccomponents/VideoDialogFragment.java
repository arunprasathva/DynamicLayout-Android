package com.minscapecomputing.dynamiccomponents;

import java.io.IOException;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static com.minscapecomputing.dynamiccomponents.R.id.videoSurface;

/**
 * Created by Arun on 03-10-2016.
 */
public class VideoDialogFragment extends android.support.v4.app.DialogFragment {

    private FullscreenVideoLayout videoView;
    private boolean isFullScreen = false;

    private int dialogWidth;
    private int dialogHeight;
    private int imageWidth;
    private int imageHeight;
    private String page;
    private Activity activity;
    private Bundle bundle;

    private ImageView close;
    private Uri uri;
    private ImageButton imgfullscreen;
    private View filler;
    private RelativeLayout video_frame;
    private String url;

    public VideoDialogFragment() {
    }

    public static VideoDialogFragment newInstance(Bundle args) {
        VideoDialogFragment fragment = new VideoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(VideoDialogFragment.STYLE_NO_TITLE, R.style.AppDialogTheme);
        activity = getActivity();
        if (getArguments() != null) {
            bundle = getArguments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_dialog_layout, container, false);

        init(rootView);
        setListener();
        if (bundle != null) {
            startPlaying();
        }

        return rootView;
    }

    private void startPlaying() {

        try {
            if (uri != null) {
                /*HttpProxyCacheServer proxy = AisleApplication.getProxy(activity);
                String proxyUrl = proxy.getProxyUrl(url);
                videoView.setVideoURI(Uri.parse(proxyUrl));*/
                videoView.setVideoURI(uri);

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        videoView.start();
                    }
                });

                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        try {
                            dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        try {
                            dismiss();
                            showFailureAlert(getString(R.string.err_video));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            n.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListener() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imgfullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFullScreen();
            }
        });
    }

    private void init(View rootView) {

        close = (ImageView) rootView.findViewById(R.id.close);
        filler = rootView.findViewById(R.id.filler);
        video_frame = (RelativeLayout) rootView.findViewById(R.id.video_container);
        videoView = (FullscreenVideoLayout) rootView.findViewById(videoSurface);
        videoView.setKeepScreenOn(true);
        videoView.setActivity(activity);

        imgfullscreen = (ImageButton) rootView.findViewById(R.id.vcv_img_fullscreen);

        if (bundle.containsKey("VideoUrl")) {
            url = bundle.getString("VideoUrl");
            uri = Uri.parse(url);
        }

        if (bundle.containsKey("page")) {
            page = bundle.getString("page");
        }

        if (bundle.containsKey("height")) {
            imageHeight = bundle.getInt("height");
        }

        if (bundle.containsKey("width")) {
            imageWidth = bundle.getInt("width");
        }
    }

    private void toggleFullScreen() {

        if (!isFullScreen) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            isFullScreen = true;
            filler.setVisibility(View.GONE);
        } else {
            Window window = getDialog().getWindow();
            if (page.equalsIgnoreCase("home")) {
                window.setLayout(dialogWidth, dialogHeight);
                window.setGravity(Gravity.CENTER);
            } else if (page.equalsIgnoreCase("detail")) {
                window.setLayout(imageWidth, imageHeight);
                window.setGravity(Gravity.TOP | Gravity.LEFT);
            }

            isFullScreen = false;
            filler.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        if (page.equalsIgnoreCase("home")) {
            setLayoutHomePage();
        } else if (page.equalsIgnoreCase("detail")) {
            setLayoutDetailPage();
        }
    }

    private void setLayoutHomePage() {

        // safety check
        if (getDialog().getWindow() == null)
            return;

        try {
            LoginParam loginParam = new LoginParam();
            int width = loginParam.getScreenWidth(activity);
            int height = loginParam.getScreenHeight(activity);
            dialogWidth = (width / 4) * 3;
            dialogHeight = (height / 4) * 3;

            Window window = getDialog().getWindow();
            window.setLayout(dialogWidth, dialogHeight);
            window.setGravity(Gravity.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  private void setLayoutHomePageBeacon() {

        // safety check
        if (getDialog().getWindow() == null)
            return;

        try {
            LoginParam loginParam = new LoginParam();
            int width = loginParam.getScreenWidth(activity);
            int height = loginParam.getScreenHeight(activity);
            *//*dialogWidth = (width / 7) * 3;
            dialogHeight = (height/4) * 3;*//*
            dialogWidth = 480;
            dialogHeight = 700;

            Window window = getDialog().getWindow();
            //          window.setLayout(dialogWidth, dialogHeight);
            window.setGravity(Gravity.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void setLayoutDetailPage() {

        // safety check
        if (getDialog().getWindow() == null)
            return;

        try {
            Window window = getDialog().getWindow();
            window.setLayout(imageWidth, imageHeight);
            window.setGravity(Gravity.TOP | Gravity.LEFT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFailureAlert(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        //alertDialog.setTitle(getString(R.string.alert_title));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}