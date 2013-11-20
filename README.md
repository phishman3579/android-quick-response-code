android-quick-response-code
===========================

Android QR Code Decoder and Encoder

## Introduction

This is a port of the ZXing (version 2.1) project but reduced in size and scope. It can be used as a direct call from any Android project instead of using the ZXing Intents mechanism.

This project is an attempt to make working with QR codes in Android a bit easier.

* Created by Justin Wetherell
* Google:   http://code.google.com/p/android-quick-response-code
* Github:   http://github.com/phishman3579/android-quick-response-code
* LinkedIn: http://www.linkedin.com/in/phishman3579
* E-mail:   phishman3579@gmail.com
* Twitter:  http://twitter.com/phishman3579

## Encoding

You can encode a String fairly easily using the following code:

### Encode with a QR Code

    //Encode with a QR Code image
    QRCodeEncoder qrCodeEncoder = new QRCodeEncoder("Hello", 
                                                    null, 
                                                    Contents.Type.TEXT,  
                                                    BarcodeFormat.QR_CODE.toString(), 
                                                    smallerDimension);
    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();


### Encode with a Barcode

    //Encode with a QR Code image
    QRCodeEncoder qrCodeEncoder = new QRCodeEncoder("12345678910", 
                                                    null, 
                                                    Contents.Type.TEXT, 
                                                    BarcodeFormat.UPC_A.toString(), 
                                                    smallerDimension);
    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();


### Encoding Activity Example

You can see an example of how to ecode a string as a Bitmap and display it on the screen by following along with the EncoderActivity.java file.

## Decoding

You can decode what is being displayed by the camera by extending Activity and implementing both IDecoderActivity and SurfaceHolder.Callback. An easier option is just to extend the DecoderActivity and override the handleDecode() method, the CaptureActivity does exactly that and is a good example to follow.

    //When you initialize the camera, start the DecoderActivityHandler
    //decodeFormarts is a Collection of BarcodeFormat objects to decipher. NULL means all.
    //characterSet should be NULL to get a callbak
    //cameraManager is used to control the preview
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        DecoderActivityHandler handler = new DecoderActivityHandler(this, 
                                                                    decodeFormats, 
                                                                    characterSet, 
                                                                    cameraManager);
    }

    //handleDecode() is called when a code has been deciphered.
    @Override 
    public void handleDecode(Result rawResult, Bitmap barcode) {
        drawResultPoints(barcode, rawResult);
        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
        handleDecodeInternally(rawResult, resultHandler, barcode);
    }

### Decoding and Catpure Activity Example

You can see an example of how to decode an image by following along with the DecoderActivity.java file. You will also find a minimal example of how to decode an image and display it's results by following the CaptureActivity.java file.

## Decoding Formats
* Aztec 2D barcode format.
* CODABAR 1D format.
* Code 39 1D format.
* Code 93 1D format.
* Code 128 1D format.
* Data Matrix 2D barcode format.
* EAN-8 1D format.
* EAN-13 1D format.
* ITF (Interleaved Two of Five) 1D format.
* MaxiCode 2D barcode format.
* PDF417 format.
* QR Code 2D barcode format.
* RSS 14 format.
* RSS EXPANDED format.
* UPC-A 1D format.
* UPC-E 1D format.
* UPC/EAN extension format. Not a stand-alone format.

## Encoding Formats
* CODABAR 1D format.
* Code 128 1D format.
* Code 39 1D format.
* EAN-8 1D format.
* EAN-13 1D format.
* ITF (Interleaved Two of Five) 1D format.
* PDF417 format.
* QR Code 2D barcode format.
* UPC-A 1D format.

