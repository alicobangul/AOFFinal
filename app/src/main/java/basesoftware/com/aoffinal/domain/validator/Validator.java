package basesoftware.com.aoffinal.domain.validator;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class Validator {

    @Inject
    public Validator() {}

    public String selectOutput(int requiredScore, String calculateOutput, String failOutput) { return (requiredScore <= 100) ? calculateOutput : failOutput; }

    // Geçme notu boş veya 0'a eşit/küçük
    public boolean averageError(@Nonnull String average) { return average.isEmpty() || Integer.parseInt(average) <= 0; }

    // Vize ortalaması boş veya 0'a eşit/küçük
    public boolean midtermError(@Nonnull String midterm) { return midterm.isEmpty() || Integer.parseInt(midterm) <= 0; }

    // Final ortalaması boş veya 0'a eşit/küçük
    public boolean finalError(@Nonnull String finalScore) { return finalScore.isEmpty() || Integer.parseInt(finalScore) <= 0; }

    // Vize + Final toplamı 100'e eşit mi ?
    public boolean totalCourseGradeError(String midterm, String finalScore) { return (Integer.parseInt(midterm) + Integer.parseInt(finalScore) != 100); }

    /**
     * Vize/Final notu boş veya 0'a eşit/küçük,
     * Veya Vize+Final toplamı 100'e eşit değil
     */
    public boolean examOrTotalError(String midtermExam, String finalExam) {

        return (midtermError(midtermExam) || finalError(finalExam)) || totalCourseGradeError(midtermExam, finalExam);

    }

}
