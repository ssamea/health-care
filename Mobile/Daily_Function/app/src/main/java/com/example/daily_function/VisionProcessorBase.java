/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.daily_function;

import static java.lang.Math.max;
import static java.lang.Math.min;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION_CODES;
import android.os.SystemClock;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageProxy;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.mlkit.vision.common.InputImage;
import com.example.daily_function.BitmapUtils;
import com.example.daily_function.CameraImageGraphic;
import com.example.daily_function.FrameMetadata;
import com.example.daily_function.GraphicOverlay;
import com.example.daily_function.InferenceInfoGraphic;
import com.example.daily_function.ScopedExecutor;
import com.example.daily_function.VisionImageProcessor;
import com.example.daily_function.preference.PreferenceUtils;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Abstract base class for vision frame processors. Subclasses need to implement {@link
 * #onSuccess(Object, GraphicOverlay)} to define what they want to with the detection results and
 * {@link #detectInImage(InputImage)} to specify the detector object.
 *
 * @param <T> The type of the detected feature.
 */
public abstract class VisionProcessorBase<T> implements VisionImageProcessor {

  protected static final String MANUAL_TESTING_LOG = "LogTagForTest";
  private static final String TAG = "VisionProcessorBase";

  private final ActivityManager activityManager;
  private final Timer fpsTimer = new Timer();
  private final ScopedExecutor executor;

  //이 프로세서가 이미 종료되었는지 여부
  private boolean isShutdown;

  // 대기 시간을 계산하는 데 사용되며 동일한 스레드에서 실행
  private int numRuns = 0;
  private long totalFrameMs = 0;
  private long maxFrameMs = 0;
  private long minFrameMs = Long.MAX_VALUE;
  private long totalDetectorMs = 0;
  private long maxDetectorMs = 0;
  private long minDetectorMs = Long.MAX_VALUE;

  //FPS를 계산하기 위해 지금까지 1 초 간격으로 처리 된 프레임 수
  private int frameProcessedInOneSecondInterval = 0;
  private int framesPerSecond = 0;

  // 최신 이미지와 해당 메타 데이터를 유지
  @GuardedBy("this")
  private ByteBuffer latestImage;

  @GuardedBy("this")
  private FrameMetadata latestImageMetaData;
  // To keep the images and metadata in process.
  @GuardedBy("this")
  private ByteBuffer processingImage;

  @GuardedBy("this")
  private FrameMetadata processingMetaData;

  protected VisionProcessorBase(Context context) {
    activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    executor = new ScopedExecutor(TaskExecutors.MAIN_THREAD);
    fpsTimer.scheduleAtFixedRate(
            new TimerTask() {
              @Override
              public void run() {
                framesPerSecond = frameProcessedInOneSecondInterval;
                frameProcessedInOneSecondInterval = 0;
              }
            },
            /* delay= */ 0,
            /* period= */ 1000);
  }

  // -----------------단일 정지 이미지 처리 용 코드----------------------------------------
  @Override
  public void processBitmap(Bitmap bitmap, final GraphicOverlay graphicOverlay) {
    long frameStartMs = SystemClock.elapsedRealtime();
    requestDetectInImage(
            InputImage.fromBitmap(bitmap, 0),
            graphicOverlay,
            /* originalCameraImage= */ null,
            /* shouldShowFps= */ false,
            frameStartMs);
  }

  // -----------------Camera1 API에서 실시간 미리보기 프레임 처리를위한 코드-----------------------
  @Override
  public synchronized void processByteBuffer(
          ByteBuffer data, final FrameMetadata frameMetadata, final GraphicOverlay graphicOverlay) {
    latestImage = data;
    latestImageMetaData = frameMetadata;
    if (processingImage == null && processingMetaData == null) {
      processLatestImage(graphicOverlay);
    }
  }

  private synchronized void processLatestImage(final GraphicOverlay graphicOverlay) {
    processingImage = latestImage;
    processingMetaData = latestImageMetaData;
    latestImage = null;
    latestImageMetaData = null;
    if (processingImage != null && processingMetaData != null && !isShutdown) {
      processImage(processingImage, processingMetaData, graphicOverlay);
    }
  }

  private void processImage(
          ByteBuffer data, final FrameMetadata frameMetadata, final GraphicOverlay graphicOverlay) {
    long frameStartMs = SystemClock.elapsedRealtime();

    // 라이브 뷰포트 아래 뷰가 카메라 미리보기 드로잉을 처리-> 수동 미리보기 드로잉에 사용 된 불필요한 비트 맵 생성 생략
    Bitmap bitmap =
            PreferenceUtils.isCameraLiveViewportEnabled(graphicOverlay.getContext())
                    ? null
                    : BitmapUtils.getBitmap(data, frameMetadata);

    requestDetectInImage(
            InputImage.fromByteBuffer(
                    data,
                    frameMetadata.getWidth(),
                    frameMetadata.getHeight(),
                    frameMetadata.getRotation(),
                    InputImage.IMAGE_FORMAT_NV21),
            graphicOverlay,
            bitmap,
            /* shouldShowFps= */ true,
            frameStartMs)
            .addOnSuccessListener(executor, results -> processLatestImage(graphicOverlay));
  }

  // ----------------- CameraX API를 위한 코드-----------------------
  @Override
  @RequiresApi(VERSION_CODES.KITKAT)
  @ExperimentalGetImage
  public void processImageProxy(ImageProxy image, GraphicOverlay graphicOverlay) {
    long frameStartMs = SystemClock.elapsedRealtime();
    if (isShutdown) {
      image.close();
      return;
    }

    Bitmap bitmap = null;
    if (!PreferenceUtils.isCameraLiveViewportEnabled(graphicOverlay.getContext())) {
      bitmap = BitmapUtils.getBitmap(image);
    }

    requestDetectInImage(
            InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees()),
            graphicOverlay,
            /* originalCameraImage= */ bitmap,
            /* shouldShowFps= */ true,
            frameStartMs)
            // When the image is from CameraX analysis use case, must call image.close() on received
            // images when finished using them. Otherwise, new images may not be received or the camera
            // may stall.
            .addOnCompleteListener(results -> image.close());
  }

  // -----------------프로세스 처리 로직-------------------------------------------------------
  private Task<T> requestDetectInImage(
          final InputImage image,
          final GraphicOverlay graphicOverlay,
          @Nullable final Bitmap originalCameraImage,
          boolean shouldShowFps,
          long frameStartMs) {
    final long detectorStartMs = SystemClock.elapsedRealtime();
    return detectInImage(image)
            .addOnSuccessListener(
                    executor,
                    results -> {
                      long endMs = SystemClock.elapsedRealtime();
                      long currentFrameLatencyMs = endMs - frameStartMs;
                      long currentDetectorLatencyMs = endMs - detectorStartMs;
                      numRuns++;
                      frameProcessedInOneSecondInterval++;
                      totalFrameMs += currentFrameLatencyMs;
                      maxFrameMs = max(currentFrameLatencyMs, maxFrameMs);
                      minFrameMs = min(currentFrameLatencyMs, minFrameMs);
                      totalDetectorMs += currentDetectorLatencyMs;
                      maxDetectorMs = max(currentDetectorLatencyMs, maxDetectorMs);
                      minDetectorMs = min(currentDetectorLatencyMs, minDetectorMs);

                      // 추론 정보는 초당 한 번만 기록
                      // frameProcessedInOneSecondInterval이 1이면 현재 두 번째 동안 처리 된 첫 번째 프레임임을 의미
                      if (frameProcessedInOneSecondInterval == 1) {
                        Log.d(TAG, "Num of Runs: " + numRuns);
                        Log.d(
                                TAG,
                                "Frame latency: max="
                                        + maxFrameMs
                                        + ", min="
                                        + minFrameMs
                                        + ", avg="
                                        + totalFrameMs / numRuns);
                        Log.d(
                                TAG,
                                "Detector latency: max="
                                        + maxDetectorMs
                                        + ", min="
                                        + minDetectorMs
                                        + ", avg="
                                        + totalDetectorMs / numRuns);
                        MemoryInfo mi = new MemoryInfo();
                        activityManager.getMemoryInfo(mi);
                        long availableMegs = mi.availMem / 0x100000L;
                        Log.d(TAG, "Memory available in system: " + availableMegs + " MB");
                      }

                      graphicOverlay.clear();
                      if (originalCameraImage != null) {
                        graphicOverlay.add(new CameraImageGraphic(graphicOverlay, originalCameraImage));
                      }
                      graphicOverlay.add(
                              new InferenceInfoGraphic(
                                      graphicOverlay,
                                      currentFrameLatencyMs,
                                      currentDetectorLatencyMs,
                                      shouldShowFps ? framesPerSecond : null));
                      VisionProcessorBase.this.onSuccess(results, graphicOverlay);
                      graphicOverlay.postInvalidate();
                    })
            .addOnFailureListener(
                    executor,
                    e -> {
                      graphicOverlay.clear();
                      graphicOverlay.postInvalidate();
                      String error = "Failed to process. Error: " + e.getLocalizedMessage();
                      Toast.makeText(
                              graphicOverlay.getContext(),
                              error + "\nCause: " + e.getCause(),
                              Toast.LENGTH_SHORT)
                              .show();
                      Log.d(TAG, error);
                      e.printStackTrace();
                      VisionProcessorBase.this.onFailure(e);
                    });
  }

  @Override
  public void stop() {
    executor.shutdown();
    isShutdown = true;
    numRuns = 0;
    totalFrameMs = 0;
    totalDetectorMs = 0;
    fpsTimer.cancel();
  }

  protected abstract Task<T> detectInImage(InputImage image);

  protected abstract void onSuccess(@NonNull T results, @NonNull GraphicOverlay graphicOverlay);

  protected abstract void onFailure(@NonNull Exception e);
}
