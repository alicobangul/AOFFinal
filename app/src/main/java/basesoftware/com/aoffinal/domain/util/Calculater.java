package basesoftware.com.aoffinal.domain.util;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import basesoftware.com.aoffinal.domain.model.CourseModel;
import basesoftware.com.aoffinal.domain.validator.Validator;
import dagger.hilt.android.scopes.ViewModelScoped;

@ViewModelScoped
public class Calculater {

    private final Validator validator;

    @Inject
    public Calculater(Validator validator) { this.validator = validator; }

    public ArrayList<CourseModel> calculateCourse(String midtermExamAverage, String finalExamAverage, String successAverage, @Nonnull ArrayList<CourseModel> courses) {

        double midtermAverage = Double.parseDouble(midtermExamAverage);
        double finalAverage = Double.parseDouble(finalExamAverage);

        int success = Integer.parseInt(successAverage);

        ArrayList<CourseModel> arrayCourse = new ArrayList<>();

        for (CourseModel course : courses) {

            Integer courseIndex = course.getCourseIndex();
            String grade = course.getGrade();
            String neededGrade = ""; // Gereken puan
            String requiredQuestionCount = ""; // Gereken soru sayısı
            int difficultyLevel = 3;

            if(!course.getGrade().matches("")) {

                // Vizenin katkı puanı
                double midtermContribution = (Double.parseDouble(course.getGrade()) * midtermAverage) / 100.0;

                // Geçmek için gerekli final katkısı
                double requiredFinalContribution = success - midtermContribution;

                // Gereken final notu
                double requiredFinalGradeRaw = (requiredFinalContribution * 100.0) / finalAverage;

                // Gereken puan/final notu (Yukarı doğru 5’in katına yuvarla)
                int roundedFinalGrade = (int) (Math.ceil(requiredFinalGradeRaw / 5.0) * 5);

                // Gereken soru sayısı (Her soru 5 puan)
                int requiredCorrectAnswers = (int) Math.ceil(roundedFinalGrade / 5.0);

                // Eğer gereken puan/final notu 100'den fazlaysa, gereken puan kısmına KALDINIZ yazdır
                neededGrade = validator.validateNeededGrade(roundedFinalGrade);

                // Eğer gereken puan 100'den fazlaysa, gereken soru kısmına KALDINIZ yazdır
                requiredQuestionCount = validator.validateRequiredQuestion(roundedFinalGrade, requiredCorrectAnswers);

                // Gereken nota göre zorluk seviyesini ayarla
                difficultyLevel = validator.validateDifficultyLevel(roundedFinalGrade);

            }

            CourseModel newCourse = new CourseModel(
                    courseIndex,
                    grade,
                    neededGrade,
                    requiredQuestionCount,
                    difficultyLevel
            );

            arrayCourse.add(newCourse);

        }

        return arrayCourse;

    }

}