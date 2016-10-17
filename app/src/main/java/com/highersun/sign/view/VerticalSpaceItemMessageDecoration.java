package com.highersun.sign.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by admin on 2016/8/24.
 */

public class VerticalSpaceItemMessageDecoration  extends RecyclerView.ItemDecoration{

    private final int mVerticalSpaceHeight;

    public VerticalSpaceItemMessageDecoration(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = mVerticalSpaceHeight;
        outRect.left = mVerticalSpaceHeight;
        outRect.right = mVerticalSpaceHeight;
    }

}
