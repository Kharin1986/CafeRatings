package com.gb.rating.ui.review;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.gb.rating.MainActivity;
import com.gb.rating.R;

public class ReviewSendDialog extends DialogFragment {

    private ReviewSendViewModel model;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.review_send_dialog, null);

        model = new ViewModelProvider(this).get(ReviewSendViewModel.class);

        Button okButton = dialogView.findViewById(R.id.ok_button);
        okButton.setOnClickListener(v -> {
            //TODO save rating
            dismiss();
            ((MainActivity) getActivity()).getNavController().navigate(R.id.navigation_home);
//            Intent parentActivityIntent = new Intent(getContext(), MainActivity.class);
//            parentActivityIntent.addFlags(
//                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                            Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(parentActivityIntent);
        });
        builder.setView(dialogView);
        return builder.create();
    }
}
