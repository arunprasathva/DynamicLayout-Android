package com.minscapecomputing.dynamiccomponents;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Arun on 08-08-2016.
 */
public class WelcomeSlidePagerAdapter extends FragmentStatePagerAdapter {

    JSONArray jsonArray;

    public WelcomeSlidePagerAdapter(FragmentManager fm, JSONArray jsonArray) {
        super(fm);
        this.jsonArray = jsonArray;
    }

    private WelcomeSlidePagerFragment createFragment(JSONObject collection) {

        WelcomeSlidePagerFragment fragment = WelcomeSlidePagerFragment.newInstance();
        Bundle args = new Bundle();
        args.putString(WelcomeSlidePagerFragment.OBJECT, collection.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return createFragment(jsonArray.optJSONObject(position));
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
//        container.removeView((View)object);
    }
}
