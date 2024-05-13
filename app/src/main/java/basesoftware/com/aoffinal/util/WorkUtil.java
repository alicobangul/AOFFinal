package basesoftware.com.aoffinal.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import javax.inject.Inject;
import basesoftware.com.aoffinal.R;
import basesoftware.com.aoffinal.databinding.QuestionSnackbarBinding;
import basesoftware.com.aoffinal.impl.Contract;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class WorkUtil {

    private final Activity activity;

    private Contract.View view;

    @Inject
    public WorkUtil(Activity activity) { this.activity = activity; }

    public void initBinds(Contract.View view) { this.view = view; }

    public void showSnackbar(String message) {

        Snackbar.make(activity.findViewById(R.id.mainLayout), message, Snackbar.LENGTH_SHORT)
                .addCallback(new BaseTransientBottomBar.BaseCallback<>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);

                        boolean isSaveInfo = message.equals(activity.getString(R.string.saveSuccess))
                                || message.equals(activity.getString(R.string.saveFailed));

                        if (isSaveInfo) inAppReview();

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

    public void inAppUpdate() {

        final int IMMEDIATE = AppUpdateType.IMMEDIATE;
        final int UPDATE_AVAILABLE = UpdateAvailability.UPDATE_AVAILABLE;

        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(activity);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnCompleteListener(task -> {

            if(task.isSuccessful() && task.getResult().updateAvailability() == UPDATE_AVAILABLE && task.getResult().isUpdateTypeAllowed(IMMEDIATE)) {

                // IMMEDIATE türünde güncellemeyi başlat
                try { appUpdateManager.startUpdateFlowForResult(task.getResult(), IMMEDIATE, activity, 11); }

                catch (IntentSender.SendIntentException e) {

                    e.getStackTrace();

                    view.checkSavedDataInDb();

                }

            }

            else view.checkSavedDataInDb();

        });

    }

    public void inAppReview() {

        ReviewManager reviewManager = ReviewManagerFactory.create(activity);

        reviewManager
                .requestReviewFlow()
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {

                        reviewManager.launchReviewFlow(activity, task.getResult()).addOnCompleteListener(result -> {

                            if (result.isSuccessful()) showSnackbar(activity.getString(R.string.thanksForComment));

                            else view.visitStoreQuestion();

                        });

                    }

                    else view.visitStoreQuestion();

                });

    }

    public void visitStorePage(String appPageLink) {

        Uri link = Uri.parse(appPageLink);

        try { activity.startActivity(new Intent(Intent.ACTION_VIEW, link).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); }

        catch (ActivityNotFoundException e) { Log.e("WorkUtil - Error", "Play store açılamadı"); }

    }

}
