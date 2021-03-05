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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;

/** 오버레이보기에서 추론 정보 (대기 시간, FPS, 해상도)를 렌더링하기위한 그래픽 인스턴스 */
public class InferenceInfoGraphic extends GraphicOverlay.Graphic {

  private static final int TEXT_COLOR = Color.WHITE;
  public static final float TEXT_SIZE = 60.0f;

  private final Paint textPaint;
  private final GraphicOverlay overlay;
  private final double latency;
  private final long detectorLatency ;

  // 입력 이미지 스트림이 처리 중일 때만 유효합니다. 단일 이미지 모드의 경우 Null
  @Nullable private final Integer framesPerSecond;

  public InferenceInfoGraphic(
      GraphicOverlay overlay, double latency,  long detectorLatency, @Nullable Integer framesPerSecond) {
    super(overlay);
    this.overlay = overlay;
    this.latency = latency;
    this.detectorLatency = detectorLatency;
    this.framesPerSecond = framesPerSecond;
    textPaint = new Paint();
    textPaint.setColor(TEXT_COLOR);
    textPaint.setTextSize(TEXT_SIZE);
    postInvalidate();
  }

  @Override
  public synchronized void draw(Canvas canvas) {
    float x = TEXT_SIZE * 0.5f;
    float y = TEXT_SIZE * 1.5f;

    /*
    canvas.drawText(
        "InputImage size: " + overlay.getImageWidth() + "x" + overlay.getImageHeight(),
        x,
        y,
        textPaint);

     */

    //  FPS (if valid) , inference latency
    /*
    if (framesPerSecond != null) {
      canvas.drawText(
          "FPS: " + framesPerSecond + ", latency: " + latency + " ms", x, y + TEXT_SIZE, textPaint);
    } else {
      canvas.drawText("Latency: " + latency + " ms", x, y + TEXT_SIZE, textPaint);
    }

     */
  }

}
