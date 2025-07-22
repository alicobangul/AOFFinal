package basesoftware.com.aoffinal.presentation.screens;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import javax.inject.Inject;
import basesoftware.com.aoffinal.R;
import basesoftware.com.aoffinal.databinding.ActivityMainBinding;
import basesoftware.com.aoffinal.domain.callback.ResultCallBack;
import basesoftware.com.aoffinal.presentation.util.UserUtil;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MainViewModel mainViewModel;

    @Inject UserUtil userUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();

        openObservers();

        startApp();

    }

    private void initialize() {

        initAds();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.setMainViewModel(mainViewModel);

        binding.setLifecycleOwner(this);

    }

    private void startApp() {

        userUtil.inAppUpdate(new ResultCallBack<>() {

            @Override
            public void onSuccess(@Nullable Void result) { mainViewModel.inAppUpdateComplete(); }

            @Override
            public void onFailure(@Nullable Throwable throwable) { }

        });

    }

    private void openObservers() {

        mainViewModel.getMainScreenDialog().observe(this, dialog -> {
            switch (dialog) {

                case SAVE_DATA_QUESTION -> userUtil.showQuestionSnackbar(getString(R.string.saveQuestion), v -> mainViewModel.saveDbDataProcess());
                case SAVE_DATA_SUCCESS -> userUtil.showSnackbar(getString(R.string.saveSuccess));
                case SAVE_DATA_FAIL -> userUtil.showSnackbar(getString(R.string.saveFailed));

                case DELETE_DATA_QUESTION -> userUtil.showQuestionSnackbar(getString(R.string.deleteQuestion), v -> mainViewModel.deleteDbDataProcess());
                case DELETE_DATA_SUCCESS -> userUtil.showSnackbar(getString(R.string.deleteSuccess));
                case DELETE_DATA_FAIL -> userUtil.showSnackbar(getString(R.string.deleteFailed));
            }
        });

        mainViewModel.getShareDialog().observe(this, share -> { if(share) userUtil.openShareAppDialog(); });

    }

    private void initAds() {

        MobileAds.initialize(this, initializationStatus -> {});

        binding.adBanner.loadAd(new AdRequest.Builder().build());

    }

}