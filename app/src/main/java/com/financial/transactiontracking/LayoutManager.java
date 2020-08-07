package com.financial.transactiontracking;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.plaid.link.result.LinkAccount;

import java.util.HashMap;
import java.util.List;


public class LayoutManager {
    FinancialHomeActivity financialHomeActivity;
    LinearLayout parentLayout;
    ViewGroup childLayout;
    HashMap<String, Integer> idMap;
    HashMap<Integer, View> viewList;
    int idCounter;

    LayoutManager(LinearLayout parentLayout, FinancialHomeActivity financialHomeActivity) {
        this.financialHomeActivity = financialHomeActivity;
        this.parentLayout = parentLayout;
        this.idMap = new HashMap<String, Integer>();
        this.viewList = new HashMap<Integer, View>();
        idCounter = parentLayout.getId();
        idCounter++;
    }

    public void deleteLayout() {
        parentLayout.removeAllViews();
    }

    public void addView(View view, String id) {
        idMap.put(id, idCounter);
        viewList.put(idCounter, view);
        view.setId(idCounter);
        idCounter++;

        parentLayout.addView(view);

    }

    public void deleteView(String id) {
        View temp = viewList.get(idMap.get(id));
        parentLayout.removeView(temp);
    }

    public void deleteView(View view) {
        parentLayout.removeView(view);
    }

    public View getView(String id) {
        return viewList.get(idMap.get(id));
    }

    protected void createCardViews(List<FinancialAccount> accounts, String institutionName) {

        ViewGroup.LayoutParams containerLayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        TextView institutionNameTextView = new TextView(financialHomeActivity);
        institutionNameTextView.setText(institutionName);
        institutionNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        institutionNameTextView.setHeight(85);

        CardView cardView = new CardView(financialHomeActivity);
        int parentWidth = this.parentLayout.getWidth();
        System.out.println("Parent width: " + parentWidth);

        int width = (int) (parentWidth * 0.8);
        System.out.println("Institution Name Text View Height: " + institutionNameTextView.getHeight());
        ViewGroup.MarginLayoutParams cardViewMarginParams = new ViewGroup.MarginLayoutParams(
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardViewMarginParams.setMargins((parentWidth - cardViewMarginParams.width) / 2,30,0,0);
        cardView.requestLayout();

        cardView.setLayoutParams(cardViewMarginParams);
        cardView.setRadius(15);
        cardView.setMaxCardElevation(30);

        LinearLayout parentContainerLayout = new LinearLayout(financialHomeActivity);
        parentContainerLayout.setOrientation(LinearLayout.VERTICAL);

        parentContainerLayout.addView(institutionNameTextView);
        cardView.addView(parentContainerLayout);

        accounts.forEach((account) -> {
            LinearLayout containerLayout = new LinearLayout(financialHomeActivity);
            containerLayout.setLayoutParams(containerLayoutParams);

            TextView redBar = new TextView(financialHomeActivity);
            redBar.setBackgroundColor(ContextCompat.getColor(financialHomeActivity, R.color.colorMaroon));

            ViewGroup.MarginLayoutParams textMargin = new ViewGroup.MarginLayoutParams(10, 50);
            textMargin.setMargins(15, 15, 0, 5);
            redBar.setLayoutParams(textMargin);

            TextView accountName = new TextView(financialHomeActivity);
            accountName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            accountName.setText(account.accountName);

            ViewGroup.MarginLayoutParams accountMargin = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                    );
            accountMargin.setMargins(10,15, 0, 0);
            accountName.setLayoutParams(accountMargin);

            containerLayout.addView(redBar);
            containerLayout.addView(accountName);
            parentContainerLayout.addView(containerLayout);
        });

        parentLayout.addView(cardView);
    }

}
