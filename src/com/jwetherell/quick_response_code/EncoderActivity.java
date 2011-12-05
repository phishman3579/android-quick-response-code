package com.jwetherell.quick_response_code;

import android.view.Display;
import android.view.WindowManager;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwetherell.quick_response_code.R;
import com.jwetherell.quick_response_code.core.BarcodeFormat;
import com.jwetherell.quick_response_code.core.WriterException;
import com.jwetherell.quick_response_code.encode.QRCodeEncoder;


/**
 * 
 * 
 * @author Justin Wetherell (phishman3579@gmail.com)
 */
public final class EncoderActivity extends Activity {
    private static final String TAG = EncoderActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.encoder);

        // This assumes the view is full screen, which is a good assumption
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 7 / 8;

        try {
            QRCodeEncoder qrCodeEncoder = null;
            qrCodeEncoder = new QRCodeEncoder("Hello",
                                              new Bundle(),
                                              Contents.Type.TEXT,
                                              BarcodeFormat.QR_CODE.toString(),
                                              smallerDimension);
            /*
            qrCodeEncoder = new QRCodeEncoder("12345678910",
                                              new Bundle(),
                                              Contents.Type.TEXT,
                                              BarcodeFormat.UPC_A.toString(),
                                              smallerDimension);
            */
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ImageView view = (ImageView) findViewById(R.id.image_view);
            view.setImageBitmap(bitmap);

            TextView contents = (TextView) findViewById(R.id.contents_text_view);
            contents.setText(qrCodeEncoder.getDisplayContents());
            setTitle(getString(R.string.app_name) + " - " + qrCodeEncoder.getTitle());
        } catch (WriterException e) {
            Log.e(TAG, "Could not encode barcode", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Could not encode barcode", e);
        }
    }
}
