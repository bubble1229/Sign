package com.highersun.sign.view.MyWheel;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;


import com.highersun.sign.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间选择器
 *
 * @author Sai
 */
public class TimePopupWindow extends PopupWindow implements OnClickListener {
    public enum Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN, YEAR_MONTH, DAY
    }// 四种选择模式，年月日时分，年月日，时分，月日时分

    public enum Type2 {
        LUNAR, SUN
    }

    private View rootView; // 总的布局
    WheelTimeNong wheelTime;
    private View btnSubmit, btnCancel, ll_isLunar;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private static final String TAG_ISLUNAR = "islunar";
    private OnTimeSelectListener timeSelectListener;
    private OnDismissed onDismissed;
    private ImageView iv_isLunar;
    private OnStatuChanged onStatuChanged;
    private Calendar myDateTime;

    public TimePopupWindow(Context context, Type type, Type2 type2) {
        super(context);
        myDateTime=Calendar.getInstance();
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.timepopwindow_anim_style);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(R.layout.pw_time, null);
        // -----确定和取消按钮
        iv_isLunar = (ImageView) rootView.findViewById(R.id.iv_isLunar);
        btnSubmit = rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        ll_isLunar = rootView.findViewById(R.id.ll_isLunar);
        ll_isLunar.setTag(TAG_ISLUNAR);//是否是农历按钮
        ll_isLunar.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        if (type2 == Type2.SUN) {
            ll_isLunar.setVisibility(View.GONE);
        }
        // ----时间转轮
        final View timepickerview = rootView.findViewById(R.id.timepicker);
        ScreenInfo screenInfo = new ScreenInfo((Activity) context);
        wheelTime = new WheelTimeNong(timepickerview, type, type2);

        wheelTime.screenheight = screenInfo.getHeight();
        //默认选中当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);

        setContentView(rootView);
    }

    /**
     * 设置可以选择的时间范围
     *
     * @param START_YEAR
     * @param END_YEAR
     */
    public void setRange(int START_YEAR, int END_YEAR) {
        WheelTimeNong.setSTART_YEAR(START_YEAR);
        WheelTimeNong.setEND_YEAR(END_YEAR);
    }

    /**
     * 设置选中时间
     *
     * @param date
     */
    public void setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);
    }

    /**
     * 指定选中的时间，显示选择器
     *
     * @param parent
     * @param gravity
     * @param x
     * @param y
     * @param date
     */
    public void showAtLocation(View parent, int gravity, int x, int y, Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);
        update();
        if(onStatuChanged!=null){
            onStatuChanged.open(true);
        }
        super.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wheelTime.setCyclic(cyclic);
    }

    /**
     * 手动切换阳历阴历滚轮
     *
     * @param isSun
     */
    public void setIsSun(boolean isSun) {
        wheelTime.setIsSun(isSun);
        if (isSun) {
            iv_isLunar.setImageResource(R.drawable.round_whilte_circle);
        } else {
            iv_isLunar.setImageResource(R.drawable.round_blue_circle);
        }
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
            return;
        } else if (tag.equals(TAG_SUBMIT)) {
            if (timeSelectListener != null) {
                try {
//					Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    timeSelectListener.onTimeSelect(wheelTime.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
            return;
        } else if (tag.equals(TAG_ISLUNAR)) {
            if (wheelTime.getIsSun()) {
                iv_isLunar.setImageResource(R.drawable.round_blue_circle);
                setIsSun(false);
                try {
                    myDateTime=wheelTime.getTime().getCalendar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                wheelTime.setPicker(myDateTime.get(Calendar.YEAR),
                        myDateTime.get(Calendar.MONTH),
                        myDateTime.get(Calendar.DAY_OF_MONTH),
                        myDateTime.get(Calendar.HOUR_OF_DAY),
                        myDateTime.get(Calendar.MINUTE));

            } else {
                iv_isLunar.setImageResource(R.drawable.round_whilte_circle);
                setIsSun(true);
                try {
                    myDateTime=wheelTime.getTime().getCalendar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                wheelTime.setPicker(myDateTime.get(Calendar.YEAR),
                        myDateTime.get(Calendar.MONTH),
                        myDateTime.get(Calendar.DAY_OF_MONTH),
                        myDateTime.get(Calendar.HOUR_OF_DAY),
                        myDateTime.get(Calendar.MINUTE));
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(onStatuChanged!=null){
            onStatuChanged.open(false);
        }
        if (onDismissed != null) {
            onDismissed.onDismissed();
        }
    }

    public interface OnTimeSelectListener {
        public void onTimeSelect(MyDateTime date);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

    public interface OnDismissed {
        public void onDismissed();
    }

    public interface OnStatuChanged{
        public void open(boolean isOpen);
    }

    public void setOnStatuChanged(OnStatuChanged onStatuChanged){
        this.onStatuChanged=onStatuChanged;
    }

    public void setOnDismissed(OnDismissed onDismissed) {
        this.onDismissed = onDismissed;
    }
}
