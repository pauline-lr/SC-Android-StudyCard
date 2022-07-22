package be.henallux.studycard.ui.deck;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentDeckBinding;
import be.henallux.studycard.models.Card;
import be.henallux.studycard.ui.MainActivity;

public class DeckFragment extends Fragment {
    private static final String ARG_DECK_ID = "deck_id";
    FragmentDeckBinding mFragmentDeckBinding;
    DeckViewModel mDeckViewModel;
    DeckAdapter mDeckAdapter;

    private int id;
    private Handler mHandler;
    private Runnable updateAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
            String pseudo = sharedPreferences.getString("pseudo", "vide");
            mDeckViewModel.getAllCardsFromDeck(id);
            mDeckViewModel.getCards().observe(getViewLifecycleOwner(), mDeckAdapter::setCard);
        }
    };
    
    public DeckFragment() {}

    static Bundle newArguments(Integer deck_id) {
        Bundle args = new Bundle();
        args.putInt(ARG_DECK_ID, deck_id);

        return args;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentDeckBinding = FragmentDeckBinding.inflate(inflater, container, false);
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        mDeckAdapter = new DeckAdapter();

        id = getArguments().getInt(ARG_DECK_ID);
        
        /*EmptyRecyclerView recyclerView = mFragmentDeckBinding.listAllCardsDeckRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setEmptyView(binding.emptyView);
        recyclerView.setAdapter(mAdapter);*/
        mHandler = new Handler();
        
        return mFragmentDeckBinding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setToolBarTitle(getString(R.string.deck_name));
        super.onResume();
        updateAdapterRunnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(updateAdapterRunnable);
    }

    private static class DeckAdapter extends RecyclerView.Adapter<DeckFragment.DeckViewHolder> {
        private List<Card> mCards;
        
        public void setCard(List<Card> cards) {
            this.mCards = cards;
            notifyDataSetChanged();
        }

        @Override
        public DeckFragment.DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_layout, parent, false);

            return new DeckFragment.DeckViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DeckFragment.DeckViewHolder holder, int position) {
            holder.mFrontCard.setText(mCards.get(position).frontCard);
        }

        @Override
        public int getItemCount() {
            return mCards == null ? 0 : mCards.size();
        }

        public Card getItem(int position) {
            return mCards != null ? mCards.get(position) : null;
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