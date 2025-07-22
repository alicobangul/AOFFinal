package basesoftware.com.aoffinal.util;

import android.content.Context;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

public class BindingUtil {

    @BindingAdapter("difficultyLevelColor")
    public static void setDifficultyLevelColor(TextView view, int level) {
        Context context = view.getContext();
        int colorResId = switch (level) {
            case 1 -> android.R.color.holo_green_dark;
            case 2 -> android.R.color.holo_orange_dark;
            default -> android.R.color.holo_red_dark;
        };

        view.setTextColor(ContextCompat.getColor(context, colorResId));
    }

}