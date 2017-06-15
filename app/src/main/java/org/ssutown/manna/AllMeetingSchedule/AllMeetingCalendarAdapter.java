package org.ssutown.manna.AllMeetingSchedule;

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
 * Created by YNH on 2017. 5. 30..
 */

public class AllMeetingCalendarAdapter extends BaseAdapter {
    // Variables
    private Context mContext;
    private static AllMeetingCalendarAdapter.ViewHolder mHolder;
    int mWeekDayNames = 7;
    int mGridViewIndexOffset = 1;

    private static class ViewHolder {
        ImageView mSelectedDayImageView;
        TextView mTextView;
        ImageView mSavedEventImageView;
    }

    // Constructor
    public AllMeetingCalendarAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        if (AllMeetingCalendar.mFirstDay != -1 && AllMeetingCalendar.mNumDaysInMonth != -1) {
            Log.d("GRID_COUNT", String.valueOf(mWeekDayNames + AllMeetingCalendar.mFirstDay + AllMeetingCalendar.mNumDaysInMonth));
            return mWeekDayNames + AllMeetingCalendar.mFirstDay + AllMeetingCalendar.mNumDaysInMonth;
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
            convertView = inflater.inflate(R.layout.layout_material_day, parent, false);

            mHolder = new AllMeetingCalendarAdapter.ViewHolder();

            if (convertView != null) {
                mHolder.mSelectedDayImageView = (ImageView) convertView.findViewById(R.id.material_calendar_selected_day);
                mHolder.mTextView = (TextView) convertView.findViewById(R.id.material_calendar_day);
                mHolder.mSavedEventImageView = (ImageView) convertView.findViewById(R.id.saved_event_imageView);
                convertView.setTag(mHolder);
            }
        } else {
            mHolder = (AllMeetingCalendarAdapter.ViewHolder) convertView.getTag();
        }

        if (mHolder.mSelectedDayImageView != null) {
            GridView gridView = (GridView) parent;
            Log.d("ITEM_CHECKED_POSITION", String.valueOf(gridView.isItemChecked(position)));
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
        if (position <= mWeekDayNames - mGridViewIndexOffset + AllMeetingCalendar.mFirstDay) {
            mHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.calendar_day_text_color));
            Log.d("NO_CLICK_POSITION", String.valueOf(position));
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
                if (position < mWeekDayNames + AllMeetingCalendar.mFirstDay) {
                    Log.d("BLANK_POSITION", "This is a blank day");
                    mHolder.mTextView.setText("");
                    mHolder.mTextView.setTypeface(Typeface.DEFAULT);
                } else {
                    mHolder.mTextView.setText(String.valueOf(position - (mWeekDayNames - mGridViewIndexOffset) -
                            AllMeetingCalendar.mFirstDay));
                    mHolder.mTextView.setTypeface(Typeface.DEFAULT_BOLD);

                    if (AllMeetingCalendar.mCurrentDay != -1) {
                        int startingPosition = mWeekDayNames - mGridViewIndexOffset + AllMeetingCalendar.mFirstDay;
                        int currentDayPosition = startingPosition + AllMeetingCalendar.mCurrentDay;

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

    private void setSavedEvent(int position) {
        // Reset saved position indicator
        mHolder.mSavedEventImageView.setVisibility(View.INVISIBLE);

        if (AllMeetingCalendar.mFirstDay != -1 && AllMeetingCalendarFragment.mSavedEventDays != null &&
                AllMeetingCalendarFragment.mSavedEventDays.size() > 0) {

            int startingPosition = mWeekDayNames - mGridViewIndexOffset + AllMeetingCalendar.mFirstDay;
//            Log.d("SAVED_EVENT_STARTING_POS", String.valueOf(startingPosition));
            if (position > startingPosition) {
                for(int i=0;i<AllMeetingCalendarFragment.mSavedEventDays.size(); i++){
                    int realyear = AllMeetingCalendar.mYear;
                    int realmonth = AllMeetingCalendar.mMonth+1;

                    String[] temp1 = AllMeetingCalendarFragment.mSavedEventDays.get(i).split("month");
                    String[] temp2 = temp1[1].split("day");
                    String month = temp2[0];
                    String[] temp3 = temp2[1].split("start");
                    String day = temp3[0];
                    String[] temp4 = temp3[1].split("end");
                    String start = temp4[0];
                    String end = temp4[1];

                    int savedEventPosition = -1;
                    if(realmonth == Integer.valueOf(month)){
                        savedEventPosition = startingPosition + Integer.valueOf(day);
                    }

                    if (position == savedEventPosition) {
                        mHolder.mSavedEventImageView.setVisibility(View.VISIBLE);
                    }

                }


//                for (int i = 0; i < AllMeetingCalendarFragment.mSavedEventDays.size(); i++) {
//                    int savedEventPosition = startingPosition + AllMeetingCalendarFragment.mSavedEventDays.get(i);
//
//                    Log.d("POSITION", String.valueOf(position));
//                    Log.d("SAVED_POSITION", String.valueOf(savedEventPosition));

//                }
            } else {
                mHolder.mSavedEventImageView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
