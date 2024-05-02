package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TFLiteClassifier {
    private Interpreter interpreter;
    private final Context context;

    public TFLiteClassifier(Context context) {
        this.context = context;
        try {
            interpreter = new Interpreter(loadModelFile("model.tflite"), null);
        } catch (IOException e) {
            // Handle the exception
        }
    }

    private MappedByteBuffer loadModelFile(String modelName) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelName);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public String classify(Bitmap image) {
        Bitmap resizedImage = Bitmap.createScaledBitmap(image, 128, 128, true);
        ByteBuffer byteBuffer = convertBitmapToByteBuffer(resizedImage);

        // Assuming model output is a float array of probabilities
        float[][] result = new float[1][1]; // Update this depending on your model's output
        interpreter.run(byteBuffer, result);

        return parseResult(result);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 128 * 128 * 3);
        byteBuffer.rewind();
        int[] intValues = new int[128 * 128];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int pixelValue : intValues) {
            byteBuffer.putFloat(((pixelValue >> 16) & 0xFF) / 255f);
            byteBuffer.putFloat(((pixelValue >> 8) & 0xFF) / 255f);
            byteBuffer.putFloat((pixelValue & 0xFF) / 255f);
        }
        return byteBuffer;
    }

    private String parseResult(float[][] result) {
        // Process the result array to human-readable form
        // For example, decoding based on your specific model's output
        return "Sample result"; // Modify according to your logic
    }

    public void close() {
        if (interpreter != null) {
            interpreter.close();
        }
    }
}
