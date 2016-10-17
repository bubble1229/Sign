package com.highersun.sign.view.MyWheel;

import android.content.Context;
import android.view.View;


import com.highersun.sign.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class WheelTimeNong {
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;
	public int screenheight;
	int years;
	private TimePopupWindow.Type type;
	private TimePopupWindow.Type2 type2;
	private static int START_YEAR = 1901, END_YEAR = 2049;
	int num=1;
	Lunar lunar;
	Calendar calendar;
	int toDayNum=0;
	boolean isSun=true;
	int year1,month1,day1,h1,m1;
	Context mContext;
	List<String> list_big ;
	List<String> list_little;
	List<List<String>> runList;
	List<String> list_months;
	OnWheelChangedListener wheelListener_month_sun;
	OnWheelChangedListener wheelListener_year_sun;
	OnWheelChangedListener wheelListener_month_lunar;
	OnWheelChangedListener wheelListener_year_lunar;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelTimeNong(View view) {
		super();
		this.view = view;
		type = TimePopupWindow.Type.ALL;
		setView(view);
	}
	public WheelTimeNong(View view, TimePopupWindow.Type type,TimePopupWindow.Type2 type2) {
		super();
		this.view = view;
		this.type = type;
		this.type2=type2;
		switch (type2){
			case LUNAR:
				isSun=false;
				break;
			case SUN:
				isSun=true;
				break;
		}
		setView(view);
	}
	public void setPicker(int year ,int month,int day){
		this.setPicker(year, month, day, 0, 0);
	}
	
	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void setPicker(int year ,int month ,int day,int h,int m) {
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		calendar= Calendar.getInstance();
		calendar.set(year, month, day,h,m);
		lunar=new Lunar(calendar);
		this.year1=year;
		this.month1=month;
		this.day1=day;
		this.h1=h;
		this.m1=m;

		toDayNum=Lunar.getDayOf1900(lunar.getLunarYear(),lunar.getLunarMonth(),lunar.getLunarDay(),lunar.getLeap());
		String[][] run={
				{"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","冬月","腊月"},
				{"一月","闰一","二月","三月","四月","五月","六月","七月","八月","九月","十月","冬月","腊月"},
				{"一月","二月","闰二","三月","四月","五月","六月","七月","八月","九月","十月","冬月","腊月"},
				{"一月","二月","三月","闰三","四月","五月","六月","七月","八月","九月","十月","冬月","腊月"},
				{"一月","二月","三月","四月","闰四","五月","六月","七月","八月","九月","十月","冬月","腊月"},
				{"一月","二月","三月","四月","五月","闰五","六月","七月","八月","九月","十月","冬月","腊月"},
				{"一月","二月","三月","四月","五月","六月","闰六","七月","八月","九月","十月","冬月","腊月"},
				{"一月","二月","三月","四月","五月","六月","七月","闰七","八月","九月","十月","冬月","腊月"},
				{"一月","二月","三月","四月","五月","六月","七月","八月","闰八","九月","十月","冬月","腊月"},
				{"一月","二月","三月","四月","五月","六月","七月","八月","九月","闰九","十月","冬月","腊月"},
				{"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","闰十","冬月","腊月"},
				{"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","冬月","闰冬","腊月"},
				{"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","冬月","腊月","闰腊"}};
		String[] str_months={
				"初一","初二","初三","初四","初五","初六",
				"初七","初八","初九","初十","十一","十二",
				"十三","十四","十五","十六","十七","十八",
				"十九","二十","廿一","廿二","廿三","廿四",
				"廿五","廿六","廿七","廿八","廿九","三十"};
		list_big = Arrays.asList(months_big);
		list_little = Arrays.asList(months_little);
		runList=new ArrayList<>();
		list_months= Arrays.asList(str_months);
		for (int i=0;i<run.length;i++){
			runList.add(Arrays.asList(run[i]));
		}
		years=lunar.getLunarYear();
		final Context context = view.getContext();
		mContext=view.getContext();
		if(!isSun){
			// 年
			wv_year = (WheelView) view.findViewById(R.id.year);
			wv_year.setAdapter(new NumericNong2WheelAdapter(START_YEAR+100000, END_YEAR+100000,"年",100000));// 设置"年"的显示数据
			wv_year.setCurrentItem(lunar.getLunarYear() - START_YEAR);// 初始化时显示的数据

			// 月
			wv_month = (WheelView) view.findViewById(R.id.month);
			if(Lunar.leapMonth(lunar.getLunarYear())==0){
				wv_month.setAdapter(new NumericNongWheelAdapter(1001, 1012,runList.get(0)));
			}else {
				wv_month.setAdapter(new NumericNongWheelAdapter(1001, 1013,runList.get(Lunar.leapMonth(lunar.getLunarYear()))));
			}
			wv_month.setCurrentItem(lunar.getLunarMonth()-1);

			// 日
			wv_day = (WheelView) view.findViewById(R.id.day);
			if(Lunar.leapMonth(years)==0){
				wv_day.setAdapter(new NumericNongWheelAdapter(1001, 1000+Lunar.monthDays(years, wv_month.getCurrentItem() + 1),list_months));
			}else {
				if(wv_month.getCurrentItem()+1<=Lunar.leapMonth(years)){
					wv_day.setAdapter(new NumericNongWheelAdapter(1001,1000+Lunar.monthDays(years, wv_month.getCurrentItem() + 1),list_months));
				}else {
					wv_day.setAdapter(new NumericNongWheelAdapter(1001,1000+Lunar.monthDays(years, wv_month.getCurrentItem()),list_months));
				}
			}
			wv_day.setCurrentItem(lunar.getLunarDay() - 1);


			wv_hours = (WheelView)view.findViewById(R.id.hour);
			wv_hours.setAdapter(new NumericNong2WheelAdapter(1000, 1023, "时",1000));
			wv_hours.setCurrentItem(h);

			wv_mins = (WheelView)view.findViewById(R.id.min);
			wv_mins.setAdapter(new NumericNong2WheelAdapter(1000, 1059, "分",1000));
			wv_mins.setCurrentItem(m);
		}else {
			// 年
			wv_year = (WheelView) view.findViewById(R.id.year);
			wv_year.setAdapter(new NumericNong2WheelAdapter(START_YEAR + 100000, END_YEAR + 100000, "年", 100000));// 设置"年"的显示数据
//			wv_year.setLabel(context.getString(R.string.pickerview_year));// 添加文字
			wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

			// 月
			wv_month = (WheelView) view.findViewById(R.id.month);
			wv_month.setAdapter(new NumericNong2WheelAdapter(1001, 1012, "月", 1000));
//			wv_month.setLabel(context.getString(R.string.pickerview_month));
			wv_month.setCurrentItem(month);
			// 日
			wv_day = (WheelView) view.findViewById(R.id.day);
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (list_big.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1031,"日",1000));
			} else if (list_little.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1030,"日",1000));
			} else {
				// 闰年
				if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
					wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1029,"日",1000));
				else
					wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1028,"日",1000));
			}
