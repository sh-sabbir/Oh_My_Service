package com.codeian.ohmyservice.customer.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.codeian.ohmyservice.R;
import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import static android.graphics.Typeface.BOLD;

public class HelpFragment extends Fragment {

    ViewGroup dynamicLayoutContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_help, container, false);

        this.dynamicLayoutContainer = root.findViewById(R.id.dynamicLayoutContainer);

        init();
        return root;
    }

    private void init() {
        String ques,ans;

        String q[] = getResources().getStringArray(R.array.help_title);
        String a[] = getResources().getStringArray(R.array.help_details);

        final ExpansionLayoutCollection expansionLayoutCollection = new ExpansionLayoutCollection();

        for (int i = 0; i < q.length; i++) {

            ques = q[i];
            ans = a[i];

            ExpansionLayout faq = addDynamicLayout(ques,ans);
            expansionLayoutCollection.add(faq);
        }
        expansionLayoutCollection.openOnlyOne(false);
    }


    public ExpansionLayout addDynamicLayout(String q, String a) {

        final ExpansionHeader expansionHeader = createExpansionHeader(q);
        dynamicLayoutContainer.addView(expansionHeader, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final ExpansionLayout expansionLayout = createExpansionLayout(a);
        dynamicLayoutContainer.addView(expansionLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        expansionHeader.setExpansionLayout(expansionLayout);

        return expansionLayout;

    }

    @NonNull
    private ExpansionLayout createExpansionLayout(String answer) {
        final ExpansionLayout expansionLayout = new ExpansionLayout(getContext());

        final LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        expansionLayout.addView(layout, ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(getContext(), 48)); //equivalent to addView(linearLayout)

        final TextView text = new TextView(getContext());
        text.setText(answer);
        text.setGravity(Gravity.START);
        text.setTextColor(Color.parseColor("#3E3E3E"));
        text.setBackgroundColor(Color.parseColor("#EEEEEE"));
        text.setPadding(dpToPx(getContext(), 16), dpToPx(getContext(), 8), dpToPx(getContext(), 16), dpToPx(getContext(), 16));
        layout.addView(text, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        //layout.addView(LayoutInflater.from(this).inflate(R.layout.item_cell, layout, false));

        return expansionLayout;
    }

    @NonNull
    private ExpansionHeader createExpansionHeader(String question) {
        final ExpansionHeader expansionHeader = new ExpansionHeader(getContext());
        expansionHeader.setBackgroundColor(Color.WHITE);

        expansionHeader.setPadding(dpToPx(getContext(), 16), dpToPx(getContext(), 8), dpToPx(getContext(), 16), dpToPx(getContext(), 8));

        final RelativeLayout layout = new RelativeLayout(getContext());
        expansionHeader.addView(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //equivalent to addView(linearLayout)

        //image
        final ImageView expansionIndicator = new AppCompatImageView(getContext());
        expansionIndicator.setImageResource(R.drawable.ic_expansion_header_indicator_grey_24dp);
        final RelativeLayout.LayoutParams imageLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layout.addView(expansionIndicator, imageLayoutParams);

        //label
        final TextView text = new TextView(getContext());
        text.setText(question);
        text.setTypeface(null,BOLD);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        text.setTextColor(Color.parseColor("#3E3E3E"));

        final RelativeLayout.LayoutParams textLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        textLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        layout.addView(text, textLayoutParams);

        expansionHeader.setExpansionHeaderIndicator(expansionIndicator);
        return expansionHeader;
    }

    public static int dpToPx(Context context, float dp) {
        return (int) (dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float pxToDp(Context context, float px) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}