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

package com.example.daily_function.posedetector;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.daily_function.ExerciseCount;
import com.example.daily_function.GraphicOverlay;
import com.example.daily_function.GraphicOverlay.Graphic;
import com.example.daily_function.InferenceInfoGraphic;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.atan2;

/** Draw the detected pose in preview. */
public class PoseGraphic extends Graphic {

  private static final float DOT_RADIUS = 8.0f;
  private static final float IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f;

  private final Pose pose;
  private final boolean showInFrameLikelihood;
  private final Paint leftPaint;
  private final Paint rightPaint;
  private final Paint whitePaint;
 // int cnt= ExerciseCount.cnt;
 private static int cnt = 0;
  
  private static final int TEXT_COLOR = Color.BLUE;
  public static final float TEXT_SIZE = 80.0f;
  private final Paint textPaint;

  PoseGraphic(GraphicOverlay overlay, Pose pose, boolean showInFrameLikelihood) {
    super(overlay);

    this.pose = pose;
    this.showInFrameLikelihood = showInFrameLikelihood;
    whitePaint = new Paint();
    whitePaint.setColor(Color.WHITE);
    whitePaint.setTextSize(IN_FRAME_LIKELIHOOD_TEXT_SIZE);
    leftPaint = new Paint();
    leftPaint.setColor(Color.GREEN);
    rightPaint = new Paint();
    rightPaint.setColor(Color.YELLOW);

    textPaint = new Paint();
    textPaint.setColor(TEXT_COLOR);
    textPaint.setTextSize(TEXT_SIZE);
  }

