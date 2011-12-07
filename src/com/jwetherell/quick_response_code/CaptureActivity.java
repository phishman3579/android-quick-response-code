/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jwetherell.quick_response_code;

import java.io.IOException;
import java.util.Collection;

import com.jwetherell.quick_response_code.R;
import com.jwetherell.quick_response_code.camera.CameraManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * Example Decoder Activity
 * 
 * @author Justin Wetherell (phishman3579@gmail.com)
 */
public class CaptureActivity extends Activity implements IDecoderActivity, SurfaceHolder.Callback {
    private static final String TAG = CaptureActivity.class.getSimpleName();

    private DecoderActivityHandler handler = null;
    
    private ViewfinderView viewfinderView = null;
    private SurfaceView surfaceView = null;

    private CameraManager cameraManager = null;
    private boolean hasSurface = false;
    private Collection<BarcodeFormat> decodeFormats = null;
    private String characterSet = null;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.capture);

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        
        handler = null;
        hasSurface = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is necessary because we
        // don't want to open the camera driver and measure the screen size if we're going to show
        // the help on first launch. That led to bugs where the scanning rectangle was the wrong size
        // and partially off screen.
        cameraManager = new CameraManager(getApplication());
        viewfinderView.setCameraManager(cameraManager);

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }

        cameraManager.closeDriver();
        
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            // Handle these events so they don't launch the Camera app
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Ignore
    }

    @Override
    public ViewfinderView getViewfinder() {
        return viewfinderView;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode) {
        //TODO: Handle the Result or Display the Bitmap
        viewfinderView.setVisibility(View.GONE);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) handler = new DecoderActivityHandler(this, decodeFormats, characterSet, cameraManager);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
        }
    }
}
