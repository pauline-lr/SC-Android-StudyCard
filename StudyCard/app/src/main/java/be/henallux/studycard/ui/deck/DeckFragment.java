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
import be.henallux.studycard.models.Deck;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;
import be.henallux.studycard.ui.home.EmptyRecyclerView;
import be.henallux.studycard.ui.home.HomeFragment;
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
            mDeckViewModel.getError().observe(getViewLifecycleOwner(), DeckFragment.this::displayErrorScreen);
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentDeckBinding = FragmentDeckBinding.inflate(inflater, container, false);
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        mDeckAdapter = new DeckAdapter();

        id = getArguments().getInt(ARG_DECK_ID);
        deckName = getArguments().getString(ARG_DECK_NAME);

        mDeckViewModel.getError().observe(getViewLifecycleOwner(), this::displayErrorScreen);
        // mFragmentDeckBinding.deckButton.setOnClickListener(view -> sendRequestStudyDeck());

        EmptyRecyclerView recyclerView = mFragmentDeckBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setEmptyView(mFragmentDeckBinding.emptyView);
        recyclerView.setAdapter(mDeckAdapter);
        //mHandler = new Handler();

        mHandler = new Handler();

        return mFragmentDeckBinding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) requireActivity()).setToolBarTitle(deckName);
        
        super.onResume();
        updateAdapterRunnable.run();
    }




    /*private void sendRequestDeckExist(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String pseudo = sharedPreferences.getString("pseudo", "vide");
        mDeckViewModel.isDeckClientExistFromWeb(peudo, id);
        mFragmentDeckBinding.progressBarCards.setVisibility(View.VISIBLE);
        mFragmentDeckBinding.listAllCardsDeckRecyclerView.setVisibility(View.GONE);
    }*/

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(updateAdapterRunnable);
    }

  /*   private void sendRequestStudyDeck(){
       SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String pseudo = sharedPreferences.getString("pseudo", "vide");
        mDeckViewModel.getAllCardsFromDeck(id);
        mFragmentDeckBinding.progressBarCards.setVisibility(View.VISIBLE);
        mFragmentDeckBinding.listAllCardsDeckRecyclerView.setVisibility(View.GONE);
    }*/

    private void displayErrorScreen(NetworkError error) {
        mFragmentDeckBinding.progressBar.setVisibility(View.GONE);
        if (error == null) {
            mFragmentDeckBinding.recyclerView.setVisibility(View.VISIBLE);
            //mFragmentDeckBinding.errorLayout.getRoot().setVisibility(View.GONE);
            return;
        }
        //mFragmentDeckBinding.errorLayout.getRoot().setVisibility(View.VISIBLE);
        mFragmentDeckBinding.recyclerView.setVisibility(View.GONE);
       /* mFragmentDeckBinding.errorLayout.errorText.setText(error.getErrorMessage());
        mFragmentDeckBinding.errorLayout.errorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(),
                getActivity().getTheme()));
        mFragmentDeckBinding.errorLayout.floatingActionButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        mFragmentDeckBinding.errorLayout.floatingActionButton.setOnClickListener(view -> {
            mFragmentDeckBinding.errorLayout.getRoot().setVisibility(View.GONE);
            mFragmentDeckBinding.listAllCardsDeckRecyclerView.setVisibility(View.VISIBLE);
            this.sendRequestStudyDeck();
        });*/
    }

    private static class DeckAdapter extends RecyclerView.Adapter<DeckViewHolder> {
        private List<Card> mCards;

        public void setCard(List<Card> cards) {
            this.mCards = cards;
            notifyDataSetChanged();
        }

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





/*package be.henallux.studycard.ui.deck;

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
import be.henallux.studycard.models.Deck;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;

public class DeckFragment extends Fragment {
    private static final String ARG_DECK_ID = "deck_id";
    private static final String ARG_DECK_NAME = "deck_name";

    FragmentDeckBinding mFragmentDeckBinding;
    DeckViewModel mDeckViewModel;
    DeckAdapter mDeckAdapter;

    private int id;
    private String deckName;
    private Handler mHandler;
    private Runnable updateAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
            String pseudo = sharedPreferences.getString("pseudo", "vide");
            id = getArguments().getInt(ARG_DECK_ID);
            mDeckViewModel.getAllCardsFromDeck(id);
            mDeckViewModel.getCards().observe(getViewLifecycleOwner(), mDeckAdapter::setCard);
        }
    };


    public static Bundle newArguments(Deck deck) {
        Bundle args = new Bundle();
        args.putString(ARG_DECK_NAME, deck.deckName);
        args.putInt(ARG_DECK_ID, deck.id);
        return args;
    }


    public DeckFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentDeckBinding = FragmentDeckBinding.inflate(inflater, container, false);
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        mDeckAdapter = new DeckAdapter();

        if(getArguments() != null){
            id = getArguments().getInt(ARG_DECK_ID);
            deckName = getArguments().getString(ARG_DECK_NAME);
        }

        sendRequestGetCards();
        mDeckViewModel.getCards().observe(getViewLifecycleOwner(), cardList -> {
            mDeckAdapter.setCard(cardList);
            mFragmentDeckBinding.progressBarCards.setVisibility(View.GONE);
            mFragmentDeckBinding.listAllCardsDeckRecyclerView.setVisibility(View.VISIBLE);
        });

        mDeckViewModel.getError().observe(getViewLifecycleOwner(), this::displayError);

        
        /*EmptyRecyclerView recyclerView = mFragmentDeckBinding.listAllCardsDeckRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setEmptyView(binding.emptyView);
        recyclerView.setAdapter(mAdapter);*/
     /*   mHandler = new Handler();

        mFragmentDeckBinding.listAllCardsDeckRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        mFragmentDeckBinding.listAllCardsDeckRecyclerView.setAdapter(mDeckAdapter);


        return mFragmentDeckBinding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) requireActivity()).setToolBarTitle(deckName);
        super.onResume();
    }

    private void sendRequestGetCards() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String pseudo = sharedPreferences.getString("pseudo", "vide");
        mDeckViewModel.getAllCardsFromDeck(id);
        mFragmentDeckBinding.progressBarCards.setVisibility(View.VISIBLE);
        mFragmentDeckBinding.listAllCardsDeckRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(updateAdapterRunnable);
    }

    private void displayError(NetworkError error) {
        mFragmentDeckBinding.progressBarCards.setVisibility(View.GONE);
        if (error == null) {
            mFragmentDeckBinding.listAllCardsDeckRecyclerView.setVisibility(View.VISIBLE);
            //binding.errorLayout.getRoot().setVisibility(View.GONE);
            return;
        }
        //binding.errorLayout.getRoot().setVisibility(View.VISIBLE);
        mFragmentDeckBinding.listAllCardsDeckRecyclerView.setVisibility(View.GONE);
        //binding.errorLayout.errorText.setText(error.getErrorMessage());
        //binding.errorLayout.floatingActionButton.setOnClickListener(view -> this.sendRequestGetDecks());
    }

    private static class DeckAdapter extends RecyclerView.Adapter<DeckViewHolder> {
        private List<Card> mCards;

        public void setCard(List<Card> cards) {
            this.mCards = cards;
            notifyDataSetChanged();
        }

        @Override
        public DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_layout, parent, false);

            return new DeckViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DeckViewHolder holder, int position) {
            String textFrontCard = mCards.get(position).frontCard;
            textFrontCard = textFrontCard.substring(0, Math.min(textFrontCard.length(), 30));
            if(textFrontCard.length() > 30)
                textFrontCard = textFrontCard + "...";
            textFrontCard = (position + 1) + " - " + textFrontCard;
            holder.mFrontCard.setText(textFrontCard);
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
}*/