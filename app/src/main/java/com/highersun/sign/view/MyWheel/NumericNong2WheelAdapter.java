package com.highersun.sign.view.MyWheel;


/**
 * Numeric Wheel adapter.
 */
public class NumericNong2WheelAdapter implements WheelAdapter {

	/** The default min value */
	public static final int DEFAULT_MAX_VALUE = 9;

	/** The default max value */
	private static final int DEFAULT_MIN_VALUE = 0;

	// Values
	private int minValue;
	private int maxValue;

	// format
	private String format;
	private int size;
	/**
	 * Default constructor
	 */
	public NumericNong2WheelAdapter() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	/**
	 * Constructor
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public NumericNong2WheelAdapter(int minValue, int maxValue) {
		this(minValue, maxValue, null,0);
	}

	/**
	 * Constructor
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 * @param format the format string
	 */
	public NumericNong2WheelAdapter(int minValue, int maxValue, String format,int size) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
		this.size=size;
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			int value = minValue + index;
			if(size==0){
				return format != null ? String.format(format, value) : Integer.toString(value);
			}else
			return value-size<10?"0"+ Integer.toString(value - size)+format: Integer.toString(value - size)+format;
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return maxValue - minValue + 1;
	}
	
	@Override
	public int getMaximumLength() {
		int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		return maxLen;
	}
}
