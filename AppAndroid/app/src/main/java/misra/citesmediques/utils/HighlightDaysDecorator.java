package misra.citesmediques.utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.DayViewDecorator;

import java.util.List;


public class HighlightDaysDecorator {
/*
    private int textColor = Color.BLACK;

    public HighlightDaysDecorator() {
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new CustomSpan(textColor));
    }

    private class CustomSpan extends MetricAffectingSpan {

        private final int color;

        public CustomSpan(int color) {
            this.color = color;
        }

        @Override
        public void updateMeasureState(TextPaint textPaint) {
            textPaint.setColor(color);
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
            textPaint.setAntiAlias(true);
        }

        @Override
        public void updateDrawState(TextPaint textPaint) {
            textPaint.setColor(color);
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
            textPaint.setAntiAlias(true);
        }
    }

    protected CustomDayView(Parcel in) {
        textColor = in.readInt();
    }

    public static final Creator<CustomDayView> CREATOR = new Creator<CustomDayView>() {
        @Override
        public CustomDayView createFromParcel(Parcel in) {
            return new CustomDayView(in);
        }

        @Override
        public CustomDayView[] newArray(int size) {
            return new CustomDayView[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(textColor);
    }

 */
}

