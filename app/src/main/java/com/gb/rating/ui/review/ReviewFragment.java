package com.gb.rating.ui.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gb.rating.MainActivity;
import com.gb.rating.R;
import com.gb.rating.models.SearchUtils;
import com.google.android.material.textfield.TextInputEditText;

public class ReviewFragment extends Fragment {
    //by lazy :)
    private ReviewSharedViewModel model;
    private ReviewSharedViewModel getSharedViewModel(){
        if (model == null) {
            if (getActivity() != null) {
                model = new ViewModelProvider(getActivity()).get(ReviewSharedViewModel.class);
                return model;
            } else return null;
        } else return model;
    }

    private View view;
    private RatingBar kitchenRating;
    private RatingBar serviceRating;
    private RatingBar ambianceRating;
    private TextInputEditText reviewText;
    private Button cancelButton;
    private Button sendButton;
    private Spinner cafeSpinner;
    private String[] cafeTypes = {SearchUtils.CAFE_TYPE, SearchUtils.BAR_TYPE, SearchUtils.RESTAURANT_TYPE, SearchUtils.FASTFOOD_TYPE};
    private String chosenCafeType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getSharedViewModel();
        view = inflater.inflate(R.layout.fragment_review,
                container, false);

        initViews();

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(),
                R.layout.spinner_item, cafeTypes);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        cafeSpinner.setAdapter(adapter);
        cafeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                CustomSpinnerAdapter.flag = true;
                chosenCafeType = cafeTypes[pos];
            }
        });

        cancelButton.setOnClickListener(v -> getActivity().onBackPressed());

        sendButton.setOnClickListener(v -> {
            float kitchenRat = kitchenRating.getRating();
            float serviceRat = serviceRating.getRating();
            float ambianceRat = ambianceRating.getRating();
            String reviewTextStr = reviewText.getText().toString();

            if (chosenCafeType != null & kitchenRat != 0 & serviceRat != 0 & ambianceRat != 0) {
                ReviewSendDialog reviewSendDialog = new ReviewSendDialog();
                if (getActivity() != null)
                    model.secondPageInfo(kitchenRat, serviceRat, ambianceRat, reviewTextStr, chosenCafeType);
                    ((MainActivity) getActivity()).navigateToHome(true);

                //проверка
                //System.out.println(kitchenRat + "\n" + serviceRat + "\n" + ambianceRat + "\n" + reviewTextStr + "\n" + chosenCafeType);
            }
        });

        return view;
    }

    private void initViews() {
        kitchenRating = view.findViewById(R.id.ratingBar_kitchen);
        serviceRating = view.findViewById(R.id.ratingBar_service);
        ambianceRating = view.findViewById(R.id.ratingBar_ambiance);
        reviewText = view.findViewById(R.id.edit_text_review);
        cancelButton = view.findViewById(R.id.cancel_button);
        sendButton = view.findViewById(R.id.send_button);
        cafeSpinner = view.findViewById(R.id.cafe_spinner);
    }

}
