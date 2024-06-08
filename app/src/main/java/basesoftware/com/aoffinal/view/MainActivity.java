package basesoftware.com.aoffinal.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import javax.inject.Inject;
import basesoftware.com.aoffinal.R;
import basesoftware.com.aoffinal.databinding.ActivityMainBinding;
import basesoftware.com.aoffinal.impl.IContract;
import basesoftware.com.aoffinal.domain.model.TrainingModel;
import basesoftware.com.aoffinal.domain.presenter.Presenter;
import basesoftware.com.aoffinal.util.WorkUtil;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements IContract.IView {

    private ActivityMainBinding binding;

    @Inject
    Presenter presenter;

    @Inject
    WorkUtil workUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        btnListener();

        checkUpdateThenStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    private void init() {

        presenter.initBinds(this);

        workUtil.initBinds(this);

        binding.setViews(this);

        binding.setLifecycleOwner(this);

        initAds();

    }

    public void btnListener() {

        binding.btnCalculate.setOnClickListener(v -> presenter.calculateClick(binding.getTrainingModel()));

        binding.btnClearData.setOnClickListener(v -> workUtil.showQuestionSnackbar(getString(R.string.deleteQuestion), action -> presenter.deleteSavedDataInDb()));

    }

    @Override
    public void checkSavedDataInDb() { presenter.checkSavedDataInDb(); }

    @Override
    public void updateView(TrainingModel trainingModel) { binding.setTrainingModel(trainingModel); }

    @Override
    public void textChanged(Integer viewTag, String viewText) { presenter.textChanged(binding.getTrainingModel(), viewTag, viewText); }

    private void initAds() {

        MobileAds.initialize(this, initializationStatus -> {});

        binding.adBanner.loadAd(new AdRequest.Builder().build());

    }

    public void checkUpdateThenStart() { workUtil.inAppUpdate(); }

    @Override
    public void saveQuestion() { workUtil.showQuestionSnackbar(getString(R.string.saveQuestion), v -> presenter.saveData()); }

    @Override
    public void saveResult(Boolean isSuccess) { workUtil.showSnackbar(getString((isSuccess) ? R.string.saveSuccess : R.string.saveFailed)); }

    @Override
    public void deleteResult(Boolean isSuccess) { workUtil.showSnackbar(getString((isSuccess) ? R.string.deleteSuccess : R.string.deleteFailed)); }

    @Override
    public void visitStoreQuestion() { workUtil.showQuestionSnackbar(getString(R.string.visitStoreQuestion), v -> openPlayStorePage()); }

    public void openPlayStorePage() { workUtil.visitStorePage(getString(R.string.app_page)); }

}
