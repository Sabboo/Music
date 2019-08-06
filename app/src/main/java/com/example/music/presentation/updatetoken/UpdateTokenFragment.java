package com.example.music.presentation.updatetoken;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.music.R;
import com.example.music.databinding.FragmentUpdateTokenBinding;
import com.example.music.helpers.ActivityUtils;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class UpdateTokenFragment extends Fragment implements UpdateTokenContract.View {

    private UpdateTokenContract.Presenter mPresenter;
    private FragmentUpdateTokenBinding mBinding;

    public UpdateTokenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_token,
                container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        mPresenter = new UpdateTokenPresenter(getContext(), this);

        mPresenter.updateToken();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleNoInternetConnectionScreen();
        proceedButtonListener();
    }

    private void handleNoInternetConnectionScreen() {
        mBinding.includeLayoutNoConnection.findViewById(R.id.btn_try_again).setOnClickListener(v -> {
            if (getContext() != null)
                if (ActivityUtils.isNetworkAvailable(getContext()))
                    mPresenter.updateToken();
        });
    }

    private void proceedButtonListener() {
        mBinding.btnProceed.setOnClickListener(Navigation.createNavigateOnClickListener(
                R.id.action_updateTokenFragment_to_songsListFragment, null));
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mBinding.progressBarUpdateToken.setVisibility(active ? VISIBLE : INVISIBLE);
    }

    @Override
    public void showNoInternetConnectionView() {
        mBinding.includeLayoutNoConnection.setVisibility(VISIBLE);
        mBinding.ivUpdateToken.setVisibility(INVISIBLE);
        mBinding.tvUpdatingToken.setVisibility(INVISIBLE);
        mBinding.tvTokenUpdated.setVisibility(INVISIBLE);
        mBinding.btnProceed.setVisibility(INVISIBLE);
    }

    @Override
    public void hideNoInternetConnectionView() {
        mBinding.includeLayoutNoConnection.setVisibility(INVISIBLE);
    }

    @Override
    public void tokenUpdatedSuccessfully() {
        hideNoInternetConnectionView();
        mBinding.ivUpdateToken.setVisibility(GONE);
        mBinding.tvUpdatingToken.setVisibility(GONE);
        mBinding.btnProceed.setVisibility(VISIBLE);
        mBinding.tvTokenUpdated.setVisibility(VISIBLE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(UpdateTokenContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
