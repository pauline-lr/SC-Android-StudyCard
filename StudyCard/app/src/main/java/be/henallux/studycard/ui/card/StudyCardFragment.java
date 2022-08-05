package be.henallux.studycard.ui.card;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentStudyCardBinding;
import be.henallux.studycard.models.Card;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;
import be.henallux.studycard.ui.deck.RevisionDeckFragment;

public class StudyCardFragment extends Fragment {
    private static final String ARG_DECK_ID = "deck_id";
    private static final String ARG_DECK_NAME = "deck_name";
    private static final String ARG_POSITION = "position";

    private int id;
    private String deckName;
    private int position;

    FragmentStudyCardBinding mFragmentStudyCardBinding;
    CardViewModel mCardViewModel;
    CardAdapter mCardAdapter;

    public static Bundle newArguments(Integer deck_id, String deck_name, Integer position) {
        Bundle args = new Bundle();
        args.putString(ARG_DECK_NAME, deck_name);
        args.putInt(ARG_DECK_ID, deck_id);
        args.putInt(ARG_POSITION, position);
        return args;
    }

    public StudyCardFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentStudyCardBinding = FragmentStudyCardBinding.inflate(inflater, container, false);
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);

        mCardAdapter = new CardAdapter();

        assert getArguments() != null;
        id = getArguments().getInt(ARG_DECK_ID);
        deckName = getArguments().getString(ARG_DECK_NAME);
        position = getArguments().getInt(ARG_POSITION);

        mCardViewModel.getError().observe(getViewLifecycleOwner(), this::displayErrorScreen);

        mCardViewModel.getCardFromWeb(id, position);

        //mStudyCardViewModel.getCard().observe(getViewLifecycleOwner(), mCardAdapter::setCard);

        Bundle cardsArgs = ResponseCardFragment.newArguments(id, deckName, position);
        mFragmentStudyCardBinding.displayResponseButton.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(R.id.action_StudyCardFragment_to_ResponseCardFragment, cardsArgs));

        Bundle deckArgs = RevisionDeckFragment.newArguments(id, deckName);
        mFragmentStudyCardBinding.leaveButton.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(R.id.action_StudyCardFragment_to_RevisionDeckFragment, deckArgs));

        return mFragmentStudyCardBinding.getRoot();
    }

    private void displayErrorScreen(NetworkError error) {
        if (error == null) {
            mFragmentStudyCardBinding.cardsInformationsLayout.setVisibility(View.VISIBLE);
            //mFragmentStudyCardBinding.errorLayout.getRoot().setVisibility(View.GONE);
            mFragmentStudyCardBinding.displayResponseButton.setEnabled(true);
            Card card = mCardViewModel.getCard().getValue();
            mFragmentStudyCardBinding.frontCardText.setText(card.frontCard);
            return;
        }
        /*mFragmentStudyCardBinding.errorLayout.getRoot().setVisibility(View.VISIBLE);
        mFragmentStudyCardBinding.cardsInformationsLayout.setVisibility(View.GONE);
        mFragmentStudyCardBinding.errorLayout.errorText.setText(error.getErrorMessage());
        mFragmentStudyCardBinding.errorLayout.errorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(),
                getActivity().getTheme()));
        mFragmentStudyCardBinding.errorLayout.floatingActionButton.setOnClickListener(view -> this.sendRequestGetCustomer());*/
    }

    @Override
    public void onResume() {
        ((MainActivity) requireActivity()).setToolBarTitle(deckName);
        
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private static class CardAdapter extends RecyclerView.Adapter<StudyCardViewHolder> {
        private Card mCard;

        public void setCard(Card card) {
            this.mCard = card;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public StudyCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_study_card, parent, false);
            return new StudyCardViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull StudyCardViewHolder holder, int position) {
            holder.displayResponseButton.setEnabled(true);
            holder.frontCardText.setText(mCard.frontCard);
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private static class StudyCardViewHolder extends RecyclerView.ViewHolder {
        protected TextView frontCardText;
        protected Button displayResponseButton;

        StudyCardViewHolder(View itemView) {
            super(itemView);
            frontCardText = itemView.findViewById(R.id.front_card_text);
            displayResponseButton = itemView.findViewById(R.id.display_response_button);
        }
    }
}