package be.henallux.studycard.ui.deck;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentCardsDeckBinding;
import be.henallux.studycard.models.Card;
import be.henallux.studycard.models.Deck;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;
import be.henallux.studycard.ui.card.CardFragment;
import be.henallux.studycard.ui.card.StudyCardFragment;
import be.henallux.studycard.ui.home.HomeFragment;
import be.henallux.studycard.utils.Utils;

public class CardsDeckFragment extends Fragment {
    private static final String ARG_DECK_ID = "deck_id";
    private static final String ARG_DECK_NAME = "deck_name";
    private static final String IS_TO_STUDY_CARDS = "is_to_study_cards";

    private int id;
    private String deckName;
    private boolean isToStudyCards;

    FragmentCardsDeckBinding mFragmentCardsDeckBinding;
    CardsDeckViewModel mCardsDeckViewModel;
    CardsDeckAdapter mCardsDeckAdapter;
    private Handler mHandler;

    public static Bundle newArguments(Integer deck_id, String deck_name, Boolean is_to_study_cards) {
        Bundle args = new Bundle();
        args.putString(ARG_DECK_NAME, deck_name);
        args.putInt(ARG_DECK_ID, deck_id);
        args.putBoolean(IS_TO_STUDY_CARDS, is_to_study_cards);
        return args;
    }

    public CardsDeckFragment() {}


    private Runnable updateAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            mCardsDeckViewModel.getCardsByCategoryDeck(id, isToStudyCards);
            mCardsDeckViewModel.getCards().observe(getViewLifecycleOwner(), cardsList -> {
                if (isToStudyCards) {
                    mCardsDeckAdapter.setCardToStudy(cardsList);
                } else {
                    mCardsDeckAdapter.setCardAcquired(cardsList);
                    mFragmentCardsDeckBinding.toStudyText.setText(R.string.cards_acquired);
                }
                mCardsDeckAdapter.setDeckName(deckName);
                mFragmentCardsDeckBinding.progressBar.setVisibility(View.GONE);
                mFragmentCardsDeckBinding.recyclerView.setVisibility(View.VISIBLE);
            });
            mCardsDeckViewModel.getError().observe(getViewLifecycleOwner(), CardsDeckFragment.this::displayError);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentCardsDeckBinding = FragmentCardsDeckBinding.inflate(inflater, container, false);
        mCardsDeckViewModel = new ViewModelProvider(this).get(CardsDeckViewModel.class);
        mCardsDeckAdapter = new CardsDeckAdapter();

        assert getArguments() != null;
        id = getArguments().getInt(ARG_DECK_ID);
        deckName = getArguments().getString(ARG_DECK_NAME);
        isToStudyCards = getArguments().getBoolean(IS_TO_STUDY_CARDS);

        mCardsDeckAdapter.setOnItemClickListener((position, v) -> {
            assert mCardsDeckAdapter.getItem(position) != null;
            Bundle cardArgs = CardFragment.newArguments(mCardsDeckAdapter.getItem(position).id, deckName);
            Navigation.findNavController(v).navigate(R.id.action_CardsDeckFragment_to_CardFragment, cardArgs);
        });

        mFragmentCardsDeckBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFragmentCardsDeckBinding.recyclerView.setEmptyView(mFragmentCardsDeckBinding.emptyView);
        mFragmentCardsDeckBinding.recyclerView.setAdapter(mCardsDeckAdapter);

        Bundle cardsArgs = StudyCardFragment.newArguments(id, deckName, 0);
        mFragmentCardsDeckBinding.startButton.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(R.id.action_CardsDeckFragment_to_StudyCardFragment, cardsArgs));

        mHandler = new Handler();

        return mFragmentCardsDeckBinding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) requireActivity()).setToolBarTitle(deckName);
        super.onResume();
        updateAdapterRunnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(updateAdapterRunnable);
    }

    private void displayError(NetworkError error) {
        mFragmentCardsDeckBinding.progressBar.setVisibility(View.GONE);
        if (error == null) {
            mFragmentCardsDeckBinding.recyclerView.setVisibility(View.VISIBLE);
            mFragmentCardsDeckBinding.errorLayout.setVisibility(View.GONE);
            return;
        }
        mFragmentCardsDeckBinding.emptyView.setVisibility(error == NetworkError.NOT_FOUND ? View.VISIBLE : View.GONE);
        mFragmentCardsDeckBinding.startButton.setVisibility(error == NetworkError.NOT_FOUND ? View.VISIBLE : View.GONE);
        mFragmentCardsDeckBinding.errorLayout.setVisibility(View.VISIBLE);
        mFragmentCardsDeckBinding.recyclerView.setVisibility(View.GONE);
        mFragmentCardsDeckBinding.errorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(), getActivity().getTheme()));
        mFragmentCardsDeckBinding.errorText.setText(error.getErrorMessage());
    }

    private static class CardsDeckAdapter extends RecyclerView.Adapter<CardDeckViewHolder> {
        private List<Card> mCards;
        private static ClickListener clickListener;
        private String deckName;

        public void setCardToStudy(List<Card> cards) {
            this.mCards = cards;
            notifyDataSetChanged();
        }

        public void setCardAcquired(List<Card> cards) {
            this.mCards = cards;
            notifyDataSetChanged();
        }

        public void setDeckName(String deckName){
            this.deckName = deckName;
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public CardDeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_button_layout, parent, false);
            return new CardDeckViewHolder(view, clickListener);
        }

        @Override
        public void onBindViewHolder(CardDeckViewHolder holder, int position) {
            String textInitFrontCard = mCards.get(position).frontCard;
            String textFrontCard = Utils.getFrontCardTextToList(textInitFrontCard, position);
            holder.cardButton.setText(textFrontCard);

            Bundle cardArgs = CardFragment.newArguments(mCards.get(position).id, deckName);
            holder.cardButton.setOnClickListener(view -> {
                Navigation.findNavController(view).navigate(R.id.action_CardsDeckFragment_to_CardFragment, cardArgs);
            });
        }

        public Card getItem(int position) {
            return mCards != null ? mCards.get(position) : null;
        }

        @Override
        public int getItemCount() {
            return mCards == null ? 0 : mCards.size();
        }

        public void setOnItemClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public interface ClickListener {
            void onItemClick(int position, View v);
        }
    }

    private static class CardDeckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView cardTextView;
        protected Button cardButton;
        public CardsDeckAdapter.ClickListener clickListener;

        public CardDeckViewHolder(@NonNull View itemView, CardsDeckAdapter.ClickListener clickListener) {
            super(itemView);
            cardTextView = itemView.findViewById(R.id.to_study_text);
            cardButton = (Button) itemView.findViewById(R.id.card_button);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), itemView);
        }
    }
}