//			wv_day.setLabel(context.getString(R.string.pickerview_day));
			wv_day.setCurrentItem(day - 1);


			wv_hours = (WheelView)view.findViewById(R.id.hour);
			wv_hours.setAdapter(new NumericNong2WheelAdapter(1000, 1023,"时",1000));
//			wv_hours.setLabel(context.getString(R.string.pickerview_hours));// 添加文字
			wv_hours.setCurrentItem(h);

			wv_mins = (WheelView)view.findViewById(R.id.min);
			wv_mins.setAdapter(new NumericNong2WheelAdapter(1000, 1059,"分",1000));
//			wv_mins.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字
			wv_mins.setCurrentItem(m);
		}

		
		// 添加"年"监听
		wheelListener_year_lunar = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				if(num==1){
					if(Lunar.leapMonth(wv_year.getCurrentItem() + START_YEAR)==0){
						wv_month.setAdapter(new NumericNongWheelAdapter(1001, 1012, runList.get(0)));
						years=newValue + START_YEAR;
//						if(Lunar.leapMonth(newValue + START_YEAR)!=Lunar.leapMonth(oldValue + START_YEAR)){
//							if(Lunar.leapMonth(newValue + START_YEAR)==0&&Lunar.leapMonth(oldValue + START_YEAR)<wv_month.getCurrentItem()+1){
//								wv_month.setCurrentItem(wv_month.getCurrentItem()-1);
//							}else if (Lunar.leapMonth(oldValue + START_YEAR)==0&&Lunar.leapMonth(newValue + START_YEAR)<wv_month.getCurrentItem()+1){
//								wv_month.setCurrentItem(wv_month.getCurrentItem()+1);
//							}
//						}
						wv_day.setAdapter(new NumericNongWheelAdapter(1001, 1000+Lunar.monthDays(wv_year.getCurrentItem() + START_YEAR, wv_month.getCurrentItem() + 1),list_months));
					}else {
						wv_month.setAdapter(new NumericNongWheelAdapter(1001, 1013,runList.get(Lunar.leapMonth(wv_year.getCurrentItem() + START_YEAR))));
						years=newValue + START_YEAR;
//						if(Lunar.leapMonth(newValue + START_YEAR)!=Lunar.leapMonth(oldValue + START_YEAR)){
//							if(Lunar.leapMonth(newValue + START_YEAR)==0&&Lunar.leapMonth(oldValue + START_YEAR)<wv_month.getCurrentItem()+1){
//								wv_month.setCurrentItem(wv_month.getCurrentItem()-1);
//							}else if (Lunar.leapMonth(oldValue + START_YEAR)==0&&Lunar.leapMonth(newValue + START_YEAR)<wv_month.getCurrentItem()+1){
//								wv_month.setCurrentItem(wv_month.getCurrentItem()+1);
//							}
//						}
						if(wv_month.getCurrentItem()+1<=Lunar.leapMonth(wv_year.getCurrentItem() + START_YEAR)) {
							wv_day.setAdapter(new NumericNongWheelAdapter(1001, 1000 + Lunar.monthDays(wv_year.getCurrentItem() + START_YEAR, wv_month.getCurrentItem() + 1), list_months));
						}else {
							wv_day.setAdapter(new NumericNongWheelAdapter(1001,1000+Lunar.monthDays(wv_year.getCurrentItem() + START_YEAR, wv_month.getCurrentItem()),list_months));
						}
					}

				}
