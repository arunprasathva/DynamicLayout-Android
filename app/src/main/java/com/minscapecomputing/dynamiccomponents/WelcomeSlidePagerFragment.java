package com.minscapecomputing.dynamiccomponents;

import com.avocarrot.json2view.DynamicProperty;
import com.avocarrot.json2view.DynamicView;
import com.avocarrot.json2view.DynamicViewId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Arun on 08-08-2016.
 */
public class WelcomeSlidePagerFragment extends Fragment {

    public static final String OBJECT = "object";

    private JSONObject jsonObject;

    public WelcomeSlidePagerFragment() {
    }

    public static WelcomeSlidePagerFragment newInstance() {
        return new WelcomeSlidePagerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        try {
            jsonObject = new JSONObject(args.getString(OBJECT));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Activity activity = getActivity();

        final View sampleView = DynamicView.createView(activity, jsonObject, ClickViewHolder.class);
        if (sampleView.getTag() != null) {
            try {
                if (((ClickViewHolder) sampleView.getTag()).videoClickableView != null) {
                    ((ClickViewHolder) sampleView.getTag()).videoClickableView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.getTag() != null) {
                                DynamicProperty dynamicProperty = (DynamicProperty) v.getTag();
                                Bundle bundle = new Bundle();
//                                bundle.putString("VideoUrl", v.getTag().toString());
                                bundle.putString("VideoUrl", dynamicProperty.getValueString());
                                bundle.putString("page", "custom");
                                if (dynamicProperty.getVideoHeight() > 0) {
                                    bundle.putInt("height", dynamicProperty.getVideoHeight());
                                    bundle.putInt("width", dynamicProperty.getVideoWidth());
                                }
                                bundle.putString("align", dynamicProperty.getVideoAlign());
                                VideoDialogFragment videoDialogFragment = VideoDialogFragment.newInstance(bundle);
                                videoDialogFragment.show(getFragmentManager(), "Video View Fragment");
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (((ClickViewHolder) sampleView.getTag()).buttonClickableView != null) {
                    ((ClickViewHolder) sampleView.getTag()).buttonClickableView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(activity, SecondActivity.class));
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (((ClickViewHolder) sampleView.getTag()).thumbImage != null) {
                    ((ClickViewHolder) sampleView.getTag()).thumbImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject item = (JSONObject) v.getTag();
                            if (item != null) {
                                if (!TextUtils.isEmpty(item.optString("video_uri"))) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("VideoUrl", item.optString("video_uri"));
                                    bundle.putString("page", "custom");
                                    if (!TextUtils.isEmpty(item.optString("height"))) {
                                        bundle.putInt("height", Integer.parseInt(item.optString("height")));
                                        bundle.putInt("width", Integer.parseInt(item.optString("width")));
                                    }
                                    bundle.putString("align", item.optString("align"));
                                    VideoDialogFragment videoDialogFragment = VideoDialogFragment.newInstance(bundle);
                                    videoDialogFragment.show(getFragmentManager(), "Video View Fragment");
                                }
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (((ClickViewHolder) sampleView.getTag()).gridView != null) {

                    View view = ((ClickViewHolder) sampleView.getTag()).gridView;
                    JSONArray jsonArray = new JSONArray(view.getTag().toString());
                    final GridAdapter gridAdapter = new GridAdapter(activity, jsonArray);
                    gridAdapter.setVideoPlayListener(new GridAdapter.VideoPlayListener() {
                        @Override
                        public void onVideoPlayClicked(JSONObject item, boolean isVideo) {
                            if (((ClickViewHolder) sampleView.getTag()).thumbImage != null) {

                                View imageView = ((ClickViewHolder) sampleView.getTag()).thumbImage;
                                imageView.setTag(item);
                                Picasso.with(activity)
                                        .load(item.optString("content_uri"))
                                        .placeholder(R.drawable.ic_place_holder)
                                        .error(R.drawable.no_image)
                                        .into((ImageView) (imageView));

                                if (((ClickViewHolder) sampleView.getTag()).playVideo != null) {
                                    ((ClickViewHolder) sampleView.getTag()).playVideo.setVisibility(isVideo ? View.VISIBLE : View.GONE);
                                }
                            }
                        }
                    });
                    final GridView grid = ((GridView) (view));
                    grid.setAdapter(gridAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sampleView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));

        return sampleView;
    }

    private void startPlaying(final FullscreenVideoLayout videoView, Uri uri) {

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
                    }
                });

                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//                        showFailureAlert(getString(R.string.err_video));
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

    static public class ClickViewHolder {
        @DynamicViewId(id = "videoClick")
        public View videoClickableView;

        @DynamicViewId(id = "buttonClick")
        public View buttonClickableView;

        @DynamicViewId(id = "gridView")
        public View gridView;

        @DynamicViewId(id = "thumbImage")
        public View thumbImage;

        @DynamicViewId(id = "playVideo")
        public View playVideo;

        public ClickViewHolder() {
        }
    }
}
