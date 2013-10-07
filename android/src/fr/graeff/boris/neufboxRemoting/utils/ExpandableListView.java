package fr.graeff.boris.neufboxRemoting.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * ListView which can be expanded
 * 
 */
public class ExpandableListView extends ListView
{

	boolean expanded = false;

	/**
	 * Constructor
	 * @param context
	 */
    public ExpandableListView(Context context)
    {
        super(context);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public ExpandableListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ExpandableListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
    	this.setFocusable(false);
        if (expanded)
        {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * Force the list view to be expanded
     * @param expanded
     */
    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }
}