//				num++;
//				if(num==4){
//					num=1;
//				}
//			}
		};
		// 添加"月"监听
		wheelListener_month_lunar = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				int maxItem = 30;
				if(Lunar.leapMonth(years)==0){
					wv_day.setAdapter(new NumericNongWheelAdapter(1001, 1000+Lunar.monthDays(years, month_num),list_months));
				}else {
					if(wv_month.getCurrentItem()+1<=Lunar.leapMonth(years)){
						wv_day.setAdapter(new NumericNongWheelAdapter(1001,1000+Lunar.monthDays(years, month_num),list_months));
					}else {
						wv_day.setAdapter(new NumericNongWheelAdapter(1001,1000+Lunar.monthDays(years, month_num-1),list_months));
					}
				}
				if (wv_day.getCurrentItem() > maxItem - 1){
					wv_day.setCurrentItem(maxItem - 1);
				}

			}
		};
		// 添加"年"监听
		wheelListener_year_sun = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				int maxItem = 30;
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1031,"日",1000));
					maxItem = 31;
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1030,"日",1000));
					maxItem = 30;
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0){
						wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1029,"日",1000));
						maxItem = 29;
					}
					else{
						wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1028,"日",1000));
						maxItem = 28;
					}
				}
				if (wv_day.getCurrentItem() > maxItem - 1){
					wv_day.setCurrentItem(maxItem - 1);
				}
			}
		};
		// 添加"月"监听
		wheelListener_month_sun = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				int maxItem = 30;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1031,"日",1000));
					maxItem = 31;
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1030,"日",1000));
					maxItem = 30;
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0){
						wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1029,"日",1000));
						maxItem = 29;
					}
					else{
						wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1028,"日",1000));
						maxItem = 28;
					}
				}
				if (wv_day.getCurrentItem() > maxItem - 1){
					wv_day.setCurrentItem(maxItem - 1);
				}

			}
		};
		if(!isSun){
			wv_year.addChangingListener(wheelListener_year_lunar);
			wv_month.addChangingListener(wheelListener_month_lunar);
		}else {
			wv_year.addChangingListener(wheelListener_year_sun);
			wv_month.addChangingListener(wheelListener_month_sun);
		}

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		switch(type){
		case ALL:
			textSize = (screenheight / 90) * 2;
			break;
		case YEAR_MONTH_DAY:
			textSize = (screenheight / 90) * 2;
			wv_hours.setVisibility(View.GONE);
			wv_mins.setVisibility(View.GONE);
			break;
		case HOURS_MINS:
			textSize = (screenheight / 100) * 3;
			wv_year.setVisibility(View.GONE);
			wv_month.setVisibility(View.GONE);
			wv_day.setVisibility(View.GONE);
			break;
		case MONTH_DAY_HOUR_MIN:
			textSize = (screenheight / 100) * 3;
			wv_year.setVisibility(View.GONE);
			break;
        case YEAR_MONTH:
            textSize = (screenheight / 100) * 3;
            wv_day.setVisibility(View.GONE);
            wv_hours.setVisibility(View.GONE);
            wv_mins.setVisibility(View.GONE);
			break;
		case DAY:
			textSize = (screenheight / 90) * 2;
			wv_year.setVisibility(View.GONE);
			wv_month.setVisibility(View.GONE);
			wv_hours.setVisibility(View.GONE);
			wv_mins.setVisibility(View.GONE);
			break;

		}
			
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;

	}

	/**
	 * 设置是否循环滚动
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic){
		wv_year.setCyclic(cyclic);
		wv_month.setCyclic(cyclic);
		wv_day.setCyclic(cyclic);
		wv_hours.setCyclic(cyclic);
		wv_mins.setCyclic(cyclic);
	}
	public MyDateTime getTime() throws ParseException {
		StringBuffer sb = new StringBuffer();
		if(!isSun){
			int year=(wv_year.getCurrentItem() + START_YEAR);
			int runMonth=Lunar.leapMonth(year);
			int day=wv_day.getCurrentItem()+1;
			boolean isRun=false;
			int month=1;
			if(runMonth==0){
				month=wv_month.getCurrentItem()+1;
			}else if(runMonth!=0&&(wv_month.getCurrentItem()+1)<=runMonth){
				month=wv_month.getCurrentItem()+1;
			}else if(runMonth!=0&&wv_month.getCurrentItem()==runMonth){
				month=wv_month.getCurrentItem();
				isRun=true;
			}else if(runMonth!=0&&wv_month.getCurrentItem()>runMonth) {
				month=wv_month.getCurrentItem();
			}
			int num=Lunar.getDayOf1900(year,month,day,isRun);
			Calendar calendar1=calendar;
			calendar1.add(Calendar.DATE,(num-toDayNum));
			sb.append(calendar1.get(Calendar.YEAR)).append("-")
					.append(calendar1.get(Calendar.MONTH) + 1).append("-")
					.append(calendar1.get(Calendar.DAY_OF_MONTH)).append(" ")
					.append(wv_hours.getCurrentItem()).append(":")
					.append(wv_mins.getCurrentItem());
		}else {
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
					.append((wv_month.getCurrentItem() + 1)).append("-")
					.append((wv_day.getCurrentItem() + 1)).append(" ")
					.append(wv_hours.getCurrentItem()).append(":")
					.append(wv_mins.getCurrentItem());
		}
		Date date = WheelTimeNong.dateFormat.parse(sb.toString());
		Calendar calendar2= Calendar.getInstance();
		calendar2.setTime(date);
		MyDateTime myDateTime=new MyDateTime(calendar2,isSun);
		return myDateTime;
	}
	public void setIsSun(boolean isSun){
		this.isSun=isSun;
//		if(!isSun){
//			// 年
//			wv_year = (WheelView) view.findViewById(R.id.year);
//			wv_year.setAdapter(new NumericNong2WheelAdapter(START_YEAR+100000, END_YEAR+100000,"年",100000));// 设置"年"的显示数据
//			wv_year.setCurrentItem(lunar.getLunarYear() - START_YEAR);// 初始化时显示的数据
//
//			// 月
//			wv_month = (WheelView) view.findViewById(R.id.month);
//			if(Lunar.leapMonth(lunar.getLunarYear())==0){
//				wv_month.setAdapter(new NumericNongWheelAdapter(1001, 1012,runList.get(0)));
//			}else {
//				wv_month.setAdapter(new NumericNongWheelAdapter(1001, 1013,runList.get(Lunar.leapMonth(lunar.getLunarYear()))));
//			}
//			wv_month.setCurrentItem(lunar.getLunarMonth()-1);
//
//			// 日
//			wv_day = (WheelView) view.findViewById(R.id.day);
//			if(Lunar.leapMonth(years)==0){
//				wv_day.setAdapter(new NumericNongWheelAdapter(1001, 1000+Lunar.monthDays(years, wv_month.getCurrentItem() + 1),list_months));
//			}else {
//				if(wv_month.getCurrentItem()+1<=Lunar.leapMonth(years)){
//					wv_day.setAdapter(new NumericNongWheelAdapter(1001,1000+Lunar.monthDays(years, wv_month.getCurrentItem() + 1),list_months));
//				}else {
//					wv_day.setAdapter(new NumericNongWheelAdapter(1001,1000+Lunar.monthDays(years, wv_month.getCurrentItem()),list_months));
//				}
//			}
//			wv_day.setCurrentItem(lunar.getLunarDay() - 1);
//
//
//			wv_hours = (WheelView)view.findViewById(R.id.hour);
//			wv_hours.setAdapter(new NumericNong2WheelAdapter(1000, 1023, "时",1000));
//			wv_hours.setCurrentItem(h1);
//
//			wv_mins = (WheelView)view.findViewById(R.id.min);
//			wv_mins.setAdapter(new NumericNong2WheelAdapter(1000, 1059, "分", 1000));
//			wv_mins.setCurrentItem(m1);
//		}else {
//			// 年
//			wv_year = (WheelView) view.findViewById(R.id.year);
//			wv_year.setAdapter(new NumericNong2WheelAdapter(START_YEAR+100000, END_YEAR+100000,"年",100000));// 设置"年"的显示数据
////			wv_year.setLabel(mContext.getString(R.string.pickerview_year));// 添加文字
//			wv_year.setCurrentItem(year1 - START_YEAR);// 初始化时显示的数据
//
//			// 月
//			wv_month = (WheelView) view.findViewById(R.id.month);
//			wv_month.setAdapter(new NumericNong2WheelAdapter(1001, 1012,"月",1000));
////			wv_month.setLabel(mContext.getString(R.string.pickerview_month));
//			wv_month.setCurrentItem(month1);
//
//			// 日
//			wv_day = (WheelView) view.findViewById(R.id.day);
//			// 判断大小月及是否闰年,用来确定"日"的数据
//			if (list_big.contains(String.valueOf(month1 + 1))) {
//				wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1031,"日",1000));
//			} else if (list_little.contains(String.valueOf(month1 + 1))) {
//				wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1030,"日",1000));
//			} else {
//				// 闰年
//				if ((year1 % 4 == 0 && year1 % 100 != 0) || year1 % 400 == 0)
//					wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1029,"日",1000));
//				else
//					wv_day.setAdapter(new NumericNong2WheelAdapter(1001, 1028,"日",1000));
//			}
////			wv_day.setLabel(mContext.getString(R.string.pickerview_day));
//			wv_day.setCurrentItem(day1 - 1);
//
//
//			wv_hours = (WheelView)view.findViewById(R.id.hour);
//			wv_hours.setAdapter(new NumericNong2WheelAdapter(1000, 1023,"时",1000));
////			wv_hours.setLabel(mContext.getString(R.string.pickerview_hours));// 添加文字
//			wv_hours.setCurrentItem(h1);
//
//			wv_mins = (WheelView)view.findViewById(R.id.min);
//			wv_mins.setAdapter(new NumericNong2WheelAdapter(1000, 1059,"分",1000));
////			wv_mins.setLabel(mContext.getString(R.string.pickerview_minutes));// 添加文字
//			wv_mins.setCurrentItem(m1);
//		}
//		if(!isSun){
//			wv_year.addChangingListener(wheelListener_year_lunar);
//			wv_month.addChangingListener(wheelListener_month_lunar);
//		}else {
//			wv_year.addChangingListener(wheelListener_year_sun);
//			wv_month.addChangingListener(wheelListener_month_sun);
//		}
	}
	public boolean getIsSun(){
		return isSun;
	}
}
