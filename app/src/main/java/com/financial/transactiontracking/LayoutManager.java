package com.financial.transactiontracking;

import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
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
        this.idMap = new HashMap<>();
        this.viewList = new HashMap<>();
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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        LinearLayout institutionLinearLayout = new LinearLayout(financialHomeActivity);
        institutionLinearLayout.setOrientation(LinearLayout.VERTICAL);
        institutionLinearLayout.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textViewParams.gravity = Gravity.CENTER;

        TextView institutionView = new TextView(financialHomeActivity);
        institutionView.setText(institutionName);
        institutionView.setTextSize(40);
        institutionView.setTextColor(ContextCompat.getColor(financialHomeActivity, R.color.colorFadedGray));
        institutionView.setLayoutParams(layoutParams);

        GradientDrawable dividerLineShape = new GradientDrawable();
        dividerLineShape.setShape(GradientDrawable.RECTANGLE);
        dividerLineShape.setColor(ContextCompat.getColor(financialHomeActivity, R.color.colorFadedGray));
        dividerLineShape.setCornerRadius(5);

        LinearLayout.LayoutParams dividerLayoutParams = new LinearLayout.LayoutParams(
                (int) (this.parentLayout.getWidth() * 0.8),
                7
        );
        dividerLayoutParams.gravity = Gravity.CENTER;
        dividerLayoutParams.setMargins(0, 0, 0, 25);

        View dividerLine = new View(financialHomeActivity);
        dividerLine.setMinimumHeight(7);
        dividerLine.setMinimumWidth((int) (this.parentLayout.getWidth() * 0.8));
        dividerLine.setLayoutParams(dividerLayoutParams);
        dividerLine.setBackground(dividerLineShape);

        institutionLinearLayout.addView(institutionView);
        institutionLinearLayout.addView(dividerLine);
        this.parentLayout.addView(institutionLinearLayout);


        /*
         Each account CardView contains 3 LinearLayouts.
            1. containerLayout contains the red bar and the other 2 Horizontally
                a) From left to right,
                    i)   redBar
                    ii)  infoLayout
                    iii) balanceLayout
            2. infoLayout contains the Account Name and Number Vertically.
                a) From top to bottom
                    i)  accountNumber
                    ii) accountName
            3. balanceLayout contains the Available and Total balance Vertically.
                a) From top to bottom
                    i)  availableBalance
                    ii) totalBalance
         */
        for (FinancialAccount account : accounts) {
            System.out.println("Creating CardView for " + account.accountName);
            int cardViewWidth = (int) (this.parentLayout.getWidth() * 0.8);
            LinearLayout.LayoutParams cardViewLayoutParam = new LinearLayout.LayoutParams(
                    cardViewWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardViewLayoutParam.gravity = Gravity.CENTER;
            cardViewLayoutParam.setMargins(0, 0, 0, 20);

            CardView cardView = new CardView(financialHomeActivity);
            cardView.setElevation(20);
            cardView.setLayoutParams(cardViewLayoutParam);
            cardView.setRadius(15);

            //Set-up containerLayout which contains the redBar, infoLayout, and balanceLayout
            LinearLayout.LayoutParams containerLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            LinearLayout containerLayout = new LinearLayout(financialHomeActivity);
            containerLayout.setLayoutParams(containerLayoutParams);



            //Set-up red bar
            ViewGroup.MarginLayoutParams redBarLayoutParam = new ViewGroup.MarginLayoutParams(
                    20,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );

            TextView redBar = new TextView(financialHomeActivity);
            redBar.setBackgroundColor(ContextCompat.getColor(financialHomeActivity, R.color.colorMaroon));
            redBar.setLayoutParams(redBarLayoutParam);

            //Set-up infoLayout which contains the account number and name
            LinearLayout.LayoutParams infoLayoutParams = new LinearLayout.LayoutParams(
                    (int) (cardViewWidth * 0.6),
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            LinearLayout infoLayout = new LinearLayout(financialHomeActivity);
            infoLayout.setOrientation(LinearLayout.VERTICAL);
            infoLayout.setLayoutParams(infoLayoutParams);


            //Set-up account name
            TextView accountName = new TextView(financialHomeActivity);
            accountName.setTextSize(25);
            accountName.setText(account.accountName);

            LinearLayout.LayoutParams accountMargin = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                    );
            accountMargin.setMargins(10, 0, 0, 0);
            accountName.setLayoutParams(accountMargin);

            //Set-up account number
            TextView accountNumber = new TextView(financialHomeActivity);
            accountNumber.setText(account.accountNumber);
            accountNumber.setTextSize(15);
            accountNumber.setTextColor(ContextCompat.getColor(financialHomeActivity, R.color.colorDarkFadedGray));
            accountNumber.setLayoutParams(accountMargin);

            //Set-up balanceLayout which contains the accountAvailableBalance and accountTotalBalance
            LinearLayout.LayoutParams balanceLayoutParams = new LinearLayout.LayoutParams (
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            LinearLayout balanceLayout = new LinearLayout(financialHomeActivity);
            balanceLayout.setOrientation(LinearLayout.VERTICAL);
            balanceLayout.setLayoutParams(balanceLayoutParams);


            //Set up account balance
            TextView accountAvailableBalance = new TextView(financialHomeActivity);
            String stringAvailableBalance = "$" + account.balance.availableBalance + "0";
            accountAvailableBalance.setText(stringAvailableBalance);
            accountAvailableBalance.setTextSize(20);

            ViewGroup.MarginLayoutParams balanceMargin = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            accountAvailableBalance.setLayoutParams(balanceMargin);
            accountAvailableBalance.setGravity(Gravity.END);

            TextView accountTotalBalance = new TextView(financialHomeActivity);
            String stringTotalBalance = "$" + account.balance.currentBalance + "0";
            accountTotalBalance.setText(stringTotalBalance);
            accountTotalBalance.setTextSize(15);
            accountTotalBalance.setLayoutParams(balanceMargin);
            accountTotalBalance.setGravity(Gravity.END);

            //Add views
            infoLayout.addView(accountNumber);
            infoLayout.addView(accountName);

            balanceLayout.addView(accountTotalBalance);
            balanceLayout.addView(accountAvailableBalance);

            containerLayout.addView(redBar);
            containerLayout.addView(infoLayout);
            containerLayout.addView(balanceLayout);

            cardView.addView(containerLayout);
            this.parentLayout.addView(cardView);
        }
    }

}
