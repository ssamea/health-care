package com.example.daily_function.posedetector.classification;

public class RepetitionCounter {
    // 이러한 임계 값은 {@link PoseClassifier}의 상위 K 값과 함께 조정
    // 기본 Top K 값은 10이므로 여기서 범위는 [0-10]
    private static final float DEFAULT_ENTER_THRESHOLD = 6f;
    private static final float DEFAULT_EXIT_THRESHOLD = 4f;

    private final String className;
    private final float enterThreshold;
    private final float exitThreshold;

    private int numRepeats;
    private boolean poseEntered;

    public RepetitionCounter(String className) {
        this(className, DEFAULT_ENTER_THRESHOLD, DEFAULT_EXIT_THRESHOLD);
    }

    public RepetitionCounter(String className, float enterThreshold, float exitThreshold) {
        this.className = className;
        this.enterThreshold = enterThreshold;
        this.exitThreshold = exitThreshold;
        numRepeats = 0;
        poseEntered = false;
    }

    /**
     * 새로운 포즈 분류 결과를 추가하고 주어진 클래스의 담당자를 업데이트
     *
     * 클래스의 @param classificationResult {link ClassificationResult}를 신뢰 값에 연결
     * @return number of reps.
     */
    public int addClassificationResult(ClassificationResult classificationResult) {
        float poseConfidence = classificationResult.getClassConfidence(className);

        if (!poseEntered) {
            poseEntered = poseConfidence > enterThreshold;
            return numRepeats;
        }

        if (poseConfidence < exitThreshold) {
            numRepeats++;
            poseEntered = false;
        }

        return numRepeats;
    }

    public String getClassName() {
        return className;
    }

    public int getNumRepeats() {
        return numRepeats;
    }
}
