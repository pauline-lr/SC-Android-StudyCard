package be.henallux.studycard.ui.deck;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentRevisionDeckBinding;
import be.henallux.studycard.models.Card;
import be.henallux.studycard.ui.MainActivity;

public class RevisionDeckFragment extends Fragment {
    private static final String ARG_DECK_ID = "deck_id";
    private static final String ARG_DECK_NAME = "deck_name";

    private int id;
    private String deckName;


    FragmentRevisionDeckBinding mFragmentRevisionDeckBinding;
    RevisionDeckViewModel mRevisionDeckViewModel;
    RevisionDeckAdapter mRevisionDeckAdapter;


    public static Bundle newArguments(Integer deck_id, String deck_name) {
        Bundle args = new Bundle();
        args.putString(ARG_DECK_NAME, deck_name);
        args.putInt(ARG_DECK_ID, deck_id);
        return args;
    }

    public RevisionDeckFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentRevisionDeckBinding = FragmentRevisionDeckBinding.inflate(inflater, container, false);
        mRevisionDeckViewModel = new ViewModelProvider(this).get(RevisionDeckViewModel.class);

        mRevisionDeckAdapter = new RevisionDeckAdapter();

        assert getArguments() != null;
        id = getArguments().getInt(ARG_DECK_ID);
        deckName = getArguments().getString(ARG_DECK_NAME);

        // mRevisionDeckViewModel.getError().observe(getViewLifecycleOwner(), this::displayErrorScreen);
        // mFragmentRevisionDeckBinding.revisionDeckButton.setOnClickListener(view -> sendRequestStudyRevisionDeck());
        mFragmentRevisionDeckBinding.revisedDeckText.setVisibility(View.GONE);
        mFragmentRevisionDeckBinding.startButton.setVisibility(View.VISIBLE);

        mRevisionDeckViewModel.getAllCardsToStudyRevisionDeck(id);
        mRevisionDeckViewModel.getAllCardsAcquiredRevisionDeck(id);

        AtomicInteger nbCardsToStudyAto = new AtomicInteger();
        mRevisionDeckViewModel.getCardsToStudy().observe(getViewLifecycleOwner(), cards -> {
                    if (cards.size() == 0) {
                        mFragmentRevisionDeckBinding.revisedDeckText.setVisibility(View.VISIBLE);
                        mFragmentRevisionDeckBinding.toStudyButton.setEnabled(false);
                        mFragmentRevisionDeckBinding.startButton.setText("Ré-étudier le paquet");
                        mFragmentRevisionDeckBinding.revisedDeckText.setText("Félicitations, vous avez terminé d'étudier ce paquet !");
                    } else {
                        mRevisionDeckAdapter.setCardToStudy(cards);
                        nbCardsToStudyAto.set(cards.size());
                        mFragmentRevisionDeckBinding.nbCardsToStudy.setText(String.valueOf(nbCardsToStudyAto));
                    }
                }
        );

        AtomicInteger nbCardsAcquiredAto = new AtomicInteger();

        mRevisionDeckViewModel.getCardsAcquired().observe(getViewLifecycleOwner(), cards -> {
                    mRevisionDeckAdapter.setCardAcquired(cards);
                    nbCardsAcquiredAto.set(cards.size());
                    mFragmentRevisionDeckBinding.nbCardsAcquired.setText(String.valueOf(nbCardsAcquiredAto));
                }
        );

        return mFragmentRevisionDeckBinding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setToolBarTitle(deckName);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private static class RevisionDeckAdapter extends RecyclerView.Adapter<RevisionDeckViewHolder> {
        private List<Card> mCards;


        public void setCardToStudy(List<Card> cards) {
            this.mCards = cards;
            notifyDataSetChanged();
        }

        public void setCardAcquired(List<Card> cards) {
            this.mCards = cards;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RevisionDeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);


            return new RevisionDeckViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RevisionDeckViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mCards == null ? 0 : mCards.size();
        }


    }

    private static class RevisionDeckViewHolder extends RecyclerView.ViewHolder {
        protected TextView nbCardsToStudy;
        protected TextView nbCardsAcquired;
        protected TextView revisedDeckText;

        RevisionDeckViewHolder(View itemView) {
            super(itemView);
            nbCardsToStudy = itemView.findViewById(R.id.nb_cards_to_study);
            nbCardsAcquired = itemView.findViewById(R.id.nb_cards_acquired);
            revisedDeckText = itemView.findViewById(R.id.revised_deck_text);
        }
    }
}