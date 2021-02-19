
package com.example.daily_function.automl;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.example.daily_function.GraphicOverlay;
import com.example.daily_function.VisionProcessorBase;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerLocalModel;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerOptions;
import java.io.IOException;
import java.util.List;


public class AutoMLImageLabelerProcessor extends VisionProcessorBase<List<ImageLabel>> {

  private static final String TAG = "AutoMLProcessor";

  private final ImageLabeler imageLabeler;

  public AutoMLImageLabelerProcessor(Context context) {
    super(context);
    Log.d(TAG, "Local model used.");
    //LocalModel 객체를 만들고 모델 매니페스트 파일의 경로를 지정
    AutoMLImageLabelerLocalModel localModel =
        new AutoMLImageLabelerLocalModel.Builder().setAssetFilePath("automl/manifest.json").build();
    imageLabeler =
        ImageLabeling.getClient(
            new AutoMLImageLabelerOptions.Builder(localModel).setConfidenceThreshold(0).build());
  }

  @Override
  public void stop() {
    super.stop();
    try {
      imageLabeler.close();
    } catch (IOException e) {
      Log.e(TAG, "Exception thrown while trying to close the image labeler", e);
    }
  }

  @Override
  protected Task<List<ImageLabel>> detectInImage(InputImage image) {
    return imageLabeler.process(image);
  }

  @Override
  protected void onSuccess(
      @NonNull List<ImageLabel> labels, @NonNull GraphicOverlay graphicOverlay) {
    graphicOverlay.add(new LabelGraphic(graphicOverlay, labels));
  }

  @Override
  protected void onFailure(@NonNull Exception e) {
    Log.w(TAG, "Label detection failed.", e);
  }
}

