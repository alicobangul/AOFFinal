package basesoftware.com.aoffinal.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputEditText;

import basesoftware.com.aoffinal.impl.Contract;

public class BindingUtil {

    @BindingAdapter("android:dataListener")
    public static void dataListener(@NonNull TextInputEditText view, @NonNull Contract.View viewImpl) {

        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable text) {

                if (text.toString().equals(".")) view.getText().clear();

                else viewImpl.textChanged(Integer.parseInt(view.getTag().toString()), text.toString());

            }

        });

    }

}
