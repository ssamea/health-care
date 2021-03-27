package com.example.daily_function.posedetector.classification;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.max;

public class ClassificationResult {
    //이 맵의 항목에서 키는 클래스 이름이고 값은이 클래스가 상위 K 개의 가장 가까운 이웃에 나타나는 횟수.
    // 값은 [0, K] 범위에 있으며 EMA 평활화 후 부동 소수점이 될 수 있음.
    // 이 숫자를 사용하여 이 클래스에있는 포즈의 신뢰도를 나타냄
    private final Map<String, Float> classConfidences;

    public ClassificationResult() {
        classConfidences = new HashMap<>();
    }

    public Set<String> getAllClasses() {
        return classConfidences.keySet();
    }

    public float getClassConfidence(String className) {
        return classConfidences.containsKey(className) ? classConfidences.get(className) : 0;
    }

    public String getMaxConfidenceClass() {
        return max(
                classConfidences.entrySet(),
                (entry1, entry2) -> (int) (entry1.getValue() - entry2.getValue()))
                .getKey();
    }

    public void incrementClassConfidence(String className) {
        classConfidences.put(className,
                classConfidences.containsKey(className) ? classConfidences.get(className) + 1 : 1);
    }

    public void putClassConfidence(String className, float confidence) {
        classConfidences.put(className, confidence);
    }
}
