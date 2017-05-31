package org.ssutown.manna.CustomCalendar;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.ssutown.manna.R;

/**
 * Created by Maximilian on 9/1/14.
 */
public class MaterialCalendarAdapter extends BaseAdapter {
    // Variables
    private Context mContext;
    private static ViewHolder mHolder;
    int mWeekDayNames = 7;
    int mGridViewIndexOffset = 1;

    private static class ViewHolder {
        ImageView mSelectedDayImageView;
    TextView mTextView;
    ImageView mSavedEventImageView;
}

    // Constructor
    public MaterialCalendarAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        if (MaterialCalendar.mFirstDay != -1 && MaterialCalendar.mNumDaysInMonth != -1) {
//            Log.d("GRID_COUNT", String.valueOf(mWeekDayNames + MaterialCalendar.mFirstDay + MaterialCalendar.mNumDaysInMonth));
            return mWeekDayNames + MaterialCalendar.mFirstDay + MaterialCalendar.mNumDaysInMonth;
        }

        return mWeekDayNames;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_material_day, parent, false);//일에 동그라미

            mHolder = new ViewHolder();

            if (convertView != null) {
                mHolder.mSelectedDayImageView = (ImageView) convertView.findViewById(R.id.material_calendar_selected_day);
                mHolder.mTextView = (TextView) convertView.findViewById(R.id.material_calendar_day);
                mHolder.mSavedEventImageView = (ImageView) convertView.findViewById(R.id.saved_event_imageView);
                convertView.setTag(mHolder);
            }
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        if (mHolder.mSelectedDayImageView != null) {
            GridView gridView = (GridView) parent;
//            Log.d("ITEM_CHECKED_POSITION", String.valueOf(gridView.isItemChecked(position)));
            if (gridView.isItemChecked(position)) {
                Animation feedBackAnimation = AnimationUtils.loadAnimation(mContext, R.anim.selected_day_feedback);
                mHolder.mSelectedDayImageView.setVisibility(View.VISIBLE);

                if (feedBackAnimation != null) {
                    mHolder.mSelectedDayImageView.startAnimation(feedBackAnimation);
                }
            } else {
                mHolder.mSelectedDayImageView.setVisibility(View.INVISIBLE);
            }
        }


        if (mHolder.mTextView != null) {
            setCalendarDay(position);
        }

        if (mHolder.mSavedEventImageView != null) {
            setSavedEvent(position);
        }

        return convertView;
    }



    private void setCalendarDay(int position) {
        if (position <= mWeekDayNames - mGridViewIndexOffset + MaterialCalendar.mFirstDay) {
            mHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.calendar_day_text_color));
