package be.henallux.studycard.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class HomeFragment  extends Fragment {
    private FragmentHomeBinding mFragmentHomeBinding;
    private HomeViewModel mHomeViewModel;

    public HomeFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        Adapter adapter = new Adapter();

        sendRequestGetDecks();
        mHomeViewModel.getDecks().observe(getViewLifecycleOwner(), deckList -> {
            adapter.setDecks(deckList);
            mFragmentHomeBinding.progressBarDecks.setVisibility(View.GONE);
            mFragmentHomeBinding.listAllDecksUserRecyclerView.setVisibility(View.VISIBLE);
        });
        mHomeViewModel.getError().observe(getViewLifecycleOwner(), this::displayError);

        mFragmentHomeBinding.listAllDecksUserRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        mFragmentHomeBinding.listAllDecksUserRecyclerView.setAdapter(adapter);

        return mFragmentHomeBinding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) requireActivity()).setToolBarTitle(getString(R.string.menu_home));
        super.onResume();
    }

    private void sendRequestGetDecks(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String pseudo = sharedPreferences.getString("pseudo", "vide");
        mHomeViewModel.getAllDecksFromUser(pseudo);
        mFragmentHomeBinding.progressBarDecks.setVisibility(View.VISIBLE);
        mFragmentHomeBinding.listAllDecksUserRecyclerView.setVisibility(View.GONE);
    }


    private void displayError(NetworkError error) {
        mFragmentHomeBinding.progressBarDecks.setVisibility(View.GONE);
        if (error == null) {
            mFragmentHomeBinding.listAllDecksUserRecyclerView.setVisibility(View.VISIBLE);
            //binding.errorLayout.getRoot().setVisibility(View.GONE);
            return;
        }
        //binding.errorLayout.getRoot().setVisibility(View.VISIBLE);
        mFragmentHomeBinding.listAllDecksUserRecyclerView.setVisibility(View.GONE);
        //binding.errorLayout.errorText.setText(error.getErrorMessage());
        //binding.errorLayout.floatingActionButton.setOnClickListener(view -> this.sendRequestGetDecks());
    }


    private static class HomeViewHolder extends RecyclerView.ViewHolder {
        public Button deckButton;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            deckButton = itemView.findViewById(R.id.deck_button);
        }
    }

    private static class Adapter extends RecyclerView.Adapter<HomeViewHolder> {
        private List<Deck> decksFromUser;

        @NonNull
        @Override
        public HomeFragment.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_layout, parent, false);
            HomeViewHolder homeViewHolder = new HomeViewHolder(view);
            return homeViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
            Deck deck = decksFromUser.get(position);
            holder.deckButton.setOnClickListener(view ->  Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_deckFragment));
            holder.deckButton.setText(deck.deckName);
        }

        @Override
        public int getItemCount() {
            return decksFromUser == null ? 0 : decksFromUser.size();
        }

        public void setDecks(List<Deck> myDecks) {
            this.decksFromUser = myDecks;
            notifyDataSetChanged();
        }
    }
}
