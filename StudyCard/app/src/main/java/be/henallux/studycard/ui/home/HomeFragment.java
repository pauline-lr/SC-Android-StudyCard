package be.henallux.studycard.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private FragmentHomeBinding binding;
    private RecyclerView homeRecyclerView;
    private HomeViewModel viewModel;

    public HomeFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        Adapter adapter = new Adapter();

        sendRequestGetDecks();
        viewModel.getDecks().observe(getViewLifecycleOwner(), deckList -> {
            adapter.setDecks(deckList);
            binding.progressBarDecks.setVisibility(View.GONE);
            binding.listAllDecksUser.setVisibility(View.VISIBLE);
        });
        viewModel.getError().observe(getViewLifecycleOwner(), this::displayErrorScreen);

        binding.listAllDecksUser.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.listAllDecksUser.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setToolBarTitle(getString(R.string.menu_home));
        super.onResume();
    }

    private void sendRequestGetDecks(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "vide");
        viewModel.getAllDecksFromUser(email);
        binding.progressBarDecks.setVisibility(View.VISIBLE);
        binding.listAllDecksUser.setVisibility(View.GONE);
    }


    private void displayErrorScreen(NetworkError error) {
        binding.progressBarDecks.setVisibility(View.GONE);
        if (error == null) {
            binding.listAllDecksUser.setVisibility(View.VISIBLE);
            binding.errorLayout.getRoot().setVisibility(View.GONE);
            return;
        }
        binding.errorLayout.getRoot().setVisibility(View.VISIBLE);
        binding.listAllDecksUser.setVisibility(View.GONE);
        binding.errorLayout.errorText.setText(error.getErrorMessage());
        binding.errorLayout.floatingActionButton.setOnClickListener(view -> this.sendRequestGetDecks());
    }


    private static class HomeViewHolder extends RecyclerView.ViewHolder {
        // Declare all the fields of a single item here.
        public TextView deckName;


        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            deckName = itemView.findViewById(R.id.deck_name);
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
            Deck c = decksFromUser.get(position);
            holder.deckName.setText(c.deckName);
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
