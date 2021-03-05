package com.example.daily_function.posedetector.classification;

import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
public class EMASmoothing {
    private static final int DEFAULT_WINDOW_SIZE = 10;
    private static final float DEFAULT_ALPHA = 0.2f;

    private final int windowSize;
    private final float alpha;
    // {@link PoseClassifier}에서 출력 한 {@link ClassificationResult}의 창
    // {@link windowSize} 크기의 창에 대해 스무딩을 실행
    private final Deque<ClassificationResult> window;

    public EMASmoothing() {
        this(DEFAULT_WINDOW_SIZE, DEFAULT_ALPHA);
    }

    public EMASmoothing(int windowSize, float alpha) {
        this.windowSize = windowSize;
        this.alpha = alpha;
        this.window = new LinkedBlockingDeque<>(windowSize);
    }

    public ClassificationResult getSmoothedResult(ClassificationResult classificationResult) {
        //
        //창 크기 인 경우 마지막 (가장 오래된) 결과를 제거
        if (window.size() == windowSize) {
            window.pollLast();
        }
        // 창 시작 부분에 삽입
        window.addFirst(classificationResult);

        Set<String> allClasses = new HashSet<>();
        for (ClassificationResult result : window) {
            allClasses.addAll(result.getAllClasses());
        }

        ClassificationResult smoothedResult = new ClassificationResult();

        for (String className : allClasses) {
            float factor = 1;
            float topSum = 0;
            float bottomSum = 0;
            for (ClassificationResult result : window) {
                float value = result.getClassConfidence(className);

                topSum += factor * value;
                bottomSum += factor;

                factor = (float) (factor * (1.0 - alpha));
            }
            smoothedResult.putClassConfidence(className, topSum / bottomSum);
        }

        return smoothedResult;
    }
}
