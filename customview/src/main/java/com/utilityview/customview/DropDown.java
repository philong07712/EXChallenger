package com.utilityview.customview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.customview.R;

import java.util.ArrayList;

public class DropDown extends CustomTextviewFonts implements View.OnClickListener {

    private ArrayList<String> options = new ArrayList<>();

    public DropDown(Context context) {
        super(context);
        initView();
    }

    public DropDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.setOnClickListener(this);
    }

    private PopupWindow popupWindowsort(Context context) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setWidth(this.getWidth());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.item_drop_down, R.id.text1,
                options);
        // the drop down list is a list view
        ListView listViewSort = new ListView(context);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        //remove border
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(8f);
        }
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white_corner));

        // set on item selected
        listViewSort.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = options.get(position);
            this.setText(selectedItem);
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, id, selectedItem);
            }

            popupWindow.dismiss();
        });

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the listview as popup content
        popupWindow.setContentView(listViewSort);

        return popupWindow;
    }

    @Override
    public void onClick(View v) {
        if (v == this) {
            PopupWindow window = popupWindowsort(v.getContext());
            window.showAsDropDown(v, 0, 0);
        }
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int pos, long rowId, String selectItem);
    }
}
