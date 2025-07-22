package basesoftware.com.aoffinal.presentation.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import android.view.View;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import javax.inject.Inject;
import basesoftware.com.aoffinal.R;
import basesoftware.com.aoffinal.databinding.QuestionSnackbarBinding;
import basesoftware.com.aoffinal.domain.callback.ResultCallBack;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class UserUtil {

    private final Activity activity;

    @Inject
    public UserUtil(Activity activity) { this.activity = activity; }

    public void showSnackbar(String message) {

        Snackbar.make(activity.findViewById(R.id.mainLayout), message, Snackbar.LENGTH_SHORT)
                .addCallback(new BaseTransientBottomBar.BaseCallback<>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);

                        String saveSuccess = activity.getString(R.string.saveSuccess);
                        String saveFailed = activity.getString(R.string.saveFailed);

                        if (message.equals(saveSuccess) || message.equals(saveFailed)) inAppReview();

                    }
                })
                .show();

    }

    @SuppressLint("RestrictedApi")
    public void showQuestionSnackbar(String message, View.OnClickListener action) {

        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.mainLayout), message, Snackbar.LENGTH_INDEFINITE);

        Snackbar.SnackbarLayout snackbarLayout = ((Snackbar.SnackbarLayout) snackbar.getView());

        QuestionSnackbarBinding snackbarBinding = QuestionSnackbarBinding.inflate(activity.getLayoutInflater());

        snackbarBinding.btnActionYes.setOnClickListener(v -> {
            action.onClick(v);
            snackbar.dismiss();
        });

        snackbarBinding.btnActionNo.setOnClickListener( v -> {
            if (message.equals(activity.getString(R.string.saveQuestion))) inAppReview();
            snackbar.dismiss();
        });

        snackbarBinding.setMessage(message);

        snackbarLayout.removeViewAt(0);

        snackbarLayout.addView(snackbarBinding.getRoot(), 0);

        snackbar.show();

    }

    public void inAppUpdate(ResultCallBack<Void> callback) {

        final int IMMEDIATE = AppUpdateType.IMMEDIATE;
        final int UPDATE_AVAILABLE = UpdateAvailability.UPDATE_AVAILABLE;

        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(activity);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnCompleteListener(task -> {

            if(task.isSuccessful() && task.getResult().updateAvailability() == UPDATE_AVAILABLE && task.getResult().isUpdateTypeAllowed(IMMEDIATE)) {

                // IMMEDIATE türünde güncellemeyi başlat
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            task.getResult(),
                            activity,
                            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                            11
                    );
                }

                catch (IntentSender.SendIntentException e) {

                    e.getStackTrace();

                    callback.onSuccess(null);

                }

            }

            else callback.onSuccess(null);

        });

    }

    public void inAppReview() {

        ReviewManager reviewManager = ReviewManagerFactory.create(activity);

        reviewManager
                .requestReviewFlow()
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {

                        reviewManager.launchReviewFlow(activity, task.getResult()).addOnCompleteListener(result -> {

                            if (result.isSuccessful()) Log.i("APPREVIEW_SUCCESS", "UserUtil_inAppReview() / YORUM / Uygulama yorumu başarılı");

                            else Log.i("APPREVIEW_FAIL", "UserUtil_inAppReview() / YORUM / Uygulama yorumu başarısız");

                        });

                    }

                });

    }

    public void openShareAppDialog() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.app_market_page_link));
        activity.startActivity(Intent.createChooser(intent, "Uygulamayı Paylaş"));

    }

}
