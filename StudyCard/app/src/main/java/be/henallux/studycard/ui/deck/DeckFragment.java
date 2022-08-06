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
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentDeckBinding;
import be.henallux.studycard.models.Card;
import be.henallux.studycard.models.Deck;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;
import be.henallux.studycard.ui.EmptyRecyclerView;
import be.henallux.studycard.utils.Utils;

public class DeckFragment extends Fragment {
    private static final String ARG_DECK_ID = "deck_id";
    private static final String ARG_DECK_NAME = "deck_name";

    private int id;
    private String deckName;

    FragmentDeckBinding mFragmentDeckBinding;
    DeckViewModel mDeckViewModel;
    DeckAdapter mDeckAdapter;
    private Handler mHandler;

    public static Bundle newArguments(Deck deck) {
        Bundle args = new Bundle();
        args.putString(ARG_DECK_NAME, deck.deckName);
        args.putInt(ARG_DECK_ID, deck.id);
        return args;
    }

    public DeckFragment() {
    }

    private Runnable updateAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            mDeckViewModel.getAllCardsFromDeck(id);

            mDeckViewModel.getCards().observe(getViewLifecycleOwner(), cardsList -> {
                mDeckAdapter.setCard(cardsList);
                mDeckViewModel.getCards().observe(getViewLifecycleOwner(), mDeckAdapter::setCard);
                mFragmentDeckBinding.progressBar.setVisibility(View.GONE);
                mFragmentDeckBinding.recyclerView.setVisibility(View.VISIBLE);
                if(cardsList.size() == 0){
                    mFragmentDeckBinding.deckButton.setVisibility(View.GONE);
                }else{
                    Bundle deckArgs = RevisionDeckFragment.newArguments(id, deckName);
                    mFragmentDeckBinding.deckButton.setVisibility(View.VISIBLE);
                    mFragmentDeckBinding.deckButton.setEnabled(true);
                    mFragmentDeckBinding.deckButton.setOnClickListener(view -> Navigation.findNavController(view)
                            .navigate(R.id.action_deckFragment_to_revsionDeckFragment, deckArgs));
                }

            });
            mDeckViewModel.getError().observe(getViewLifecycleOwner(), DeckFragment.this::displayError);
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentDeckBinding = FragmentDeckBinding.inflate(inflater, container, false);
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        mDeckAdapter = new DeckAdapter();

        assert getArguments() != null;
        id = getArguments().getInt(ARG_DECK_ID);
        deckName = getArguments().getString(ARG_DECK_NAME);

        mDeckViewModel.getError().observe(getViewLifecycleOwner(), this::displayError);

        EmptyRecyclerView recyclerView = mFragmentDeckBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setEmptyView(mFragmentDeckBinding.emptyView);
        recyclerView.setAdapter(mDeckAdapter);

        mHandler = new Handler();

        return mFragmentDeckBinding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) requireActivity()).setToolBarTitle(deckName);
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        super.onResume();
        updateAdapterRunnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(updateAdapterRunnable);
    }

    private void displayError(NetworkError error) {
        mFragmentDeckBinding.progressBar.setVisibility(View.GONE);
        if (error == null) {
            mFragmentDeckBinding.recyclerView.setVisibility(View.VISIBLE);
            mFragmentDeckBinding.errorLayout.setVisibility(View.GONE);
            return;
        }
        mFragmentDeckBinding.emptyView.setVisibility(error == NetworkError.NOT_FOUND ? View.VISIBLE : View.GONE);
        mFragmentDeckBinding.deckButton.setVisibility(error == NetworkError.NOT_FOUND ? View.VISIBLE : View.GONE);
        mFragmentDeckBinding.errorLayout.setVisibility(View.VISIBLE);
        mFragmentDeckBinding.recyclerView.setVisibility(View.GONE);
        mFragmentDeckBinding.errorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(), getActivity().getTheme()));
        mFragmentDeckBinding.errorText.setText(error.getErrorMessage());
    }

    private static class DeckAdapter extends RecyclerView.Adapter<DeckViewHolder> {
        private List<Card> mCards;

        public void setCard(List<Card> cards) {
            this.mCards = cards;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_layout, parent, false);

            return new DeckViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DeckViewHolder holder, int position) {
            String textInitFrontCard = mCards.get(position).frontCard;
            String textFrontCard = Utils.getFrontCardTextToList(textInitFrontCard, position);
            holder.mFrontCard.setText(textFrontCard);
        }

        @Override
        public int getItemCount() {
            return mCards == null ? 0 : mCards.size();
        }
    }

    private static class DeckViewHolder extends RecyclerView.ViewHolder {
        protected TextView mFrontCard;

        DeckViewHolder(View itemView) {
            super(itemView);
            mFrontCard = itemView.findViewById(R.id.card_text_view);
        }
    }
}