//            Log.d("NO_CLICK_POSITION", String.valueOf(position));
        } else {
            mHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.calendar_number_text_color));
        }

        switch (position) {
            case 0:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.sunday));
                mHolder.mTextView.setTypeface(Typeface.DEFAULT);
                break;

            case 1:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.monday));
                mHolder.mTextView.setTypeface(Typeface.DEFAULT);
                break;

            case 2:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.tuesday));
                mHolder.mTextView.setTypeface(Typeface.DEFAULT);
                break;

            case 3:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.wednesday));
                mHolder.mTextView.setTypeface(Typeface.DEFAULT);
                break;

            case 4:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.thursday));
                mHolder.mTextView.setTypeface(Typeface.DEFAULT);
                break;

            case 5:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.friday));
                mHolder.mTextView.setTypeface(Typeface.DEFAULT);
                break;

            case 6:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.saturday));
                mHolder.mTextView.setTypeface(Typeface.DEFAULT);
                break;

            default:
                Log.d("CURRENT_POSITION", String.valueOf(position));
                if (position < mWeekDayNames + MaterialCalendar.mFirstDay) {    //blank day
                    Log.d("BLANK_POSITION", "This is a blank day");
                    mHolder.mTextView.setText("");
                    mHolder.mTextView.setTypeface(Typeface.DEFAULT);
                } else {                                                        //no blank day
                    mHolder.mTextView.setText(String.valueOf(position - (mWeekDayNames - mGridViewIndexOffset) -
                            MaterialCalendar.mFirstDay));
                    mHolder.mTextView.setTypeface(Typeface.DEFAULT_BOLD);

                    if (MaterialCalendar.mCurrentDay != -1) {
                        int startingPosition = mWeekDayNames - mGridViewIndexOffset + MaterialCalendar.mFirstDay;
                        int currentDayPosition = startingPosition + MaterialCalendar.mCurrentDay;

                        if (position == currentDayPosition) {
                            mHolder.mTextView.setTextColor(mContext.getResources().getColor(
                                    R.color.calendar_current_number_text_color));

                        } else {
                            mHolder.mTextView.setTextColor(mContext.getResources().getColor(
                                    R.color.calendar_number_text_color));
                        }
                    } else {
                        mHolder.mTextView.setTextColor(mContext.getResources().getColor(
                                R.color.calendar_number_text_color));
                    }
                }
                break;
        }
    }

    private void setSavedEvent(int position) {          //일정 있는 부분 표시, listview는 띄워주지 않아
        //여기서 색 바꾸면 될듯
        // Reset saved position indicator
        mHolder.mSavedEventImageView.setVisibility(View.INVISIBLE);

//        if (MaterialCalendar.mFirstDay != -1 && MaterialCalendarFragment.mSavedEventDays != null &&
//                MaterialCalendarFragment.mSavedEventDays.size() > 0) {
//
//            int startingPosition = mWeekDayNames - mGridViewIndexOffset + MaterialCalendar.mFirstDay;
//            Log.d("SAVEDEVENTSTARTING_POS", String.valueOf(startingPosition));
//            if (position > startingPosition) {
//                for (int i = 0; i < MaterialCalendarFragment.mSavedEventDays.size(); i++) {
//                    int savedEventPosition = startingPosition + MaterialCalendarFragment.mSavedEventDays.get(i);
//
//                    Log.d("POSITION", String.valueOf(position));
//                    Log.d("SAVED_POSITION", String.valueOf(savedEventPosition));
//                    if (position == savedEventPosition) {
//                        mHolder.mSavedEventImageView.setVisibility(View.VISIBLE);
//                    }
//                }
//            } else {
//                mHolder.mSavedEventImageView.setVisibility(View.INVISIBLE);
//            }
//        }
        if (MaterialCalendar.mFirstDay != -1 && MaterialCalendarFragment.mSaveTestday != null &&
                MaterialCalendarFragment.mSaveTestday.size() > 0) {

            int startingPosition = mWeekDayNames - mGridViewIndexOffset + MaterialCalendar.mFirstDay;
            if (position > startingPosition) {

                for (int i = 0; i < MaterialCalendarFragment.mSaveTestday.size(); i++) {
                    //mSaveTestDay에서 year이랑 month뽑아내서 맞는 달 찾아내서 비교하는 코드
                    int realyear = MaterialCalendar.mYear;
                    int realmonth = MaterialCalendar.mMonth+1;
                    String[] temp1 = MaterialCalendarFragment.mSaveTestday.get(i).split("month");
                    String[] temp2 = temp1[0].split("ear");
                    String year = temp2[1];
                    String[] temp3 = temp1[1].split("day");
                    String month = temp3[0];
                    String day = temp3[1];
                    int savedEventPosition = -1;
                    if(realyear == Integer.valueOf(year) && realmonth == Integer.valueOf(month)){
                        savedEventPosition = startingPosition + Integer.valueOf(day);
                    }

                    if (position == savedEventPosition) {
                        mHolder.mSavedEventImageView.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                mHolder.mSavedEventImageView.setVisibility(View.INVISIBLE);
            }
        }


    }
}
