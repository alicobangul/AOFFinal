package basesoftware.com.aoffinal.domain.validator;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import basesoftware.com.aoffinal.util.Constant;
import dagger.hilt.android.scopes.ViewModelScoped;

@ViewModelScoped
public class Validator {

    @Inject
    public Validator() {}

    public String validateNeededGrade(int neededGrade) { return String.valueOf((neededGrade <= 100) ? neededGrade : "KALDINIZ"); }

    public String validateRequiredQuestion(int neededGrade, int requiredQuestion) { return String.valueOf((neededGrade <= 100) ? requiredQuestion : "KALDINIZ"); }

    public int validateDifficultyLevel(int neededGrade) { return (neededGrade <= 30) ? 1 : (neededGrade <= 50) ? 2 : 3; }

    public List<String> validateSettingsData(String midtermExamAverage, String finalExamAverage, String successAverage) {

        List<String> settingsData = List.of(midtermExamAverage, finalExamAverage, successAverage);

        List<String> defaults = List.of(
                Constant.DEFAULT_MIDTERM_EXAM_AVERAGE,
                Constant.DEFAULT_FINAL_EXAM_AVERAGE,
                Constant.DEFAULT_SUCCESS_AVERAGE);
        List<String> results = new ArrayList<>();

        for (int i = 0; i < defaults.size(); i++) {
            settingsData.size();
            String value = settingsData.get(i) != null ? settingsData.get(i).trim() : "";
            results.add(value.isEmpty() || value.equals("0") ? defaults.get(i) : value);
        }

        return results;

    }

    public String checkChangedSettingsData(String input) {
        try {
            double value = Double.parseDouble(input.trim());
            int result = (int) Math.max(0, Math.min(value, 100));
            return String.valueOf(result);
        } catch (Exception e) {
            return "";
        }
    }

    public String checkChangedCourseData(String midtermExam) {

        // Not verisi değiştiğinde [input - kullanıcı girişi] kontrol ediliyor

        try {
            String exam = (midtermExam == null) ? "" : midtermExam.trim();
            if (exam.isEmpty() || exam.matches("(?!\\.$)(\\d{1,2}(\\.\\d+)?|100(\\.0+)?)")) return exam;
            if (exam.equals(".")) return "";
            return Double.parseDouble(exam) > 100 ? "100" : "";
        }
        catch (Exception e) { return ""; }

    }

}