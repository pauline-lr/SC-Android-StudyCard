package be.henallux.studycard.ui.card;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.henallux.studycard.databinding.FragmentCardBinding;
import be.henallux.studycard.models.Card;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;

public class CardFragment extends Fragment {
    private static final String ARG_CARD_ID = "card_id";
    private static final String ARG_DECK_NAME = "deck_name";

    private String deckName;

    FragmentCardBinding mFragmentCardBinding;
    CardViewModel mCardViewModel;

    public static Bundle newArguments(Integer card_id, String deck_name) {
        Bundle args = new Bundle();
        args.putString(ARG_DECK_NAME, deck_name);
        args.putInt(ARG_CARD_ID, card_id);
        return args;
    }

    public CardFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentCardBinding = FragmentCardBinding.inflate(inflater, container, false);
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);

        assert getArguments() != null;
        int id = getArguments().getInt(ARG_CARD_ID);
        deckName = getArguments().getString(ARG_DECK_NAME);

        mCardViewModel.getCardFromWeb(id);

        mCardViewModel.getError().observe(getViewLifecycleOwner(), this::displayErrorScreen);

        return mFragmentCardBinding.getRoot();
    }

    private void displayErrorScreen(NetworkError error) {
        if (error == null) {
            mFragmentCardBinding.errorLayout.setVisibility(View.GONE);
            Card card = mCardViewModel.getCard().getValue();
            assert card != null;
            mFragmentCardBinding.frontCardText.setText(card.frontCard);
            mFragmentCardBinding.backCardText.setText(card.backCard);
            return;
        }
        mFragmentCardBinding.errorLayout.setVisibility(View.VISIBLE);
        mFragmentCardBinding.frontCardText.setVisibility(View.GONE);
        mFragmentCardBinding.backCardText.setVisibility(View.GONE);
        mFragmentCardBinding.lineDrawable.setVisibility(View.GONE);
        mFragmentCardBinding.errorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(), getActivity().getTheme()));
        mFragmentCardBinding.errorText.setText(error.getErrorMessage());
    }

    @Override
    public void onResume() {
        ((MainActivity) requireActivity()).setToolBarTitle(deckName);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}