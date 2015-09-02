package drobmax.com.maptesthelp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import drobmax.com.maptesthelp.R;
import drobmax.com.maptesthelp.adapters.ViewPagerAdapter;
import drobmax.com.maptesthelp.interfaces.OnMyEventListener;
import drobmax.com.maptesthelp.interfaces.OnReplaceFragmentListener;
import drobmax.com.maptesthelp.utils.ListnersHosting;
import drobmax.com.maptesthelp.utils.SlidingTabLayout;

/**
 * Created by Admin on 05.08.2015.
 */
public class PagerFragment extends Fragment implements OnReplaceFragmentListener {
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Editing marker","List of markers"};
    OnMyEventListener onMyEventListener;
    int Numboftabs =2;

    public static PagerFragment newInstance(OnMyEventListener onMyEventListener) {
        PagerFragment fragment = new PagerFragment();
        fragment.onMyEventListener = onMyEventListener;
        Bundle arguments = new Bundle();
        return fragment;
    }
    public PagerFragment(){
        ListnersHosting.getInstance().setOnReplaceFragmentListener(this);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    PagerAdapter adapter;
    ViewPager pager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, null);
        pager = (ViewPager)view.findViewById(R.id.pager1);
         adapter = new ViewPagerAdapter(getActivity().
                getSupportFragmentManager(),Titles,Numboftabs, onMyEventListener);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);
        tabs = (SlidingTabLayout)view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        return view;
    }

    @Override
    public void onReplaceFragment(int position) {
        pager.setCurrentItem(position);
    }
}
