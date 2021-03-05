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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

/**
 * A view which renders a series of custom graphics to be overlayed on top of an associated preview
 * (i.e., the camera preview). The creator can add graphics objects, update the objects, and remove
 * them, triggering the appropriate drawing and invalidation within the view.
 *
 * <p>Supports scaling and mirroring of the graphics relative the camera's preview properties. The
 * idea is that detection items are expressed in terms of an image size, but need to be scaled up
 * to the full view size, and also mirrored in the case of the front-facing camera.
 *
 * <p>Associated {@link Graphic} items should use the following methods to convert to view
 * coordinates for the graphics that are drawn:
 *
 * <ol>
 *   <li>{@link Graphic#scale(float)} adjusts the size of the supplied value from the image scale
 *       to the view scale.
 *   <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)} adjust the
 *       coordinate from the image's coordinate system to the view coordinate system.
 * </ol>
 */
public class GraphicOverlay extends View {
  private final Object lock = new Object();
  private final List<Graphic> graphics = new ArrayList<>();
  // 이미지 좌표에서 오버레이 뷰 좌표로 변환하기위한 매트릭스.
  private final Matrix transformationMatrix = new Matrix();

  private int imageWidth;
  private int imageHeight;
  // 이미지 크기에 대한 오버레이보기 크기의 요소
  // 이미지 좌표의 모든 항목은 오버레이보기 영역에 맞게 크기만큼 조정되어야함
  private float scaleFactor = 1.0f;
  // 크기 조정 후 오버레이보기 영역에 이미지를 맞추기 위해 각면에서 잘라야하는 수평 픽셀 수
  private float postScaleWidthOffset;
  //이미지에 맞게 각 측면에서 잘라야하는 수직 픽셀 수
  // 스케일링 후 오버레이 뷰 영역.
  private float postScaleHeightOffset;
  private boolean isImageFlipped;
  private boolean needUpdateTransformation = true;

  /**
   * 그래픽 오버레이 내에서 렌더링되는 사용자 정의 그래픽 객체의 기본 클래스.
   * 이를 하위 클래스로 만들고 {@link Graphic # draw (Canvas)} 메서드를 구현하여 그래픽 요소를 정의
   * {@link GraphicOverlay # add (Graphic)}를 사용하여 오버레이에 인스턴스를 추가
   */
  public abstract static class Graphic {
    private GraphicOverlay overlay;

    public Graphic(GraphicOverlay overlay) {
      this.overlay = overlay;
    }

    /**
     *제공된 캔버스에 그래픽을 그리기. 그리기는 다음 방법을 사용하여 그려진 그래픽의보기 좌표로 변환해야함.
     *
     * <ol>
     *   <li>{@link Graphic#scale(float)} adjusts the size of the supplied value from the image
     *       scale to the view scale.
     *   <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)} adjust the
     *       coordinate from the image's coordinate system to the view coordinate system.
     * </ol>
     *
     * @param canvas drawing canvas
     */
    public abstract void draw(Canvas canvas);

    /** 제공된 값을 이미지 스케일에서 뷰 스케일로 조정 */
    public float scale(float imagePixel) {
      return imagePixel * overlay.scaleFactor;
    }

    /** 앱의 애플리케이션 컨텍스트를 반환 */
    public Context getApplicationContext() {
      return overlay.getContext().getApplicationContext();
    }

    public boolean isImageFlipped() {
      return overlay.isImageFlipped;
    }

    /**
     * 이미지의 좌표계에서 뷰 좌표계로 x 좌표를 조정
     */
    public float translateX(float x) {
      if (overlay.isImageFlipped) {
        return overlay.getWidth() - (scale(x) - overlay.postScaleWidthOffset);
      } else {
        return scale(x) - overlay.postScaleWidthOffset;
      }
    }

    /**
     * 이미지의 좌표계에서 뷰 좌표계로 y 좌표를 조정
     */
    public float translateY(float y) {
      return scale(y) - overlay.postScaleHeightOffset;
    }

    /**
     * Returns a {@link Matrix} for transforming from image coordinates to overlay view coordinates.
     */
    public Matrix getTransformationMatrix() {
      return overlay.transformationMatrix;
    }

    public void postInvalidate() {
      overlay.postInvalidate();
    }
  }

  public GraphicOverlay(Context context, AttributeSet attrs) {
    super(context, attrs);
    addOnLayoutChangeListener(
        (view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) ->
            needUpdateTransformation = true);
  }

  /** 오버레이에서 모든 그래픽을 제거 */
  public void clear() {
    synchronized (lock) {
      graphics.clear();
    }
    postInvalidate();
  }

  /** 오버레이에 그래픽을 추가 */
  public void add(Graphic graphic) {
    synchronized (lock) {
      graphics.add(graphic);
    }
  }

  /**오버레이에서 그래픽을 제거 */
  public void remove(Graphic graphic) {
    synchronized (lock) {
      graphics.remove(graphic);
    }
    postInvalidate();
  }

  /**
   * 나중에 이미지 좌표를 변환하는 방법을 알려주는 크기 및 뒤집기 여부를 포함하여 감지기에서 처리중인 이미지의 소스 정보를 설정
   *
   * @param imageWidth the width of the image sent to ML Kit detectors
   * @param imageHeight the height of the image sent to ML Kit detectors
   * @param isFlipped whether the image is flipped. Should set it to true when the image is from the
   *     front camera.
   */
  public void setImageSourceInfo(int imageWidth, int imageHeight, boolean isFlipped) {
    Preconditions.checkState(imageWidth > 0, "image width must be positive");
    Preconditions.checkState(imageHeight > 0, "image height must be positive");
    synchronized (lock) {
      this.imageWidth = imageWidth;
      this.imageHeight = imageHeight;
      this.isImageFlipped = isFlipped;
      needUpdateTransformation = true;
    }
    postInvalidate();
  }

  public int getImageWidth() {
    return imageWidth;
  }

  public int getImageHeight() {
    return imageHeight;
  }

  private void updateTransformationIfNeeded() {
    if (!needUpdateTransformation || imageWidth <= 0 || imageHeight <= 0) {
      return;
    }
    float viewAspectRatio = (float) getWidth() / getHeight();
    float imageAspectRatio = (float) imageWidth / imageHeight;
    postScaleWidthOffset = 0;
    postScaleHeightOffset = 0;
    if (viewAspectRatio > imageAspectRatio) {
      // 이 뷰에 표시하려면 이미지를 세로이어야함
      scaleFactor = (float) getWidth() / imageWidth;
      postScaleHeightOffset = ((float) getWidth() / imageAspectRatio - getHeight()) / 2;
    } else {
      // 이 뷰에 표시하려면 이미지를 가로로
      scaleFactor = (float) getHeight() / imageHeight;
      postScaleWidthOffset = ((float) getHeight() * imageAspectRatio - getWidth()) / 2;
    }

    transformationMatrix.reset();
    transformationMatrix.setScale(scaleFactor, scaleFactor);
    transformationMatrix.postTranslate(-postScaleWidthOffset, -postScaleHeightOffset);

    if (isImageFlipped) {
      transformationMatrix.postScale(-1f, 1f, getWidth() / 2f, getHeight() / 2f);
    }

    needUpdateTransformation = false;
  }

  /** 연관된 그래픽 객체로 오버레이를 그림 */
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    synchronized (lock) {
      updateTransformationIfNeeded();

      for (Graphic graphic : graphics) {
        graphic.draw(canvas);
      }
    }
  }
}
