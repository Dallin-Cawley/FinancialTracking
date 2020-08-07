package com.financial.transactiontracking;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.plaid.link.Plaid;
import com.plaid.link.configuration.LinkConfiguration;
import com.plaid.link.configuration.PlaidEnvironment;
import com.plaid.link.configuration.PlaidProduct;
import com.plaid.link.result.PlaidLinkResultHandler;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

public class FinancialHomeActivity extends AppCompatActivity {
    LinearLayout parentLinearLayout;
    FloatingActionButton addInstitution;
    LayoutManager layoutManager;
    private User loggedUser;

    private PlaidLinkResultHandler myPlaidResultHandler = new PlaidLinkResultHandler(
            linkSuccess -> {
                System.out.println("Successfully created Item");
                try {
                    Item newItem = new Item(linkSuccess);
                    layoutManager.createCardViews(newItem.accounts, newItem.institutionName);
                    PlaidHandler.getPlaidHandlerIntstance().executeTask(new HomeSocketRunnable(linkSuccess.publicToken, newItem), this);
                    loggedUser.addInstitution(newItem);
                    UserSerializationManagement.saveUser(FinancialHomeActivity.this, loggedUser);
                    PlaidHandler.getPlaidHandlerIntstance().executeTask(new PlaidAPIRunnable(new PlaidAPIConnection()));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }

                return Unit.INSTANCE;
            },
            linkExit -> {
                return Unit.INSTANCE;
            }
    );


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.financial_home);
        parentLinearLayout = findViewById(R.id.parent_linear_layout);
        addInstitution = findViewById(R.id.floating_action_buton);

        layoutManager = new LayoutManager(parentLinearLayout, FinancialHomeActivity.this);
        Intent intent = getIntent();

        if (intent != null) {
            loggedUser = (User) intent.getExtras().getSerializable("user");

        }
        else {
            System.out.println("Intent bundle is null");
        }

        addInstitution.setOnClickListener(view -> {
            ArrayList<PlaidProduct> products = new ArrayList<PlaidProduct>();
            products.add(PlaidProduct.TRANSACTIONS);

            Plaid.openLink(FinancialHomeActivity.this, new LinkConfiguration.Builder()
                    .clientName("Transaction Tracker")
                    .products(products)
                    .environment(PlaidEnvironment.SANDBOX)
                    .publicKey(getString(R.string.plaid_sandbox_api_public_key)).build());
        });

        if (loggedUser.getInstitutions().size() > 0) {
            ViewTreeObserver vto = parentLinearLayout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    parentLinearLayout.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);

                    List<Item> institutions = loggedUser.getInstitutions();
                    int institutionsSize = institutions.size();
                    for (int i = 0; i < institutionsSize; i++) {
                        layoutManager.createCardViews(institutions.get(i).accounts, institutions.get(i).institutionName);
                    }

                    PlaidHandler.getPlaidHandlerIntstance().executeTask(new PlaidAPIRunnable(new PlaidAPIConnection()));

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!myPlaidResultHandler.onActivityResult(requestCode, resultCode, data)) {
            Log.i(MainActivity.class.getSimpleName(), "Not handled");
        }
    }

    public void addItem(Item item){
        loggedUser.addInstitution(item);
        System.out.println("Item Access Token: " + item.getAccessToken());
    }

    public void printPlaidResult(String result) {
        System.out.println("Plaid Result:\n" + result);
    }
}
