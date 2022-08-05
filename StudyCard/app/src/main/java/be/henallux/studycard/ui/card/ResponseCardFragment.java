package be.henallux.studycard.ui.card;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentResponseCardBinding;
import be.henallux.studycard.databinding.FragmentResponseCardBinding;
import be.henallux.studycard.models.Card;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.models.RevisionCategory;
import be.henallux.studycard.repositories.web.dto.RevisionCategoryDto;
import be.henallux.studycard.ui.MainActivity;
import be.henallux.studycard.ui.deck.DeckViewModel;
import be.henallux.studycard.ui.deck.RevisionDeckFragment;

public class ResponseCardFragment extends Fragment {
    private static final String ARG_DECK_ID = "deck_id";
    private static final String ARG_DECK_NAME = "deck_name";
    private static final String ARG_POSITION = "position";

    private int id;
    private String deckName;
    private int position;

    FragmentResponseCardBinding mFragmentResponseCardBinding;
    CardViewModel mCardViewModel;
    DeckViewModel mDeckViewModel;
    CardAdapter mCardAdapter;

    public static Bundle newArguments(Integer deck_id, String deck_name, Integer position) {
        Bundle args = new Bundle();
        args.putString(ARG_DECK_NAME, deck_name);
        args.putInt(ARG_DECK_ID, deck_id);
        args.putInt(ARG_POSITION, position);
        return args;
    }

    public ResponseCardFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentResponseCardBinding = FragmentResponseCardBinding.inflate(inflater, container, false);
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        mCardAdapter = new CardAdapter();

        assert getArguments() != null;
        id = getArguments().getInt(ARG_DECK_ID);
        deckName = getArguments().getString(ARG_DECK_NAME);
        position = getArguments().getInt(ARG_POSITION);

        mDeckViewModel.getAllCardsFromDeck(id);

        mCardViewModel.getCardFromWeb(id, position);

        mCardViewModel.getError().observe(getViewLifecycleOwner(), this::displayErrorScreen);

        Bundle deckArgs = RevisionDeckFragment.newArguments(id, deckName);
        mFragmentResponseCardBinding.leaveButton.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(R.id.action_ResponseCardFragment_to_RevisionDeckFragment, deckArgs));

        return mFragmentResponseCardBinding.getRoot();
    }

    private void displayErrorScreen(NetworkError error) {
        if (error == null) {
            mFragmentResponseCardBinding.cardsInformationsLayout.setVisibility(View.VISIBLE);
            //mFragmentResponseCardBinding.errorLayout.getRoot().setVisibility(View.GONE);
            Card card = mCardViewModel.getCard().getValue();
            assert card != null;
            mFragmentResponseCardBinding.frontCardText.setText(card.frontCard);
            mFragmentResponseCardBinding.backCardText.setText(card.backCard);
            mDeckViewModel.getCards().observe(getViewLifecycleOwner(), cardsList -> {
                position = cardsList.size() == (position + 1) ? 0 : position + 1;
            });

            Bundle cardsArgs = StudyCardFragment.newArguments(id, deckName, position);
            mFragmentResponseCardBinding.difficultyOrder0.setOnClickListener(view -> {
                Navigation.findNavController(view).navigate(R.id.action_ResponseCardFragment_to_StudyCardFragment, cardsArgs);
                mCardViewModel.updateCategoryOfCard(card.id, 1);
            });
            mFragmentResponseCardBinding.difficultyOrder1.setOnClickListener(view -> {
                Navigation.findNavController(view).navigate(R.id.action_ResponseCardFragment_to_StudyCardFragment, cardsArgs);
                mCardViewModel.updateCategoryOfCard(card.id, 2);
            });
            mFragmentResponseCardBinding.difficultyOrder2.setOnClickListener(view -> {
                Navigation.findNavController(view).navigate(R.id.action_ResponseCardFragment_to_StudyCardFragment, cardsArgs);
                mCardViewModel.updateCategoryOfCard(card.id, 3);
            });
            mFragmentResponseCardBinding.difficultyOrder3.setOnClickListener(view -> {
                Navigation.findNavController(view).navigate(R.id.action_ResponseCardFragment_to_StudyCardFragment, cardsArgs);
                mCardViewModel.updateCategoryOfCard(card.id, 4);
            });
            return;
        }
        /*mFragmentResponseCardBinding.errorLayout.getRoot().setVisibility(View.VISIBLE);
        mFragmentResponseCardBinding.cardsInformationsLayout.setVisibility(View.GONE);
        mFragmentResponseCardBinding.errorLayout.errorText.setText(error.getErrorMessage());
        mFragmentResponseCardBinding.errorLayout.errorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(),
                getActivity().getTheme()));
        mFragmentResponseCardBinding.errorLayout.floatingActionButton.setOnClickListener(view -> this.sendRequestGetCustomer());*/
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setToolBarTitle(deckName);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private static class CardAdapter extends RecyclerView.Adapter<ResponseCardViewHolder> {
        private Card mCard;

        public void setCard(Card card) {
            this.mCard = card;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ResponseCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_response_card, parent, false);
            return new ResponseCardViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ResponseCardViewHolder holder, int position) {
            holder.frontCardText.setText(mCard.frontCard);
            holder.backCardText.setText(mCard.backCard);
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private static class ResponseCardViewHolder extends RecyclerView.ViewHolder {
        protected TextView frontCardText;
        protected TextView backCardText;
        protected Button DifficultyOrder0Button;
        protected Button DifficultyOrder1Button;
        protected Button DifficultyOrder2Button;
        protected Button DifficultyOrder3Button;

        ResponseCardViewHolder(View itemView) {
            super(itemView);
            frontCardText = itemView.findViewById(R.id.front_card_text);
            backCardText = itemView.findViewById(R.id.back_card_text);
            DifficultyOrder0Button = itemView.findViewById(R.id.difficulty_order_0);
            DifficultyOrder1Button = itemView.findViewById(R.id.difficulty_order_1);
            DifficultyOrder2Button = itemView.findViewById(R.id.difficulty_order_2);
            DifficultyOrder3Button = itemView.findViewById(R.id.difficulty_order_3);
        }
    }
}