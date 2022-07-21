package be.henallux.studycard.ui.decks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentDecksBinding;
import be.henallux.studycard.models.Deck;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;


public class DecksFragment extends Fragment {
    private FragmentDecksBinding mFragmentDecksBinding;
    private Handler mHandler;
    DecksViewModel mDecksViewModel;
    DeckAdapter mDeckAdapter;
    
    public DecksFragment(){}
    
    private Runnable updateAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            sendRequestGetdecks();
            mDecksViewModel.getDecks().observe(getViewLifecycleOwner(), deckList -> {
                mDeckAdapter.setDecks(deckList);
                mFragmentDecksBinding.progressBar.setVisibility(View.GONE);
                //mFragmentDecksBinding.recyclerView.setVisibility(View.VISIBLE);
            });
            mDecksViewModel.getError().observe(getViewLifecycleOwner(), DecksFragment.this::displayErrorScreen);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mFragmentDecksBinding = FragmentDecksBinding.inflate(inflater, container, false);
        mDecksViewModel = new ViewModelProvider(this).get(DecksViewModel.class);
        mDeckAdapter = new DeckAdapter();

        mDeckAdapter.setOnItemClickListener((position, v) -> {
            /*Bundle searchSportHallArgs = SearchDeckFragment.newArguments(mDeckAdapter.getItem(position));
            Navigation.findNavController(v)
                    .navigate(R.id.action_decksFragment_to_searchDeckFragment, searchSportHallArgs);*/
        });

        /*mFragmentDecksBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFragmentDecksBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mFragmentDecksBinding.recyclerView.setEmptyView(mFragmentDecksBinding.emptyView);
        mFragmentDecksBinding.recyclerView.setAdapter(mDeckAdapter);*/
        mHandler = new Handler();

        return mFragmentDecksBinding.getRoot();
    }
    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setToolBarTitle(getString(R.string.decks));
        super.onResume();
        updateAdapterRunnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(updateAdapterRunnable);
    }

    private void sendRequestGetdecks(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String pseudo = sharedPreferences.getString("pseudo", "vide");
        mDecksViewModel.getAllDecksFromUser(pseudo);
        mFragmentDecksBinding.progressBar.setVisibility(View.VISIBLE);
        //mFragmentDecksBinding.recyclerView.setVisibility(View.GONE);

    }

    private void displayErrorScreen(NetworkError error) {
        mFragmentDecksBinding.progressBar.setVisibility(View.GONE);
        if (error == null) {
           /* mFragmentDecksBinding.recyclerView.setVisibility(View.VISIBLE);
            mFragmentDecksBinding.errorLayout.getRoot().setVisibility(View.GONE);*/
            return;
        }
       /* mFragmentDecksBinding.errorLayout.getRoot().setVisibility(View.VISIBLE);
        mFragmentDecksBinding.recyclerView.setVisibility(View.GONE);
        mFragmentDecksBinding.errorLayout.errorText.setText(error.getErrorMessage());
        mFragmentDecksBinding.errorLayout.errorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(),
                getActivity().getTheme()));
        mFragmentDecksBinding.errorLayout.floatingActionButton.setOnClickListener(view -> this.sendRequestGetDecks());*/
    }

    private static class DeckAdapter extends RecyclerView.Adapter<DeckViewHolder>{
        private List<Deck> mDecks;
        private static ClickListener clickListener;

        public void setDecks(List<Deck> notes) {
            this.mDecks = notes;
            notifyDataSetChanged();
        }

        @Override
        public DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.deck_layout, parent, false);

            return new DeckViewHolder(itemView, clickListener);
        }

        @Override
        public void onBindViewHolder(DeckViewHolder holder, int position) {
            holder.mDeckName.setText(mDecks.get(position).deckName);
        }

        @Override
        public int getItemCount() {
            return mDecks == null ? 0 : mDecks.size();
        }

        public Deck getItem(int position) {
            return mDecks != null ? mDecks.get(position) : null;
        }

        public void setOnItemClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public interface ClickListener {
            void onItemClick(int position, View v);
        }
    }

    private static class DeckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public DeckAdapter.ClickListener clickListener;
        protected TextView mDeckName;

        DeckViewHolder(View itemView, DeckAdapter.ClickListener clickListener) {
            super(itemView);
            mDeckName = (TextView) itemView.findViewById(R.id.deck_button);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), itemView);
        }
    }
}