package com.basecamp.android.core.common.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Corbella on 27/11/15.
 */
public class AttrHelper {
    private List<Integer>   keys;
    private TypedArray      results;

    public static AttrHelper from(Context ctx, AttributeSet attrs, int ... values){
        return new AttrHelper(ctx, attrs, values);
    }


    private AttrHelper(Context ctx, AttributeSet attrs, int ... values){
        Arrays.sort(values);
        keys = new ArrayList<>(values.length);
        for (int i : values) keys.add(i);
        results = attrs==null?null:ctx.obtainStyledAttributes(attrs, values);
    }


    public int getResourceId(int key, int def){
        int index = getIndexOf(key);
        if (index<0) return def;
        try {
            return results.getResourceId(index, def);
        }catch (Exception e){
            return def;
        }
    }


    public boolean getBoolean(int key, boolean def){
        int index = getIndexOf(key);
        if (index<0) return def;
        try {
            return results.getBoolean(index, def);
        }catch (Exception e){
            return def;
        }
    }


    public int getInt(int key, int def){
        int index = getIndexOf(key);
        if (index<0) return def;
        try {
            return results.getInt(index, def);
        }catch (Exception e){
            return def;
        }
    }


    public float getFloat(int key, float def){
        int index = getIndexOf(key);
        if (index<0) return def;
        try {
            return results.getFloat(index, def);
        }catch (Exception e){
            return def;
        }
    }


    public String getString(int key, String def){
        int index = getIndexOf(key);
        if (index<0) return def;
        try {
            String d = results.getString(index);
            return d==null?def:d;
        }catch (Exception e){
            return def;
        }
    }


    public <E extends Enum<E>> E getEnum(int key, Class<E> clazz, E def){
        int value = getInt(key, -1);
        if (value<0){
            return def;
        }else{
            return clazz.getEnumConstants()[value];
        }
    }

    public <E extends Enum<E>> List<E> getFlags(int key, Class<E> clazz){
        int value = getInt(key, -1);
        if (value<0){
            return new ArrayList<>();
        }else{
            List<E> result = new ArrayList<>();
            for (E enumConstant : clazz.getEnumConstants()) {
                int integer = (int)Math.pow(2, enumConstant.ordinal());
                if ((integer & value) != 0) result.add(enumConstant);
            }
            return result;
        }
    }


    public int getColor(int key, int def){
        int index = getIndexOf(key);
        if (index<0) return def;
        try {
            return results.getColor(index, def);
        }catch (Exception e){
            return def;
        }
    }


    public int getDimension(int key, int def){
        int index = getIndexOf(key);
        if (index<0) return def;
        try {
            return results.getDimensionPixelSize(index, def);
        }catch (Exception e){
            return def;
        }
    }


    public Drawable getDrawable(int key, Drawable def){
        int index = getIndexOf(key);
        if (index<0) return def;
        try {
            Drawable d = results.getDrawable(index);
            return d==null?def:d;
        }catch (Exception e){
            return def;
        }
    }




    private int getIndexOf(int key){
        if (results==null) return -1;
        return keys.indexOf(key);
    }


    public void close(){
        if (results!=null) results.recycle();
    }

}
