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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.util.List;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentHomeBinding;
import be.henallux.studycard.models.Deck;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;
import be.henallux.studycard.ui.deck.DeckFragment;

public class HomeFragment  extends Fragment {
    private FragmentHomeBinding mFragmentHomeBinding;
    private Handler mHandler;
    private HomeViewModel mHomeViewModel;
    Adapter mAdapter;

    public HomeFragment() {}

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

        /*sendRequestGetDecks();
        mHomeViewModel.getDecks().observe(getViewLifecycleOwner(), deckList -> {
            mAdapter.setOnItemClickListener((position, v) -> {
                Bundle bundle = DeckFragment.newArguments(mAdapter.getItem(position));
                Navigation.findNavController(v)
                        .navigate(R.id.action_homeFragment_to_deckFragment, bundle);
            });

            mAdapter.setDecks(deckList);
            mFragmentHomeBinding.progressBarDecks.setVisibility(View.GONE);
            mFragmentHomeBinding.recyclerView.setVisibility(View.VISIBLE);
        });
        mHomeViewModel.getError().observe(getViewLifecycleOwner(), this::displayError);*/

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

    private void sendRequestGetDecks(){
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
            //binding.errorLayout.getRoot().setVisibility(View.GONE);
            return;
        }
        //binding.errorLayout.getRoot().setVisibility(View.VISIBLE);
        mFragmentHomeBinding.recyclerView.setVisibility(View.GONE);
        //binding.errorLayout.errorText.setText(error.getErrorMessage());
        // binding.errorLayout.errorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(),
        //                getActivity().getTheme()));
        //binding.errorLayout.floatingActionButton.setOnClickListener(view -> this.sendRequestGetDecks());
    }


    private static class Adapter extends RecyclerView.Adapter<HomeViewHolder> {
        private List<Deck> decksFromUser;
        private static ClickListener clickListener;

        @Override
        public HomeFragment.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_layout, parent, false);
            HomeViewHolder homeViewHolder = new HomeViewHolder(view, clickListener);
            return homeViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
            holder.deckButton.setText(decksFromUser.get(position).deckName);

            Bundle deckArgs = DeckFragment.newArguments(decksFromUser.get(position));
            holder.deckButton.setOnClickListener(view ->  {
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

/*package be.henallux.studycard.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Objects;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentHomeBinding;
import be.henallux.studycard.models.Deck;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;
import be.henallux.studycard.ui.deck.DeckFragment;
import be.henallux.studycard.ui.deck.DeckViewModel;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding mFragmentHomeBinding;
    HomeViewModel mHomeViewModel;
    private Handler mHandler;
    Adapter mAdapter;

    public HomeFragment() {
    }

    /*private Runnable updateAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            sendRequestGetDecks();
            mHomeViewModel.getDecks().observe(getViewLifecycleOwner(), deckList -> {
                mAdapter.setDecks(deckList);
                mFragmentHomeBinding.progressBarDecks.setVisibility(View.GONE);
                mFragmentHomeBinding.recyclerView.setVisibility(View.VISIBLE);
            });
            mHomeViewModel.getError().observe(getViewLifecycleOwner(), HomeFragment.this::displayError);
        }
    };*/

/*
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        sendRequestGetDecks();
        mHomeViewModel.getDecks().observe(getViewLifecycleOwner(), deckList -> {
            mAdapter.setDecks(deckList);
            mFragmentHomeBinding.progressBarDecks.setVisibility(View.GONE);
            mFragmentHomeBinding.recyclerView.setVisibility(View.VISIBLE);
        });
        mHomeViewModel.getError().observe(getViewLifecycleOwner(), this::displayError);

        // new
        /*mAdapter.setOnItemClickListener((position, view) -> {
            Bundle bundle = DeckFragment.newArguments(mAdapter.getItem(position));
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_deckFragment, bundle);
        });*/
/*
        mFragmentHomeBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        mFragmentHomeBinding.recyclerView.setAdapter(mAdapter);


        return mFragmentHomeBinding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) requireActivity()).setToolBarTitle(getString(R.string.menu_home));
        super.onResume();
       // updateAdapterRunnable.run();
    }

    private void sendRequestGetDecks() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String pseudo = sharedPreferences.getString("pseudo", "vide");
        mHomeViewModel.getAllDecksFromUser(pseudo);
        mFragmentHomeBinding.progressBarDecks.setVisibility(View.VISIBLE);
        mFragmentHomeBinding.recyclerView.setVisibility(View.GONE);
    }


    private void displayError(NetworkError error) {
        mFragmentHomeBinding.progressBarDecks.setVisibility(View.GONE);
        if (error == null) {
            mFragmentHomeBinding.recyclerView.setVisibility(View.VISIBLE);
            //binding.errorLayout.getRoot().setVisibility(View.GONE);
            return;
        }
        //binding.errorLayout.getRoot().setVisibility(View.VISIBLE);
        mFragmentHomeBinding.recyclerView.setVisibility(View.GONE);
        //binding.errorLayout.errorText.setText(error.getErrorMessage());
        //binding.errorLayout.floatingActionButton.setOnClickListener(view -> this.sendRequestGetDecks());
    }

    private static class Adapter extends RecyclerView.Adapter<HomeViewHolder> {
        private List<Deck> decksFromUser;
        private static ClickListener clickListener;

        @NonNull
        @Override
        public HomeFragment.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_layout, parent, false);
            HomeViewHolder homeViewHolder = new HomeViewHolder(view, clickListener);
            return homeViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
            Deck deck = decksFromUser.get(position);
            holder.deckButton.setText(deck.deckName);
            holder.deckButton.setOnClickListener(view ->  Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_deckFragment));
        }

        @Nullable
        @Contract(pure = true)
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
        public Button deckButton;

        public HomeViewHolder(@NonNull View itemView, Adapter.ClickListener clickListener) {
            super(itemView);
            deckButton = itemView.findViewById(R.id.deck_button);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), itemView);
        }
    }
}
*/