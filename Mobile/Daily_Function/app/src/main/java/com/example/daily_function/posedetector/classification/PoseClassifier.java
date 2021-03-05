package com.example.daily_function.posedetector.classification;

import static com.example.daily_function.posedetector.classification.PoseEmbedding.getPoseEmbedding;
import static com.example.daily_function.posedetector.classification.Utils.maxAbs;
import static com.example.daily_function.posedetector.classification.Utils.multiply;
import static com.example.daily_function.posedetector.classification.Utils.multiplyAll;
import static com.example.daily_function.posedetector.classification.Utils.subtract;
import static com.example.daily_function.posedetector.classification.Utils.sumAbs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import android.util.Pair;
import com.google.mlkit.vision.common.PointF3D;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PoseClassifier {
    private static final String TAG = "PoseClassifier";
    private static final int MAX_DISTANCE_TOP_K = 30;
    private static final int MEAN_DISTANCE_TOP_K = 10;
    // Z는 일반적으로 X 및 Y보다 정확도가 낮기 때문에 가중치가 낮음
    private static final PointF3D AXES_WEIGHTS = PointF3D.from(1, 1, 0.2f);

    private final List<PoseSample> poseSamples;
    private final int maxDistanceTopK;
    private final int meanDistanceTopK;
    private final PointF3D axesWeights;

    public PoseClassifier(List<PoseSample> poseSamples) {
        this(poseSamples, MAX_DISTANCE_TOP_K, MEAN_DISTANCE_TOP_K, AXES_WEIGHTS);
    }

    public PoseClassifier(List<PoseSample> poseSamples, int maxDistanceTopK,
                          int meanDistanceTopK, PointF3D axesWeights) {
        this.poseSamples = poseSamples;
        this.maxDistanceTopK = maxDistanceTopK;
        this.meanDistanceTopK = meanDistanceTopK;
        this.axesWeights = axesWeights;
    }

    private static List<PointF3D> extractPoseLandmarks(Pose pose) {
        List<PointF3D> landmarks = new ArrayList<>();
        for (PoseLandmark poseLandmark : pose.getAllPoseLandmarks()) {
            landmarks.add(poseLandmark.getPosition3D());
        }
        return landmarks;
    }

    /**
     * 신뢰도 값의 최대 범위를 반환.
     *
     * <p><Since we calculate confidence by counting {@link PoseSample}s that survived
     * outlier-filtering by maxDistanceTopK and meanDistanceTopK, this range is the minimum of two.
     */
    public int confidenceRange() {
        return min(maxDistanceTopK, meanDistanceTopK);
    }

    public ClassificationResult classify(Pose pose) {
        return classify(extractPoseLandmarks(pose));
    }

    public ClassificationResult classify(List<PointF3D> landmarks) {
        ClassificationResult result = new ClassificationResult();
        // 랜드 마크가 감지되지 않으면 종료
        if (landmarks.isEmpty()) {
            return result;
        }

        // X 축을 뒤집어서
        List<PointF3D> flippedLandmarks = new ArrayList<>(landmarks);
        multiplyAll(flippedLandmarks, PointF3D.from(-1, 1, 1));

        List<PointF3D> embedding = getPoseEmbedding(landmarks);
        List<PointF3D> flippedEmbedding = getPoseEmbedding(flippedLandmarks);


        // 분류는 두 단계로 정해짐:
        //  * 먼저 MAX 거리로 top-K 샘플을 선택-> 주어진 포즈와 거의 동일한 샘플을 제거 할 수 있지만 다른 방향으로 구부러진 관절이 거의 없을 수 있음
        //  * 그런 다음 MEAN 거리로 Top-K 샘플을 선택->이상 값을 제거한 후 평균적으로 가장 가까운 샘플을 선택

        //
        //top_k 크기에 도달하면 팝할 수 있도록 최대 거리를 맨 위에 유지.
        PriorityQueue<Pair<PoseSample, Float>> maxDistances = new PriorityQueue<>(
                maxDistanceTopK, (o1, o2) -> -Float.compare(o1.second, o2.second));
        // 이상 값을 제거하기 위해 최소 거리로 상위 K 포즈 샘플을 검색
        for (PoseSample poseSample : poseSamples) {
            List<PointF3D> sampleEmbedding = poseSample.getEmbedding();

            float originalMax = 0;
            float flippedMax = 0;
            for (int i = 0; i < embedding.size(); i++) {
                originalMax =
                        max(
                                originalMax,
                                maxAbs(multiply(subtract(embedding.get(i), sampleEmbedding.get(i)), axesWeights)));
                flippedMax =
                        max(
                                flippedMax,
                                maxAbs(
                                        multiply(
                                                subtract(flippedEmbedding.get(i), sampleEmbedding.get(i)), axesWeights)));
            }
            // 최대 거리를 원본의 최소 및 뒤집힌 최대 거리로 설정
            maxDistances.add(new Pair<>(poseSample, min(originalMax, flippedMax)));
            // 최고 n을 유지하기를 원하므로 가장 높은 거리를 꺼냄
            if (maxDistances.size() > maxDistanceTopK) {
                maxDistances.poll();
            }
        }

        // 상단에 더 높은 평균 거리를 유지하여 top_k 크기에 도달 할 때 팝업
        PriorityQueue<Pair<PoseSample, Float>> meanDistances = new PriorityQueue<>(
                meanDistanceTopK, (o1, o2) -> -Float.compare(o1.second, o2.second));
        // 특이 치를 제거하기 위해 최소 평균 거리로 상위 K 포즈 샘플을 검색
        for (Pair<PoseSample, Float> sampleDistances : maxDistances) {
            PoseSample poseSample = sampleDistances.first;
            List<PointF3D> sampleEmbedding = poseSample.getEmbedding();

            float originalSum = 0;
            float flippedSum = 0;
            for (int i = 0; i < embedding.size(); i++) {
                originalSum += sumAbs(multiply(
                        subtract(embedding.get(i), sampleEmbedding.get(i)), axesWeights));
                flippedSum += sumAbs(
                        multiply(subtract(flippedEmbedding.get(i), sampleEmbedding.get(i)), axesWeights));
            }
            // 평균 거리를 원래의 최소 거리와 뒤집힌 평균 거리로 설정
            float meanDistance = min(originalSum, flippedSum) / (embedding.size() * 2);
            meanDistances.add(new Pair<>(poseSample, meanDistance));
            // 최고 k를 유지하기를 원하므로 가장 높은 평균 거리를 pop
            if (meanDistances.size() > meanDistanceTopK) {
                meanDistances.poll();
            }
        }

        for (Pair<PoseSample, Float> sampleDistances : meanDistances) {
            String className = sampleDistances.first.getClassName();
            result.incrementClassConfidence(className);
        }

        return result;
    }
}