  @Override
  public void draw(Canvas canvas) {
    float x = TEXT_SIZE * 1.5f;
    float y = TEXT_SIZE * 1.5f;

    List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();
    if (landmarks.isEmpty()) {
      return;
    }
    // 모든 랜드마크 포인터 그리기
    for (PoseLandmark landmark : landmarks) {
      drawPoint(canvas, landmark.getPosition(), whitePaint);
      if (showInFrameLikelihood) {
        canvas.drawText(
            String.format(Locale.US, "%.2f", landmark.getInFrameLikelihood()),
            translateX(landmark.getPosition().x),
            translateY(landmark.getPosition().y),
            whitePaint);
      }
    }
    PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.Type.LEFT_SHOULDER);
    PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.Type.RIGHT_SHOULDER);
    PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.Type.LEFT_ELBOW);
    PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.Type.RIGHT_ELBOW);
    PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.Type.LEFT_WRIST);
    PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.Type.RIGHT_WRIST);
    PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.Type.LEFT_HIP);
    PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.Type.RIGHT_HIP);
    PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.Type.LEFT_KNEE);
    PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.Type.RIGHT_KNEE);
    PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.Type.LEFT_ANKLE);
    PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.Type.RIGHT_ANKLE);

    PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.Type.LEFT_PINKY);
    PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.Type.RIGHT_PINKY);
    PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.Type.LEFT_INDEX);
    PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.Type.RIGHT_INDEX);
    PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.Type.LEFT_THUMB);
    PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.Type.RIGHT_THUMB);
    PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.Type.LEFT_HEEL);
    PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.Type.RIGHT_HEEL);
    PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.Type.LEFT_FOOT_INDEX);
    PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.Type.RIGHT_FOOT_INDEX);

    drawLine(canvas, leftShoulder.getPosition(), rightShoulder.getPosition(), whitePaint);
    drawLine(canvas, leftHip.getPosition(), rightHip.getPosition(), whitePaint);

    // Left body
    drawLine(canvas, leftShoulder.getPosition(), leftElbow.getPosition(), leftPaint);
    drawLine(canvas, leftElbow.getPosition(), leftWrist.getPosition(), leftPaint);
    drawLine(canvas, leftShoulder.getPosition(), leftHip.getPosition(), leftPaint);
    drawLine(canvas, leftHip.getPosition(), leftKnee.getPosition(), leftPaint);
    drawLine(canvas, leftKnee.getPosition(), leftAnkle.getPosition(), leftPaint);
    drawLine(canvas, leftWrist.getPosition(), leftThumb.getPosition(), leftPaint);
    drawLine(canvas, leftWrist.getPosition(), leftPinky.getPosition(), leftPaint);
    drawLine(canvas, leftWrist.getPosition(), leftIndex.getPosition(), leftPaint);
    drawLine(canvas, leftAnkle.getPosition(), leftHeel.getPosition(), leftPaint);
    drawLine(canvas, leftHeel.getPosition(), leftFootIndex.getPosition(), leftPaint);

    // Right body
    drawLine(canvas, rightShoulder.getPosition(), rightElbow.getPosition(), rightPaint);
    drawLine(canvas, rightElbow.getPosition(), rightWrist.getPosition(), rightPaint);
    drawLine(canvas, rightShoulder.getPosition(), rightHip.getPosition(), rightPaint);
    drawLine(canvas, rightHip.getPosition(), rightKnee.getPosition(), rightPaint);
    drawLine(canvas, rightKnee.getPosition(), rightAnkle.getPosition(), rightPaint);
    drawLine(canvas, rightWrist.getPosition(), rightThumb.getPosition(), rightPaint);
    drawLine(canvas, rightWrist.getPosition(), rightPinky.getPosition(), rightPaint);
    drawLine(canvas, rightWrist.getPosition(), rightIndex.getPosition(), rightPaint);
    drawLine(canvas, rightAnkle.getPosition(), rightHeel.getPosition(), rightPaint);
    drawLine(canvas, rightHeel.getPosition(), rightFootIndex.getPosition(), rightPaint);



    double rightElbowAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.Type.RIGHT_WRIST),
            pose.getPoseLandmark(PoseLandmark.Type.RIGHT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.Type.RIGHT_SHOULDER));
    Log.d("PoseGraphic.class", "rightElbowAngle11 각도: " + rightElbowAngle);

    double leftElbowAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.Type.LEFT_WRIST),
            pose.getPoseLandmark(PoseLandmark.Type.LEFT_ELBOW),
            pose.getPoseLandmark(PoseLandmark.Type.LEFT_SHOULDER));
    Log.d("PoseGraphic.class", "leftElbowAngle11 각도: " + leftElbowAngle);


    //스쿼트 내력갈때 옆모습일경우 무릎각도
    double rightKneeAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.Type.RIGHT_HIP),
            pose.getPoseLandmark(PoseLandmark.Type.RIGHT_KNEE),
            pose.getPoseLandmark(PoseLandmark.Type.RIGHT_ANKLE));


    double leftKneeAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.Type.LEFT_HIP),
            pose.getPoseLandmark(PoseLandmark.Type.LEFT_KNEE),
            pose.getPoseLandmark(PoseLandmark.Type.LEFT_ANKLE));

    //스쿼트 내려갈때 정면일 경우 엉덩이 각도
    double rightHipAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.Type.RIGHT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.Type.RIGHT_HIP),
            pose.getPoseLandmark(PoseLandmark.Type.RIGHT_KNEE));

    double leftHipAngle = getAngle(
            pose.getPoseLandmark(PoseLandmark.Type.LEFT_SHOULDER),
            pose.getPoseLandmark(PoseLandmark.Type.LEFT_HIP),
            pose.getPoseLandmark(PoseLandmark.Type.LEFT_KNEE));
    Log.d("PoseGraphic.class","오른쪽 엉덩이 각도:"+rightHipAngle);
    Log.d("PoseGraphic.class","왼족 엉덩이 각도:"+leftHipAngle);

    double leftLeg=getDistnce(pose.getPoseLandmark(PoseLandmark.Type.LEFT_HIP),
            pose.getPoseLandmark(PoseLandmark.Type.LEFT_ANKLE));
  //  double tempLeftLeg=leftLeg/2;

    double rightLeg=getDistnce(pose.getPoseLandmark(PoseLandmark.Type.RIGHT_HIP),
            pose.getPoseLandmark(PoseLandmark.Type.RIGHT_ANKLE));
  //  double tempRightLeg=rightLeg/2;

    //준비자세
    if(rightElbowAngle<=90 && leftElbowAngle<=90){
    //  canvas.drawText("준비자세완료" , x, y, textPaint);
      Log.d("PoseGraphic.class", "준비자세완료 " + "오른쪽 각도:"+rightElbowAngle +" 왼쪽 각도:"+leftElbowAngle);


      //내려가는 자세
      if((rightKneeAngle<=90 || leftKneeAngle<=90) || (rightHipAngle<=135 && leftHipAngle<=135)){
        cnt++;

       // canvas.drawText("내려가는중" , x, y, textPaint);;
      //  canvas.drawText("개수: " + cnt, x, y, textPaint);
        Log.d("PoseGraphic.class", "내려가는중"+"rightKneeAngle 각도: " + rightKneeAngle+ " leftKneeAngle 각도: " + leftKneeAngle);
        Log.d("PoseGraphic.class","왼쪽 골반각도"+leftHipAngle+"오른쪽 골반각도"+rightHip);
        Log.d("PoseGraphic.class", "갯수: " + cnt);


        //올라가는 자세
        if((rightKneeAngle>90 && leftKneeAngle>90)|| (rightHipAngle>135 && leftHipAngle>135)){
          Log.d("PoseGraphic.class", "올라가는중"+"rightKneeAngle 각도: " + rightKneeAngle+ " leftKneeAngle 각도: " + leftKneeAngle);

          if(cnt==12) {
            canvas.drawText("달성! ", x, y, textPaint);
            //ExerciseCount.cnt = 0;
            cnt=0;
          }

        }
      }

      //내려가는 자세가 부정확 할 때
     // else  canvas.drawText("자세가 틀렸습니다." , x, y, textPaint);

    }

    else{
      Toast.makeText(this.getApplicationContext(),"카메라 정면을 바라보고 양 손을 머리 뒤로 해주세요:"+rightElbowAngle,Toast.LENGTH_SHORT);
      Log.d("PoseGraphic.class", "오른쪽 각도가 큼: " + rightElbowAngle);
      Log.d("PoseGraphic.class", "왼쪽쪽 각도가 큼: " + leftElbowAngle);
    }

    canvas.drawText("개수: " +cnt, x, y, textPaint);

  }

  void drawPoint(Canvas canvas, @Nullable PointF point, Paint paint) {
    if (point == null) {
      return;
    }
    canvas.drawCircle(translateX(point.x), translateY(point.y), DOT_RADIUS, paint);
  }

  void drawLine(Canvas canvas, @Nullable PointF start, @Nullable PointF end, Paint paint) {
    if (start == null || end == null) {
      return;
    }
    canvas.drawLine(
        translateX(start.x), translateY(start.y), translateX(end.x), translateY(end.y), paint);
  }

  //각도가 0도에서 180도 사이 구하기
  //두 점 사이의 절대각도를 잼: atan2
  static double getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
    double result =
            Math.toDegrees(
                    atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                            lastPoint.getPosition().x - midPoint.getPosition().x)
                            - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                            firstPoint.getPosition().x - midPoint.getPosition().x));
    result = Math.abs(result); // 음수는 될 수 가 없으므로
    if (result > 180) {
      result = (360.0 - result); // 정확하게
    }
    return result;
  }

  static double getDistnce(PoseLandmark firstPoint, PoseLandmark lastPoint){
    double result=Math.sqrt(Math.pow(lastPoint.getPosition().y-firstPoint.getPosition().y,2)+Math.pow(lastPoint.getPosition().x-firstPoint.getPosition().x,2));

    return result;

  }

}
