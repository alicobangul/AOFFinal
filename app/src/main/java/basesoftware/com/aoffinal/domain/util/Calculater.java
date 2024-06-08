package basesoftware.com.aoffinal.domain.util;

import java.text.DecimalFormat;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class Calculater {

    @Inject
    public Calculater() {}

    public double calculateResult(@Nonnull String training, Double midtermExam, Double finalExam, Integer average) {

        // Gerekli not hesaplanıyor
        double calculateResult = (average - (Double.parseDouble(training) * midtermExam)) / finalExam;

        /**
         * Eğer double küsüratı iki haneden büyük ise iki haneye düşürüldü
         * String tipindeki dönüşte , işareti . ile değiştirildi [double çevirme işlemi için]
         */
        calculateResult = Double
                .parseDouble((
                        new DecimalFormat((calculateResult < 10.00) ? "#.##" : "##.##")
                                .format(calculateResult)).replace(",",".")
                );

        // Sonuç küsüratlı sayı, yukarı yuvarlandı örn: 65.10 -> 66 tam sayı
        if (calculateResult % 1 != 0) calculateResult = Math.ceil(calculateResult);

        // Sonuç 5'e tam bölünmüyorsa 5 ve katına yuvarla [Her soru 5 puan]
        if (((int) calculateResult) % 5 > 0) calculateResult = calculateResult + (5 - calculateResult % 5);

        return calculateResult;

    }

    public double midtermCalculate(String midtermExam) { return Double.parseDouble(midtermExam) / 100; }

    public double finalExamCalculate(String finalExam) { return Double.parseDouble(finalExam) / 100; }

    // Gereken Puan (Kullanıcının vize ortalaması eğer geçer notu fazlasıyla karşılıyor ise 0 yaz)
    public int calculateRequiredScore(double calculateResult) { return Math.max((int) calculateResult, 0); }

    // Gereken soru sayısı (Kullanıcının vize ortalaması eğer geçer notu fazlasıyla karşılıyor ise 0 yaz)
    public int calculateRequiredQuestion(double calculateResult) { return Math.max((int) calculateResult / 5, 0); }

}
