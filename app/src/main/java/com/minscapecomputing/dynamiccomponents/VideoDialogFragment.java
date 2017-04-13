package com.minscapecomputing.dynamiccomponents;

import org.json.JSONException;

import java.io.IOException;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
    private String imageAlign;

    public VideoDialogFragment() { }

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

        try {
            init(rootView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private void init(View rootView) throws JSONException {

        close = (ImageView) rootView.findViewById(R.id.close);
        filler = rootView.findViewById(R.id.filler);
        video_frame = (RelativeLayout) rootView.findViewById(R.id.video_container);
        videoView = (FullscreenVideoLayout) rootView.findViewById(videoSurface);
        videoView.setKeepScreenOn(true);
        videoView.setActivity(activity);

        imgfullscreen = (ImageButton) rootView.findViewById(R.id.vcv_img_fullscreen);

        if (bundle != null) {
            if (bundle.containsKey("VideoUrl")) {
                uri = Uri.parse(bundle.getString("VideoUrl"));
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

            if (bundle.containsKey("align")) {
                imageAlign = bundle.getString("align");
            }
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
            } else if (page.equalsIgnoreCase("custom") && imageWidth > 0) {
                window.setLayout(imageWidth, imageHeight);
                setCustomAlignment(window);
            } else {
                window.setLayout(dialogWidth, dialogHeight);
                window.setGravity(Gravity.CENTER);
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
        } else if (page.equalsIgnoreCase("custom") && imageWidth > 0) {
            setLayoutCustomPage();
        } else {
            setLayoutHomePage();
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

    private void setLayoutCustomPage() {

        // safety check
        if (getDialog().getWindow() == null)
            return;

        try {
            Window window = getDialog().getWindow();
            window.setLayout(imageWidth, imageHeight);

            setCustomAlignment(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCustomAlignment(Window window) {

        if (getAlignment() != null) {
            int[] gravities = getAlignment();
            if (gravities.length == 1) {
                window.setGravity(gravities[0]);
            } else if (gravities.length == 2) {
                window.setGravity(gravities[0] | gravities[1]);
            } else {
                window.setGravity(Gravity.CENTER);
            }
        } else {
            window.setGravity(Gravity.CENTER);
        }
    }

    private int[] getAlignment() {

        if (TextUtils.isEmpty(imageAlign)) return new int[]{Gravity.CENTER};
        if (imageAlign != null) {
            if (imageAlign.contains("|")) {
                String[] split = imageAlign.split("\\|");
                if (split.length > 0) {
                    int[] alignmentValue = new int[split.length];
                    for (int i = 0; i < split.length; i++) {
                        alignmentValue[i] = Integer.parseInt(split[i]);
                    }
                    return alignmentValue;
                }
            } else {
                return new int[]{Integer.parseInt(imageAlign)};
            }
        }
        return null;
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