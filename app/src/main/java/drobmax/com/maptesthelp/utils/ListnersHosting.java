package drobmax.com.maptesthelp.utils;

import drobmax.com.maptesthelp.interfaces.OnDataEditingListner;
import drobmax.com.maptesthelp.interfaces.OnDrawerOpenListner;
import drobmax.com.maptesthelp.interfaces.OnMyEventListener;
import drobmax.com.maptesthelp.interfaces.OnLocationAddListener;
import drobmax.com.maptesthelp.interfaces.OnNewItemCreatedListener;
import drobmax.com.maptesthelp.interfaces.OnReplaceFragmentListener;

/**
 * Created by Admin on 06.08.2015.
 */
public class ListnersHosting {
    private static ListnersHosting ourInstance = new ListnersHosting();

    public static ListnersHosting getInstance() {
        return ourInstance;
    }

    private OnLocationAddListener onLocationAddListener;
    private OnMyEventListener onMyEventListener;
    private OnReplaceFragmentListener onReplaceFragmentListener;
    private OnDataEditingListner onDataEditingListner;
    private OnNewItemCreatedListener onNewItemCreatedListener;
    private OnDrawerOpenListner onDrawerOpenListner;

    public OnDrawerOpenListner getOnDrawerOpenListner() {
        return onDrawerOpenListner;
    }

    public void setOnDrawerOpenListner(OnDrawerOpenListner onDrawerOpenListner) {
        this.onDrawerOpenListner = onDrawerOpenListner;
    }

    public OnNewItemCreatedListener getOnNewItemCreatedListener() {
        return onNewItemCreatedListener;
    }

    public void setOnNewItemCreatedListener(OnNewItemCreatedListener onNewItemCreatedListener) {
        this.onNewItemCreatedListener = onNewItemCreatedListener;
    }

    public OnDataEditingListner getOnDataEditingListner() {
        return onDataEditingListner;
    }

    public void setOnDataEditingListner(OnDataEditingListner onDataEditingListner) {
        this.onDataEditingListner = onDataEditingListner;
    }

    public OnReplaceFragmentListener getOnReplaceFragmentListener() {
        return onReplaceFragmentListener;
    }

    public void setOnReplaceFragmentListener(OnReplaceFragmentListener onReplaceFragmentListener) {
        this.onReplaceFragmentListener = onReplaceFragmentListener;
    }

    public OnMyEventListener getOnMyEventListener1() {
        return onMyEventListener1;
    }

    public void setOnMyEventListener1(OnMyEventListener onMyEventListener1) {
        this.onMyEventListener1 = onMyEventListener1;
    }

    private OnMyEventListener onMyEventListener1;

    public OnMyEventListener getOnMyEventListener() {
        return onMyEventListener;
    }

    public void setOnMyEventListener(OnMyEventListener onMyEventListener) {
        this.onMyEventListener = onMyEventListener;
    }

    public OnLocationAddListener getOnLocationAddListener() {
        return onLocationAddListener;
    }


    public void setOnLocationAddListener(OnLocationAddListener onLocationAddListener) {
        this.onLocationAddListener = onLocationAddListener;
    }

    private ListnersHosting() {
    }

}
