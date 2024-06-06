package basesoftware.com.aoffinal.util;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import basesoftware.com.aoffinal.impl.IContract;

public class BindingUtil {

    @BindingAdapter("android:dataListener")
    public static void dataListener(@NonNull TextInputEditText view, @NonNull IContract.IView viewImpl) {

        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable text) {

                // text.toString().isEmpty() || !text.toString().equals(".") || (Integer.parseInt(text.toString()) > 0 && Integer.parseInt(text.toString()) <= 100)
                String correctDataRegex = "^$|^(?!\\.$)(\\d{1,2}(\\.\\d+)?|100(\\.0+)?)$";

                if (text.toString().matches(correctDataRegex)) viewImpl.textChanged(Integer.parseInt(view.getTag().toString()), text.toString());

                else {

                    if (text.toString().equals(".")) Objects.requireNonNull(view.getText()).clear();

                    else if (text.toString().equals("100.") || Double.parseDouble(text.toString()) > 100) view.setText("100");

                }

            }

        });

    }

}
