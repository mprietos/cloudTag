package net.opentrends.cloudtaglibrary;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class CloudTag extends RelativeLayout {

    private static final int FIRST_INDEX = 100;
    private static final int ROUNDED_RADIUS = 40;
    private static final int VIEW_PADDING_INSIDE = 10;
    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final int TEXT_SIZE_MORE_ELEMENTS = 19;
    private static final int DEFAULT_TAG_LAYOUT_COLOR = R.color.colorPrimary;
    private static final int DEFAULT_TAG_TEXT_COLOR = R.color.colorWhite;
    private static final int DEFAULT_DELETABLE_TEXT_COLOR = R.color.colorWhite;
    private static final String DEFAULT_DELETABLE_STRING = "X";
    private static final int TAG_LAYOUT_TOP_MERGIN = 10;
    private static final int TAG_LAYOUT_LEFT_MERGIN = 10;
    private static final int LAYOUT_WIDTH_OFFSET = 10;
    private static final int MIN_PADDING = 5;
    private static final int MORE_LAYOUT_LEFT_MERGIN = 20;
    /**
     * Properties
     */
    private int mTagBackGround;
    private int mTagFontColor;
    private int mDeleteFontColor;
    private boolean mCanDelete;
    private boolean mRoundedCorners;
    private float mTagFontSize;
    private float mDeleteFontSize;
    private int mNumberOfTags;
    private int mMaxLines;

    /** tag list */
    private List<Tag> mTags = new ArrayList<Tag>();
    private LayoutInflater mInflater;
    private Display mDisplay;
    private ViewTreeObserver mViewTreeObserber;
    private boolean mInitialized = false;

    private Context mContext;

    /** view size param */
    private int mWidth;
    private int mHeight;
    private int numberOfLines;
    private int tempMaxLines;
    private boolean exitBucle;


    private OnTagDeleteListener mDeleteListener;

    public CloudTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CloudTag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    public void add(Tag tag) {
        mTags.add(tag);
    }


    public int getNumberOfLines() {
        return numberOfLines;
    }


    public void setTagBackGround(int mTagBackGround) {
        this.mTagBackGround = mTagBackGround;
    }

    public void setTagFontColor(int mTagFontColor) {
        this.mTagFontColor = mTagFontColor;
    }

    public void setDeleteFontColor(int mDeleteFontColor) {
        this.mDeleteFontColor = mDeleteFontColor;
    }

    public void setCanDelete(boolean mCanDelete) {
        this.mCanDelete = mCanDelete;
    }

    public void setRoundedCorners(boolean mRoundedCorners) {
        this.mRoundedCorners = mRoundedCorners;
    }

    public void setTagFontSize(float mTagFontSize) {
        this.mTagFontSize = mTagFontSize;
    }

    public void setDeleteFontSize(float mDeleteFontSize) {
        this.mDeleteFontSize = mDeleteFontSize;
    }

    public void setNumberOfTags(int mNumberOfTags) {
        this.mNumberOfTags = mNumberOfTags;
    }

    public void setMaxLines(int mMaxLines) {
        this.mMaxLines = mMaxLines;
    }

    /**
     * onSizeChanged
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
    }

    /**
     * view width
     * @return layout width
     */
    public int width() {
        return mWidth;
    }

    /**
     * view height
     * @return int layout height
     */
    public int height() {
        return mHeight;
    }
    /**
     * initalize instance
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void initialize(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDisplay  = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mViewTreeObserber = getViewTreeObserver();
        mViewTreeObserber.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(!mInitialized) {
                    mInitialized = true;
                    drawTags();
                }
            }
        });

        // get AttributeSet
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.CloudTag, defStyle, defStyle);
        mTagBackGround = typeArray.getColor(
                R.styleable.CloudTag_tagBackGround, getResources().getColor(DEFAULT_TAG_LAYOUT_COLOR));
        mTagFontColor = typeArray.getColor(
                R.styleable.CloudTag_tagFontColor, getResources().getColor(DEFAULT_TAG_TEXT_COLOR));
        mDeleteFontColor = typeArray.getColor(
                R.styleable.CloudTag_tagFontColor, getResources().getColor(DEFAULT_DELETABLE_TEXT_COLOR));


        mMaxLines = typeArray.getInt(
                R.styleable.CloudTag_maxLines, 0);

        tempMaxLines = mMaxLines;
        mNumberOfTags = typeArray.getInt(
                R.styleable.CloudTag_numberOfTags, 0);
        mCanDelete = typeArray.getBoolean(
                R.styleable.CloudTag_canDelete, false);
        mRoundedCorners = typeArray.getBoolean(
                R.styleable.CloudTag_roundedCorners, true);

        mTagFontSize =  typeArray.getDimension(
                R.styleable.CloudTag_tagFontSize, DEFAULT_TEXT_SIZE);
        mDeleteFontSize =  typeArray.getDimension(
                R.styleable.CloudTag_deleteFontSize,DEFAULT_TEXT_SIZE);

    }

    public void remove(int position) {
        mTags.remove(position);
        tempMaxLines = mMaxLines;
        drawTags();
    }


    public void drawTags() {
        if(!mInitialized) {
            return;
        }

        // clear all tag
        removeAllViews();

        exitBucle = false;


        float total = getPaddingLeft() + getPaddingRight();

        boolean allElements = true;
        int lastElement = 0;
        if (mNumberOfTags == 0){
            allElements = true;
            lastElement = mTags.size();
        }else{
            if (mNumberOfTags < mTags.size()){
                allElements = false;
                lastElement = mNumberOfTags;
            }else{
                allElements = true;
                lastElement = mTags.size();
            }
        }

        numberOfLines = 1;
        int indexView = FIRST_INDEX; //First Id of the views
        int indexHeader = indexView;// The header tag of this line
        for (int i=0;i<lastElement;i++) {
            final int position = i;
            final Tag tag = mTags.get(i);
            GradientDrawable shape =  new GradientDrawable();
            if (mRoundedCorners) {
                shape.setCornerRadius(ROUNDED_RADIUS);
            }else{
                shape.setCornerRadius(0);
            }
            shape.setColor(mTagBackGround);

            View tagLayout = mInflater.inflate(R.layout.cloudtag_view, null);
            tagLayout.setId(indexView);
            tagLayout.setBackground(shape);


            // label text
            TextView tagView = (TextView) tagLayout.findViewById(R.id.tagLabel);
            tagView.setText(tag.getText());
            tagView.setPadding(VIEW_PADDING_INSIDE, VIEW_PADDING_INSIDE, VIEW_PADDING_INSIDE, VIEW_PADDING_INSIDE);
            tagView.setTextColor(mTagFontColor);
            tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTagFontSize);
            if(mCanDelete) {
                tagView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDeleteListener != null) {
                            Tag targetTag = tag;
                            mDeleteListener.onTagDeleted(targetTag, position);
                        }
                    }
                });
            }
            // calculate　of tag layout width
            float tagWidth = tagView.getPaint().measureText(tag.getText())
                    + VIEW_PADDING_INSIDE * 2;  // tagView padding (left & right)

            TextView deletableView = (TextView) tagLayout.findViewById(R.id.tagDelete);
            if(mCanDelete) {
                deletableView.setVisibility(View.VISIBLE);
                deletableView.setText(DEFAULT_DELETABLE_STRING);
                deletableView.setPadding(VIEW_PADDING_INSIDE, VIEW_PADDING_INSIDE, VIEW_PADDING_INSIDE, VIEW_PADDING_INSIDE);
                deletableView.setTextColor(mDeleteFontColor);
                deletableView.setTextSize(mDeleteFontSize);
                deletableView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mDeleteListener != null) {
                            Tag targetTag = tag;
                            mDeleteListener.onTagDeleted(targetTag, position);
                        }

                    }
                });
                tagWidth += deletableView.getPaint().measureText(DEFAULT_DELETABLE_STRING)
                        + VIEW_PADDING_INSIDE * 2; // deletableView Padding (left & right)
            } else {
                deletableView.setVisibility(View.GONE);
            }


            LayoutParams tagParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tagParams.setMargins(0, 0, 0, 0);


            if (mWidth <= total + tagWidth + LAYOUT_WIDTH_OFFSET) {
                // New Line
                if (tempMaxLines != 0){
                    // Ha escogido no mostrar todas las líneas
                    numberOfLines++;
                    if (numberOfLines > tempMaxLines){
                        // Salgo del bucle
                        exitBucle = true;
                        break;
                    }
                }
                tagParams.addRule(RelativeLayout.BELOW, indexHeader);
                tagParams.topMargin = TAG_LAYOUT_TOP_MERGIN;
                // initialize total param (layout padding left & layout padding right)
                total = getPaddingLeft() + getPaddingRight();
                indexHeader = indexView;
            } else {
                tagParams.addRule(RelativeLayout.ALIGN_TOP, indexHeader);
                tagParams.addRule(RelativeLayout.RIGHT_OF, indexView - 1);
                if (indexView != indexHeader) {
                    tagParams.leftMargin = TAG_LAYOUT_LEFT_MERGIN;
                    total += TAG_LAYOUT_LEFT_MERGIN;
                }
            }
            indexView++;
            total += tagWidth;
            tagLayout.setPadding(VIEW_PADDING_INSIDE,MIN_PADDING,VIEW_PADDING_INSIDE,MIN_PADDING);
            addView(tagLayout, tagParams);

        }

        if (mMaxLines != 0 && (numberOfLines > tempMaxLines)){
            drawShowMore(indexHeader);
        }


        if (!allElements){
            // No se muestran todos los tags, muestro el contador de los que faltan por ver

            String textCounter = "+" + (mTags.size() - mNumberOfTags);
            TextView textView = new TextView(mContext);
            textView.setText(textCounter);
            textView.setTextColor(mTagFontColor);
            textView.setTextSize(TEXT_SIZE_MORE_ELEMENTS);
            textView.setTypeface(null, Typeface.BOLD);
            float tagWidth = textView.getPaint().measureText(textCounter)
                    + VIEW_PADDING_INSIDE * 2;  // tagView padding (left & right)

            LayoutParams tagParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tagParams.setMargins(0, 0, 0, 0);


            if (mWidth <= total + tagWidth + LAYOUT_WIDTH_OFFSET) {
                // New Line

                tagParams.addRule(RelativeLayout.BELOW, indexHeader);
                tagParams.topMargin = TAG_LAYOUT_TOP_MERGIN;
                // initialize total param (layout padding left & layout padding right)
            } else {
                tagParams.addRule(RelativeLayout.CENTER_VERTICAL, indexHeader);
                tagParams.addRule(RelativeLayout.RIGHT_OF, indexView-1);
                if (indexView != indexHeader) {
                    tagParams.leftMargin = MORE_LAYOUT_LEFT_MERGIN;
                }
            }

            textView.setPadding(VIEW_PADDING_INSIDE,MIN_PADDING,VIEW_PADDING_INSIDE, MIN_PADDING);
            addView(textView, tagParams);

        }

    }

    private void drawShowMore(int indexHeader) {
        TextView mShowMore;
        mShowMore = new TextView(mContext);
        mShowMore.setTextColor(mTagBackGround);
        mShowMore.setTextSize(mTagFontSize);
        mShowMore.setTypeface(null, Typeface.BOLD);

        if (exitBucle) {
            // Si se ha salido del bucle, tiene que salir mostrar todo
            mShowMore.setText(mContext.getString(R.string.ver_todos).toUpperCase());
            mShowMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
            tempMaxLines = 0;
        }else {
            mShowMore.setText(mContext.getString(R.string.no_ver_todos).toUpperCase());
            mShowMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0);
            tempMaxLines = mMaxLines;
        }

        mShowMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawTags();
            }
        });

        LayoutParams tagParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tagParams.setMargins(0, 0, 0, 0);
        tagParams.addRule(RelativeLayout.BELOW, indexHeader);
        tagParams.topMargin = TAG_LAYOUT_TOP_MERGIN;
        tagParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        mShowMore.setPadding(VIEW_PADDING_INSIDE, MIN_PADDING, VIEW_PADDING_INSIDE, MIN_PADDING);
        addView(mShowMore, tagParams);


    }
    /**
     * setter for OnTagDeleteListener
     * @param deleteListener
     */
    public void setOnTagDeleteListener(OnTagDeleteListener deleteListener) {
        mDeleteListener = deleteListener;
    }


    /**
     * listener for tag delete
     */
    public interface OnTagDeleteListener {
        void onTagDeleted(Tag tag, int position);
    }


}
