package com.example.daily_function.posedetector.classification;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import androidx.annotation.WorkerThread;
import com.google.common.base.Preconditions;
import com.google.mlkit.vision.pose.Pose;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class PoseClassifierProcessor {
    private static final String TAG = "PoseClassifierProcessor";
    private static final String POSE_SAMPLES_FILE = "pose/fitness_pose_samples.csv";

    // 반복 횟수를 계산할 클래스를 지정
    // 지정된 {@code POSE_SAMPLES_FILE}의 라벨,  포즈 샘플에 대해 고유 한 클래스 레이블을 설정가능
    private static final String PUSHUPS_CLASS = "pushups_down";
    private static final String SQUATS_CLASS = "squats_down";
    private static final String[] POSE_CLASSES = {
            PUSHUPS_CLASS, SQUATS_CLASS
    };

    private final boolean isStreamMode;

    private EMASmoothing emaSmoothing;
    private List<RepetitionCounter> repCounters;
    private PoseClassifier poseClassifier;
    private String lastRepResult;
    private TextToSpeech tts;              // TTS 변수 선언
    private int cnt;

    //초기화 및 포즈 샘플 로드
    @WorkerThread
    public PoseClassifierProcessor(Context context, boolean isStreamMode) {
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
        this.isStreamMode = isStreamMode;
        if (isStreamMode) {
            emaSmoothing = new EMASmoothing();
            repCounters = new ArrayList<>();
            lastRepResult = "";
            cnt=0;
        }
        loadPoseSamples(context);
    }

    private void loadPoseSamples(Context context) {
        List<PoseSample> poseSamples = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(POSE_SAMPLES_FILE)));
            String csvLine = reader.readLine();
            while (csvLine != null) {
                // 행이 유효한 {@link PoseSample}이 아니면 null이 표시되고 목록에 추가하지 않음
                PoseSample poseSample = PoseSample.getPoseSample(csvLine, ",");
                if (poseSample != null) {
                    poseSamples.add(poseSample);
                }
                csvLine = reader.readLine();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error when loading pose samples.\n" + e);
        }
        poseClassifier = new PoseClassifier(poseSamples);
        if (isStreamMode) {
            for (String className : POSE_CLASSES) {
                repCounters.add(new RepetitionCounter(className));
            }
        }
    }

    /**
     * 새로운 {@link Pose} 입력이 주어지면 포즈 분류 결과와 함께 형식이 지정된 {@link String} 목록을 반환
     *
     * 현재 다음과 같이 최대 2 개의 문자열을 반환
     * 0 : PoseClass : 운동 횟수
     * 1 : PoseClass : [0.0-1.0] 신뢰도
     */
    @WorkerThread
    public List<String> getPoseResult(Pose pose) {
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
        List<String> result = new ArrayList<>();
        ClassificationResult classification = poseClassifier.classify(pose);
        int cnt;

        // Update {@link RepetitionCounter}s if {@code isStreamMode}.
        if (isStreamMode) {
            //포즈를 찾을 수 없더라도 스무딩에 포즈를 제공
            classification = emaSmoothing.getSmoothedResult(classification);

            // 포즈를 찾을 수없는 경우 repCounter를 업데이트하지 않고 종료
            if (pose.getAllPoseLandmarks().isEmpty()) {
                result.add(lastRepResult);
                return result;
            }

            for (RepetitionCounter repCounter : repCounters) {
                int repsBefore = repCounter.getNumRepeats();
                int repsAfter = repCounter.addClassificationResult(classification);
                if (repsAfter > repsBefore) {
                    // 개수증가하면 삑소리남 ㅋㅋ.
                    ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                    tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                    lastRepResult = String.format(
                            Locale.KOREA, "%s : %d 개", repCounter.getClassName(), repsAfter);
                    break;
                }
            }

            result.add(lastRepResult);
        }

        // 포즈가 발견되면 결과에 현재 프레임의 maxConfidence 클래스를 추가
        if (!pose.getAllPoseLandmarks().isEmpty()) {
            String maxConfidenceClass = classification.getMaxConfidenceClass();
            String maxConfidenceClassResult = String.format(
                    Locale.KOREA,
                    "%s : %.2f 신뢰도",
                    maxConfidenceClass,
                    classification.getClassConfidence(maxConfidenceClass)
                            / poseClassifier.confidenceRange());
            result.add(maxConfidenceClassResult);
        }

        return result;
    }

}
