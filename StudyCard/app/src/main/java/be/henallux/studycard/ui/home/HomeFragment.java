package be.henallux.studycard.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentHomeBinding;
import be.henallux.studycard.models.Deck;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;
import be.henallux.studycard.ui.deck.DeckFragment;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding mFragmentHomeBinding;
    private Handler mHandler;
    private HomeViewModel mHomeViewModel;
    Adapter mAdapter;

    public HomeFragment() {
    }

    private Runnable updateAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            sendRequestGetDecks();
            mHomeViewModel.getDecks().observe(getViewLifecycleOwner(), deckList -> {
                mAdapter.setDecks(deckList);
                mFragmentHomeBinding.progressBar.setVisibility(View.GONE);
                mFragmentHomeBinding.recyclerView.setVisibility(View.VISIBLE);
            });
            mHomeViewModel.getError().observe(getViewLifecycleOwner(), HomeFragment.this::displayError);
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mAdapter = new Adapter();

        mAdapter.setOnItemClickListener((position, v) -> {
            assert mAdapter.getItem(position) != null;
            Bundle deckArgs = DeckFragment.newArguments(mAdapter.getItem(position));
            Navigation.findNavController(v)
                    .navigate(R.id.action_homeFragment_to_deckFragment, deckArgs);
        });

        mFragmentHomeBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFragmentHomeBinding.recyclerView.setEmptyView(mFragmentHomeBinding.emptyView);
        mFragmentHomeBinding.recyclerView.setAdapter(mAdapter);

        mHandler = new Handler();

        return mFragmentHomeBinding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) requireActivity()).setToolBarTitle(getString(R.string.menu_home));
        super.onResume();
        updateAdapterRunnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(updateAdapterRunnable);
    }

    private void sendRequestGetDecks() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String pseudo = sharedPreferences.getString("pseudo", "vide");
        mHomeViewModel.getAllDecksFromUser(pseudo);
        mFragmentHomeBinding.progressBar.setVisibility(View.VISIBLE);
        mFragmentHomeBinding.recyclerView.setVisibility(View.GONE);
    }


    private void displayError(NetworkError error) {
        mFragmentHomeBinding.progressBar.setVisibility(View.GONE);
        if (error == null) {
            mFragmentHomeBinding.recyclerView.setVisibility(View.VISIBLE);
            mFragmentHomeBinding.errorLayout.setVisibility(View.GONE);
            return;
        }
        mFragmentHomeBinding.emptyView.setVisibility(error == NetworkError.NOT_FOUND ? View.VISIBLE : View.GONE);
        mFragmentHomeBinding.errorLayout.setVisibility(View.VISIBLE);
        mFragmentHomeBinding.recyclerView.setVisibility(View.GONE);
        mFragmentHomeBinding.errorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(), getActivity().getTheme()));
        mFragmentHomeBinding.errorText.setText(error.getErrorMessage());
    }


    private static class Adapter extends RecyclerView.Adapter<HomeViewHolder> {
        private List<Deck> decksFromUser;
        private static ClickListener clickListener;

        @Override
        public HomeFragment.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_layout, parent, false);
            return new HomeViewHolder(view, clickListener);
        }

        @Override
        public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
            holder.deckButton.setText(decksFromUser.get(position).deckName);

            Bundle deckArgs = DeckFragment.newArguments(decksFromUser.get(position));
            holder.deckButton.setOnClickListener(view -> {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_deckFragment, deckArgs);
            });
        }

        public Deck getItem(int position) {
            return decksFromUser != null ? decksFromUser.get(position) : null;
        }

        @Override
        public int getItemCount() {
            return decksFromUser == null ? 0 : decksFromUser.size();
        }

        public void setDecks(List<Deck> myDecks) {
            this.decksFromUser = myDecks;
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public interface ClickListener {
            void onItemClick(int position, View v);
        }
    }

    private static class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Adapter.ClickListener clickListener;
        protected Button deckButton;

        public HomeViewHolder(@NonNull View itemView, Adapter.ClickListener clickListener) {
            super(itemView);
            deckButton = (Button) itemView.findViewById(R.id.deck_button);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), itemView);
        }
    }
}