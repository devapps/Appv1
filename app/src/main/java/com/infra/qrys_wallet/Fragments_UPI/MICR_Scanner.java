package com.infra.qrys_wallet.Fragments_UPI;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.infra.qrys_wallet.Fragments.SetPaymentAddressFragment;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.SharedPreference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import leadtools.ILeadStream;
import leadtools.L_ERROR;
import leadtools.LeadRect;
import leadtools.LeadSize;
import leadtools.LeadStreamFactory;
import leadtools.RasterImage;
import leadtools.codecs.CodecsLoadAsyncCompletedEvent;
import leadtools.codecs.CodecsLoadAsyncCompletedListener;
import leadtools.codecs.RasterCodecs;
import leadtools.controls.CoordinateType;
import leadtools.controls.ImageViewerNewImageResetOptions;
import leadtools.controls.ImageViewerRubberBandEvent;
import leadtools.controls.ImageViewerRubberBandInteractiveMode;
import leadtools.controls.ImageViewerRubberBandListener;
import leadtools.controls.ImageViewerSizeMode;
import leadtools.controls.RasterImageViewer;
import leadtools.converters.ConvertFromImageOptions;
import leadtools.converters.ConvertToImageOptions;
import leadtools.converters.RasterImageConverter;
import leadtools.demos.DeviceUtils;
import leadtools.demos.Messager;
import leadtools.demos.OpenFileDialog;
import leadtools.demos.Progress;
import leadtools.demos.Support;
import leadtools.demos.Utils;
import leadtools.forms.ocr.OcrAutoPreprocessPageCommand;
import leadtools.forms.ocr.OcrDocument;
import leadtools.forms.ocr.OcrEngine;
import leadtools.forms.ocr.OcrEngineManager;
import leadtools.forms.ocr.OcrEngineType;
import leadtools.forms.ocr.OcrMicrData;
import leadtools.forms.ocr.OcrPage;
import leadtools.forms.ocr.OcrPageCharacters;
import leadtools.forms.ocr.OcrPageType;
import leadtools.forms.ocr.OcrSettingManager;
import leadtools.forms.ocr.OcrWord;
import leadtools.forms.ocr.OcrZone;
import leadtools.forms.ocr.OcrZoneCharacters;
import leadtools.forms.ocr.OcrZoneFillMethod;
import leadtools.imageprocessing.core.MICRCodeDetectionCommand;

import static android.hardware.Camera.AutoFocusCallback;
import static android.hardware.Camera.PreviewCallback;
import static android.hardware.Camera.getNumberOfCameras;
import static android.hardware.Camera.open;

/**
 * A simple {@link Fragment} subclass.
 */
public class MICR_Scanner extends Fragment implements SurfaceHolder.Callback {
    private static final int IMAGE_GALLERY = 0x0001;
    private static final int IMAGE_CAPTURE = 0x0002;
    private static final long IMAGE_PROCESS_DELAY = 1; //10;
    String ChequeNum;
    private static final long AUTO_FOCUS_DELAY = 2000;

    private static final String OCR_TEMP_DIRECTORY = Environment.getExternalStorageDirectory() + "/LEADTOOLS Demos/MICR_DEMO/";
    private static final String OCR_RUNTIME_DIRECTORY = OCR_TEMP_DIRECTORY + "OCRRuntime/";
    private static final String CAPTURED_IMAGE_TEMP_DIRECTORY = Environment.getExternalStorageDirectory() + "/LEADTOOLS Demos/MICR_DEMO/";
    private static final int MicrExtractionMode_Strict = 0;
    private static final int MicrExtractionMode_Relaxed = 1;

    private Uri mImageCaptureUri;
    RelativeLayout viewsContainer;
    private boolean mStartLiveCapture;
    public static boolean chequePaymentAddress=false;

    FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction ft;
    String MICRResulttobeSent,flagCreateCheque,flagReceiveMoneyviaCheq;

    private Rect mLiveCaptureRect;
    private Paint mLiveCapturePaint;
    private Paint mLiveCapturePaintMicr;
    private Paint mTextPaint;
    private Paint mRectPaint;
    private Camera mCamera; // for live capture

    private RasterImageViewer mImageViewer;
    private View mOverlayView;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private ProgressDialog mProgressDlg;
    private boolean mShowProgress;

    private float mHorizontalMargins;
    private float mVerticalMargins;

    private float mInstructionLabelWidth;
    private float mInstructionLabelHeight;
    private float mInstructionLabelLeft;
    private float mInstructionLabelTop;

    private float mCameraGuideLeft;
    private float mCameraGuideTop;
    private float mCameraGuideWidth;
    private float mCameraGuideHeight;
    private float mVerticalStrokeWidth;
    private float mMicrClearBandHeight;
    private Rect mMicrAreaBounds;
    private RectF mMicrNoteBounds;
    private static final String mMicrNoteLine1 = "Make sure the MICR code fits within the MICR Area guides.";
    private static final String mMicrNoteLine2 = "Also ensure there is plenty of light on the cheque.";
    private static final String mMicrAreaNote = "MICR Area";
    private int mMicrNoteTop;
    private int mMicrNoteLeftLine1;
    private int mMicrNoteLeftLine2;
    private int mMicrAreaNoteLeft;
    private boolean mDrawMicrGuides;
    private Button mReadMicrBtn;
    private Button mReadAreaBtn;
    private OcrEngine mOcrEngine;
    private int mMicrVideoExtractionMode;
    private int mMicrPictureExtractionMode;
    private boolean mIsWorking;
    private LeadRect mMicrReadAreaBounds;
    View rootView;
    SharedPreference appPref;
    public static final int RESULT_OK           = -1;
    private Context context;
    public MICR_Scanner() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
         rootView = inflater.inflate(R.layout.fragment_micr__scanner, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mReadMicrBtn = (Button) rootView.findViewById(R.id.btn_recognize_micr);
        mReadAreaBtn = (Button) rootView.findViewById(R.id.btn_recognize_zone);
        context = getActivity();
        // Set License
        Support.setLicense(getActivity());

        flagCreateCheque= appPref.getInstance().getString("flagCreateCheque");
        flagReceiveMoneyviaCheq= appPref.getInstance().getString("flagReceiveMoneyviaCheq");

        System.out.println("flagCreateCheque " + flagCreateCheque);
        System.out.println("flagReceiveMoneyviaCheq " + flagReceiveMoneyviaCheq);
        mLiveCaptureRect = new Rect();
        mMicrAreaBounds = new Rect();
        mMicrNoteBounds = new RectF();
        mLiveCapturePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLiveCapturePaint.setColor(Color.RED);
        mLiveCapturePaint.setStrokeWidth(3);
        mLiveCapturePaint.setStyle(Paint.Style.STROKE);

        mLiveCapturePaintMicr = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLiveCapturePaintMicr.setColor(Color.RED);
        mLiveCapturePaintMicr.setStrokeWidth(3);
        mLiveCapturePaintMicr.setStyle(Paint.Style.STROKE);
        mLiveCapturePaintMicr.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(20);

        mRectPaint = new Paint();
        mRectPaint.setColor(Color.DKGRAY);
        mRectPaint.setAlpha(128);

        mImageViewer = (RasterImageViewer) rootView.findViewById(R.id.imageviewer);
        RelativeLayout viewsContainer = (RelativeLayout) rootView.findViewById(R.id.relativelayout_viewerscontainer);

        // Set conversion options
        mImageViewer.setConvertToImageOptions(ConvertToImageOptions.LINK_IMAGE.getValue());

        mImageViewer.setNewImageResetOptions(ImageViewerNewImageResetOptions.NONE.getValue());
        mImageViewer.setSizeMode(ImageViewerSizeMode.FIT);
        // Set Dpi
        mImageViewer.setUseDpi(true);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mImageViewer.setScreenDpiX(metrics.densityDpi);
        mImageViewer.setScreenDpiY(metrics.densityDpi);
        mDrawMicrGuides = false;


        // Init Overlay View
        mOverlayView = new View(getActivity()) {
            @Override
            public void onDraw(Canvas canvas) {
                super.onDraw(canvas);

                if (mDrawMicrGuides) {
                    canvas.drawRoundRect(mMicrNoteBounds, 10, 10, mRectPaint);
                    canvas.drawText(mMicrNoteLine1, mMicrNoteLeftLine1, mMicrNoteTop, mTextPaint);
                    canvas.drawText(mMicrNoteLine2, mMicrNoteLeftLine2, mMicrNoteTop * 2, mTextPaint);
                    canvas.drawText(mMicrAreaNote, mMicrAreaNoteLeft, (int) (mMicrAreaBounds.top + (mMicrAreaBounds.height() / 3.0) * 2.0), mTextPaint);

                    canvas.drawLine(mLiveCaptureRect.left, mLiveCaptureRect.top, mLiveCaptureRect.right, mLiveCaptureRect.top, mLiveCapturePaint);
                    canvas.drawLine(mLiveCaptureRect.left, mLiveCaptureRect.top, mLiveCaptureRect.left, mLiveCaptureRect.top + mVerticalStrokeWidth, mLiveCapturePaint);
                    canvas.drawLine(mLiveCaptureRect.right, mLiveCaptureRect.top, mLiveCaptureRect.right, mLiveCaptureRect.top + mVerticalStrokeWidth, mLiveCapturePaint);
                    canvas.drawLine(mLiveCaptureRect.left, mLiveCaptureRect.bottom, mLiveCaptureRect.left, mLiveCaptureRect.bottom - mVerticalStrokeWidth, mLiveCapturePaint);
                    canvas.drawLine(mLiveCaptureRect.right, mLiveCaptureRect.bottom, mLiveCaptureRect.right, mLiveCaptureRect.bottom - mVerticalStrokeWidth, mLiveCapturePaint);
                    canvas.drawLine(mLiveCaptureRect.left, mLiveCaptureRect.bottom, mLiveCaptureRect.right, mLiveCaptureRect.bottom, mLiveCapturePaint);

                    canvas.drawLine(mMicrAreaBounds.left, mMicrAreaBounds.top, mMicrAreaBounds.right, mMicrAreaBounds.top, mLiveCapturePaintMicr);
                }
            }
        };

        viewsContainer.addView(mOverlayView);

        mSurfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        // Set the holder type if the API is lower than 11
        if (Build.VERSION.SDK_INT < 11)
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mIsWorking = false;
        copyLanguageFiles();
        mMicrVideoExtractionMode = MicrExtractionMode_Strict;
        mMicrPictureExtractionMode = MicrExtractionMode_Relaxed;
        init();

        if (DeviceUtils.hasCamera(getActivity()))
            startLiveCapture();

        setMicrExtractMode();


        return rootView;
    }


    private boolean copyLanguageFiles() {
        try {
            Utils.createDirectory(OCR_RUNTIME_DIRECTORY);
            String assetsDirectoryName = "ocr_runtime";
            String[] languagesFilesNames = getActivity().getAssets().list(assetsDirectoryName);
            if(languagesFilesNames.length == 0)
                return false;

            for(String languageFile: languagesFilesNames) {
                String name = String.format("%s%s", OCR_RUNTIME_DIRECTORY, languageFile);
                File file = new File(name);
                if(!file.exists()) {
                    // copy the language file
                    InputStream is = getActivity().getAssets().open(assetsDirectoryName + "/" + languageFile);
                    OutputStream os = new FileOutputStream(name);
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                    }
                    os.close();
                    is.close();
                }
            }

            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    private void init() {
        try {
            RasterCodecs codecsForOCR = new RasterCodecs(Utils.getSharedLibsPath(getActivity()));
//         codecsForOCR.getOptions().getLoad().setXResolution(300);
//         codecsForOCR.getOptions().getLoad().setYResolution(300);

            mOcrEngine = OcrEngineManager.createEngine(OcrEngineType.Advantage);
            mOcrEngine.startup(codecsForOCR, "", OCR_RUNTIME_DIRECTORY, Utils.getSharedLibsPath(getActivity()));
            OcrSettingManager settingsManager = mOcrEngine.getSettingManager();
            settingsManager.setBooleanValue("Recognition.Preprocess.MobileImagePreprocess", true);
            settingsManager.setBooleanValue("Recognition.ShareOriginalImage", true);
            settingsManager.setBooleanValue("Recognition.ModifyProcessingImage", true);
            settingsManager.setBooleanValue("Recognition.Preprocess.UseZoningEngine", false);
        } catch(Exception ex) {
            Messager.showError(getActivity(), ex.getMessage(), "Error");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Restart live capture
        if(mStartLiveCapture) {
            startLiveCapture();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        viewsContainer.removeView(mOverlayView);

        if(isRemoving() && mImageViewer != null)
            mImageViewer.setImage(null);

        // Stop live capture
        if(mStartLiveCapture) {
            stopLiveCapture(false);
            mStartLiveCapture = true;
        }
    }



    public void onSelectImage(View v) {
        int id = v.getId();

        if(id == R.id.btn_image_live_capture) {
            if(mCamera != null)
                stopLiveCapture(true);
            else {
                if(!DeviceUtils.hasCamera(getActivity())) {
                    Messager.showError(getActivity(), "The device doesn't have a camera", null);
                    return;
                }

                startLiveCapture();
            }
        } else if(id == R.id.btn_image_gallery) {
            stopLiveCapture(true);
            Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            gallery.setType("image/*");
            startActivityForResult(gallery, IMAGE_GALLERY);
        } else if(id == R.id.btn_image_browse) {
            stopLiveCapture(true);
            if(!DeviceUtils.isMediaMounted()) {
                Messager.showError(getActivity(), "The sdcard is not mounted", null);
                return;
            }

            OpenFileDialog.OnFileSelectedListener onFileSelectedListener = new OpenFileDialog.OnFileSelectedListener() {
                @Override
                public void onFileSelected(String fileName) {
                    File file = new File(fileName);
                    if(file.exists())
                        loadImage(fileName);
                }
            };

            OpenFileDialog openDlg = new OpenFileDialog(getActivity(), Utils.getSupportedImagesFormatFilter(), onFileSelectedListener);
            openDlg.show();

        } else if (id == R.id.btn_image_capture) {
            stopLiveCapture(true);
            if(!DeviceUtils.isMediaMounted()) {
                Messager.showError(getActivity(), "The sdcard is not mounted", null);
                return;
            }
            else if(!DeviceUtils.hasCamera(getActivity())) {
                Messager.showError(getActivity(), "The device doesn't have a camera", null);
                return;
            }

            // Start the camera in a new thread to avoid starting while Live capture not release the camera yet
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Keep waiting until the camera released
                    while(mCamera != null) {
                    }

                    Utils.createDirectory(CAPTURED_IMAGE_TEMP_DIRECTORY);

                    mImageCaptureUri = Utils.getExtFileUri("", ".jpg", CAPTURED_IMAGE_TEMP_DIRECTORY);
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(camera, IMAGE_CAPTURE);
                }
            }).start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String imageFileName = null;
            switch (requestCode) {
                case IMAGE_GALLERY:
                    imageFileName = Utils.getGalleryPathName(getActivity().getContentResolver(), data.getData());
                    break;

                case IMAGE_CAPTURE:
                    String uriPath = mImageCaptureUri.getPath();
                    if(uriPath != null) {
                        File file = new File(uriPath);
                        if (file.exists())
                            imageFileName = uriPath;
                    }
                    break;

                default:
                    break;
            }

            if(imageFileName != null) {
                loadImage(imageFileName);
            }
        }
    }

    private void loadImage(String imageFileName) {
        try {
            mProgressDlg = Progress.show(getActivity(), "Load Image", "Loading");
            RasterCodecs codecs = new RasterCodecs(Utils.getSharedLibsPath(getActivity()));
            codecs.getOptions().getLoad().setXResolution(300);
            codecs.getOptions().getLoad().setYResolution(300);

            ILeadStream stream = LeadStreamFactory.create(imageFileName);

            codecs.addLoadAsyncCompletedListener(new CodecsLoadAsyncCompletedListener() {
                @Override
                public void onLoadAsyncCompleted(CodecsLoadAsyncCompletedEvent event) {
                    Progress.close(mProgressDlg);
                    if (event.getError() != null || event.getCancelled()) {
                        Messager.showError(getActivity(), event.getError().getMessage(), "Error loading file");
                    } else {
                        RasterImage image = event.getImage();
                        setImage(image);
                    }
                }
            });

            // Load image
            codecs.loadAsync(stream, null);
        } catch (Exception ex) {
            Progress.close(mProgressDlg);
            Messager.showError(getActivity(), ex.getMessage(), "Error loading file");
        }
    }

    private void startLiveCapture() {
        // Clear Image
        setImage(null);

        // Start live capture
        mStartLiveCapture = true;
        mReadMicrBtn.setEnabled(false);
        mReadAreaBtn.setEnabled(false);

        mSurfaceView.setVisibility(View.VISIBLE);
        setMicrExtractMode();
    }

    private void stopLiveCapture(boolean showProgress) {
        if(mStartLiveCapture) {
            // Clear Image
            setImage(null);

            mShowProgress = showProgress;
            mOverlayView.setVisibility(View.INVISIBLE);
            mSurfaceView.setVisibility(View.GONE);

            mReadMicrBtn.setEnabled(true);
            mReadAreaBtn.setEnabled(true);
        }

        setMicrExtractMode();
    }

    private void setImage(RasterImage image) {
        try {
            mImageViewer.setImage(image);
        } catch(Exception ex) {
            Messager.showError(getActivity(), ex.getMessage(), "");
        }
    }

    private void initCameraParameters(LeadSize size) {
        try {
            Camera.Parameters parameters = mCamera.getParameters();

            try {
                List<Integer> supportedPreviewFormatsList = parameters.getSupportedPreviewFormats();
                // Check for "JPEG" and "RGB_565" support
                if(supportedPreviewFormatsList != null && supportedPreviewFormatsList.size() < 0) {
                    if(supportedPreviewFormatsList.contains(ImageFormat.JPEG))
                        parameters.setPreviewFormat(ImageFormat.JPEG);
                    else if(supportedPreviewFormatsList.contains(ImageFormat.RGB_565))
                        parameters.setPreviewFormat(ImageFormat.RGB_565);
                }
            } catch(Exception ex) {
                Log.w("SetPreviewFormat", ex.getMessage());
            }

            try {
                List<Camera.Size> supportedPreviewSizesList = parameters.getSupportedPreviewSizes();
                Camera.Size previewSize = null;
                int sizeDiff = 0;
                if(supportedPreviewSizesList != null && supportedPreviewSizesList.size() > 0) {
                    for(int i = 1; i < supportedPreviewSizesList.size(); i++) {
                        Camera.Size temp = supportedPreviewSizesList.get(i);
                        int diff = Math.abs(temp.width - size.getWidth()) + Math.abs(temp.height - size.getHeight());
                        if (diff < sizeDiff || previewSize == null) {
                            sizeDiff = diff;
                            previewSize = temp;
                        }
                    }
                }

                if(previewSize != null) {
                    parameters.setPreviewSize(previewSize.width, previewSize.height);

                    // Update Rectangles
                    LeadRect bounds = new LeadRect(0, 0, size.getWidth(), size.getHeight());

                    mHorizontalMargins = bounds.getWidth() / 15;
                    mVerticalMargins = bounds.getHeight() / 15;

                    mInstructionLabelWidth = bounds.getWidth() - (mHorizontalMargins * 2);
                    mInstructionLabelHeight = (bounds.getHeight() / 8);
                    mInstructionLabelLeft = mHorizontalMargins;
                    mInstructionLabelTop = mVerticalMargins / 2;

                    mCameraGuideLeft = mInstructionLabelLeft;
                    mCameraGuideTop = mInstructionLabelTop + mInstructionLabelHeight + (mVerticalMargins / 2);
                    mCameraGuideWidth = mInstructionLabelWidth;
                    mCameraGuideHeight = bounds.getHeight() - mCameraGuideTop - mVerticalMargins;

                    mVerticalStrokeWidth = (float)(mCameraGuideHeight / 4.0);

                    mMicrClearBandHeight = mCameraGuideHeight / 5;

                    mLiveCaptureRect = new Rect((int)mCameraGuideLeft, (int)mCameraGuideTop, (int)(mCameraGuideWidth + mCameraGuideLeft), (int)(mCameraGuideHeight + mCameraGuideTop));

                    mMicrNoteTop = (int)(mLiveCaptureRect.top / 3.0);
                    mMicrNoteLeftLine1 = (int)(((mLiveCaptureRect.width()) / 2 + mHorizontalMargins)  - (mTextPaint.measureText(mMicrNoteLine1) / 2));
                    mMicrNoteLeftLine2 = (int)(((mLiveCaptureRect.width()) / 2 + mHorizontalMargins)  - (mTextPaint.measureText(mMicrNoteLine2) / 2));
                    mMicrAreaNoteLeft = (int)(((mLiveCaptureRect.width()) / 2 + mHorizontalMargins)  - (mTextPaint.measureText(mMicrAreaNote) / 2));

                    mDrawMicrGuides = true;

                    int micrTop = (int)(mCameraGuideHeight + mCameraGuideTop - mMicrClearBandHeight);
                    mMicrAreaBounds = new Rect((int)mCameraGuideLeft, micrTop, mLiveCaptureRect.right, (int)(mMicrClearBandHeight + micrTop));
                    mMicrNoteBounds = new RectF(mLiveCaptureRect.left, mMicrNoteTop - mTextPaint.getTextSize(), mLiveCaptureRect.right, mLiveCaptureRect.top - 10);

                    float ratioX = (float)size.getWidth() / previewSize.width;
                    float ratioY = (float)size.getHeight() / previewSize.height;

                    mMicrReadAreaBounds = LeadRect.fromLTRB(
                            (int)(mMicrAreaBounds.left / ratioX),
                            (int)(mMicrAreaBounds.top / ratioY),
                            (int)(mMicrAreaBounds.right / ratioX),
                            (int)(mMicrAreaBounds.bottom / ratioY));
                }
            } catch(Exception ex) {
                Log.w("SetPreviewSize", ex.getMessage());
                // hide the OverlayView and clear the crop rectangle
                mOverlayView.setVisibility(View.INVISIBLE);
            }

            try {
                List<String> supportedFlashModesList = parameters.getSupportedFlashModes();
                if (supportedFlashModesList != null && supportedFlashModesList.contains(Camera.Parameters.FLASH_MODE_OFF))
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            } catch(Exception ex) {
                Log.w("SetFlashMode", ex.getMessage());
            }

            mCamera.setParameters(parameters);
        } catch(Exception ex) {
            Log.w("SetParameters", ex.getMessage());
        }
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(!mStartLiveCapture || mCamera == null)
            return;

        try {
            Log.e("Test 0"," startpreview ");
            initCameraParameters(new LeadSize(width, height));
            mCamera.setDisplayOrientation(0);
            mCamera.startPreview();

            // Process Image (Delay 10 ms)
            mCamera.setOneShotPreviewCallback(new PreviewCallback() {

                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    // Check if live capture stopped
                    Log.e("Test 1"," startpreview ");
                    if (!mStartLiveCapture || mCamera == null)
                        return;

                    try {

                        Camera.Parameters parameters = camera.getParameters();

                        // Convert to Bitmap
                        Camera.Size previewSize = parameters.getPreviewSize();
                        byte[] imgData = null;

                        switch (parameters.getPreviewFormat()) {
                            // If the camera support "JPEG" or "RGB_565" use the data direct, if not use the default "NV21"
                            case ImageFormat.JPEG:

                            case ImageFormat.RGB_565:
                                imgData = data;

                                break;

                            default:

                                YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);
                                imgData = baos.toByteArray();
                                baos = null;
                                yuvimage = null;
                                break;
                        }

                        Bitmap bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                        RasterImage rasterImage = RasterImageConverter.convertFromBitmap(bitmap, ConvertFromImageOptions.NONE.getValue());

                        MicrResutls micrRes = detectAndRecognizeMicr(rasterImage, mMicrReadAreaBounds);

                        System.out.println("micrRes data "+micrRes.isMicrCodeDetected());

                        boolean found = false;
                        Log.e("Test 1.6"," startpreview ");
                        if (micrRes != null && micrRes.isMicrCodeDetected()) {
                            Log.e("Test 1.7"," startpreview ");
                            // Set start value to 'true' to restart live capture...
                            displayResults(micrRes, rasterImage, mMicrReadAreaBounds);
                            found = true;
                        }

                        // Free Data
                        rasterImage.dispose();
                        rasterImage = null;
                        bitmap = null;

                        if (!found) {
                            Log.e("Test 1.8"," startpreview ");
                            final PreviewCallback previewCallBack = this;
                            Timer oneShotTimer = new Timer();
                            oneShotTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {

                                    if (!mStartLiveCapture || mCamera == null)
                                        return;
                                    Log.e("Test 1.9"," startpreview ");
                                    mCamera.setOneShotPreviewCallback(previewCallBack);
                                }
                            }, IMAGE_PROCESS_DELAY);
                        }
                    } catch(Exception ex) {
                        stopLiveCapture(true);
                        Log.e("Test 10", " startpreview ");
                        Messager.showError(getActivity(), ex.getMessage(), "Live Capture");
                    }
                }
            });

            // Do AutoFocus every 2 seconds
            mCamera.autoFocus(new AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    final AutoFocusCallback autoFocusCallback = this;

                    Timer autoFocusTimer = new Timer();
                    autoFocusTimer.schedule(new TimerTask() {
                        public void run() {
                            if (!mStartLiveCapture || mCamera == null)
                                return;

                            mCamera.autoFocus(autoFocusCallback);
                        }
                    }, AUTO_FOCUS_DELAY);
                }
            });
        } catch (Exception ex) {
            Messager.showError(getActivity(), "Couldn't start live capture", "Live Capture");
            stopLiveCapture(true);
        }
    }


    public void surfaceCreated(SurfaceHolder holder) {
        if(!mStartLiveCapture || mCamera != null)
            return;

        try {
            mCamera = open();
            // if the device doesn't have a back-facing camera; "Camera.open()" will return null
            if(mCamera == null && Build.VERSION.SDK_INT >= 9) {
                // "Camera.open (int cameraId)" supported in API 9+
                mCamera = open(getNumberOfCameras() - 1);
            }
            mCamera.setPreviewDisplay(mSurfaceHolder);
            // show overlay view
            mOverlayView.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            Messager.showError(getActivity(), "Couldn't start live capture", "Live Capture");
            stopLiveCapture(true);
        }
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        if(!mStartLiveCapture || mCamera == null)
            return;

        mStartLiveCapture = false;
        if(mShowProgress) {
            mProgressDlg = Progress.show(getActivity(), null, "Stopping Live Capture");
            Thread stopCameraThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    stopCamera();
                    Progress.close(mProgressDlg);
                }
            });
            stopCameraThread.start();
        } else {
            stopCamera();
        }
    }

    private void stopCamera() {
        try {
            mCamera.cancelAutoFocus();
            mCamera.stopPreview();
            mCamera.setPreviewDisplay(null);
            mCamera.release();
        } catch (Exception ex) {
            Messager.showError(getActivity(), ex.getMessage(), "Live Capture");
        }
        mCamera = null;
    }

    private String getMicrZoneText(OcrZoneCharacters zoneCharacters) {
        List<OcrWord> words = zoneCharacters.getWords();
        if(words.size() > 0) {
            String micrLineText = new String();
            for (int i = 0; i < words.size(); i++) {
                OcrWord word = words.get(i);
                micrLineText += word.getValue();
                micrLineText += " ";
            }

            return micrLineText;
        }

        return null;
    }

    char[] micr_chars = {
            (char)0x2446, (char)0x2447, (char)0x2448, (char)0x2449, // MICR characters
            (char)0x0030, (char)0x0031, (char)0x0032, (char)0x0033,
            (char)0x0034, (char)0x0035, (char)0x0036, (char)0x0037,
            (char)0x0038, (char)0x0039};

    private String getMicrString(String micrText) {
        // We need to convert it, because in getActivity() font, a to d are the MICR symbols
        char[] newChars = new char[micrText.length()];
        for (int i = 0; i < newChars.length; i++)
        {
            char c = micrText.charAt(i);

            // check if it is a MICR symbol, convert it to a to d
            if (c >= 0x2446 && c <= 0x2449) {
                // Make it a + the difference
                newChars[i] = (char)('a' + (c - 0x2446));
            }
            else {
                // Add it as is
                newChars[i] = c;
            }
        }

        return new String(newChars);
    }

    boolean isStrictMode() {
        if (mStartLiveCapture)
            return mMicrVideoExtractionMode == MicrExtractionMode_Strict;
        else
            return mMicrPictureExtractionMode == MicrExtractionMode_Strict;
    }

    private void setMicrExtractMode() {
        boolean isStrictMode = isStrictMode();

        OcrSettingManager settingsManager = mOcrEngine.getSettingManager();
        settingsManager.setBooleanValue("Recognition.CharacterFilter.PostprocessMICR", isStrictMode);
        settingsManager.setBooleanValue("Recognition.Fonts.RecognizeFontAttributes", isStrictMode);
    }

    public void displayMICRSettings(final boolean isLiveCaptureStarted) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View settingsView = layoutInflater.inflate(R.layout.micr_settings, null);

        {
            final Spinner micrVideoExtarctionModeSpinner = (Spinner) settingsView.findViewById(R.id.spnr_video_micr_extraction_mode);
            micrVideoExtarctionModeSpinner.setSelection(mMicrVideoExtractionMode);
            micrVideoExtarctionModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0)
                        mMicrVideoExtractionMode = MicrExtractionMode_Strict;
                    else
                        mMicrVideoExtractionMode = MicrExtractionMode_Relaxed;

                    setMicrExtractMode();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });
        }

        {
            final Spinner micrPictureExtarctionModeSpinner = (Spinner) settingsView.findViewById(R.id.spnr_picture_micr_extraction_mode);
            micrPictureExtarctionModeSpinner.setSelection(mMicrPictureExtractionMode);
            micrPictureExtarctionModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0)
                        mMicrPictureExtractionMode = MicrExtractionMode_Strict;
                    else
                        mMicrPictureExtractionMode = MicrExtractionMode_Relaxed;

                    setMicrExtractMode();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });
        }

        AlertDialog.Builder resultsDlgBuilder = new AlertDialog.Builder(getActivity());
        resultsDlgBuilder.setView(settingsView);
        final AlertDialog resultsDlg = resultsDlgBuilder.create();
        resultsDlg.setTitle(R.string.settings_micr_extraction_mode);

        resultsDlg.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resultsDlg.dismiss();

                if (isLiveCaptureStarted)
                    startLiveCapture();
            }
        });

        resultsDlg.show();
    }

    private MicrResutls detectAndRecognizeMicr(RasterImage image, LeadRect searchingZoneValue) {
        OcrDocument ocrDocument = null;
        boolean micrCodeDetected = false;
        boolean showErrorMessage = !mStartLiveCapture;
        MicrResutls micrRes = new MicrResutls(null, null, searchingZoneValue, micrCodeDetected, showErrorMessage);

        try {
            LeadRect searchingZone;
            if (searchingZoneValue.isEmpty())
                searchingZone = new LeadRect(0, 0, image.getWidth(), image.getHeight());
            else
                searchingZone = searchingZoneValue;

            // Create LTOcrDocument and add the image into it.
            ocrDocument = mOcrEngine.getDocumentManager().createDocument();
            OcrPage ocrPage = ocrDocument.getPages().addPage(image, null);
            if(ocrPage == null)
                return micrRes;

            // Auto deskew the image before calling the MICR detection function
            ocrPage.autoPreprocess(OcrAutoPreprocessPageCommand.DESKEW, null);

            // Get the processed image from the engine
            RasterImage rasterImage = ocrPage.getRasterImage(OcrPageType.PROCESSING);

            double maxPageCoordinate = Math.max((double)ocrPage.getWidth(), (double)ocrPage.getHeight());
            double maxImageCoordinate = Math.max((double)rasterImage.getWidth(), (double)rasterImage.getHeight());
            double ratio = Math.min(maxPageCoordinate, maxImageCoordinate) / Math.max(maxPageCoordinate, maxImageCoordinate);
            if(ratio != 1.0) {
                searchingZone = new LeadRect((int)(searchingZone.getX() * ratio), (int)(searchingZone.getY() * ratio), (int)(searchingZone.getWidth() * ratio), (int)(searchingZone.getHeight() * ratio));
                ocrPage.setRasterImage(rasterImage);
                micrRes.setScale(ratio);
            }

            MICRCodeDetectionCommand micrCommand = new MICRCodeDetectionCommand();
            micrCommand.setSearchingZone(searchingZone);
            int ret = micrCommand.run(rasterImage);

            if(ret == L_ERROR.SUCCESS.getValue() && !micrCommand.getMICRZone().isEmpty()) {
                // We were able to determine the MICR area inside the image, so read the MICR code using the OCR engine

                // Add one zone to the OCR page with the same detected MICR zone rectangle
                OcrZone zone = new OcrZone();
                zone.setBounds(micrCommand.getMICRZone());
                zone.setFillMethod(OcrZoneFillMethod.MICR);
                ocrPage.getZones().add(zone);

                ocrPage.recognize(null);

                OcrPageCharacters pageCharacters = ocrPage.getRecognizedCharacters();
                if(pageCharacters == null || pageCharacters.size() <= 0)
                    return micrRes;

                OcrZoneCharacters characters = pageCharacters.get(0);
                if(isStrictMode() && characters.size() < 18)
                    return micrRes;

                String micrLineText = getMicrZoneText(characters);
                if(micrLineText != null && micrLineText.length() > 0) {
                    OcrMicrData micrData = characters.extractMicrData();
                    if (isStrictMode() && (micrData == null || micrData.getAccount().length() <= 0 || micrData.getRouting().length() <= 0))
                        return micrRes;

                    micrCodeDetected = true;

                    micrRes.setMicrData(micrData);
                    micrRes.setMicrText(micrLineText);
                    micrRes.setArea(searchingZone);
                    micrRes.setMicrCodeDetected(micrCodeDetected);
                    micrRes.setShowErrorMessage(showErrorMessage);
                    return micrRes;
                }
            }
        }
        finally {
            // Clear the OcrDocument pages
            if(ocrDocument != null)
                ocrDocument.getPages().clear();
        }

        return micrRes;
    }

    public void onRecognizeMicr(View v) {
        if(mImageViewer.getImage() == null) {
            // show error message and return
            Messager.showError(getActivity(), "No image loaded, please load image first.", null);
            return;
        }

        RasterImage rasterImage = mImageViewer.getImage().clone();
        LeadRect searchingZone = LeadRect.getEmpty();
        new RecognizeTextTask().execute(new RecognizeTextTaskParams(rasterImage, searchingZone));
    }

    public void onRecognizeArea(View v) {
        if(mIsWorking)
            return;

        RasterImage image = mImageViewer.getImage();
        if(image == null) {
            Messager.showError(getActivity(), "No image loaded", null);
            return;
        }

        // Use RuberbandInteractiveMode to select the area
        ImageViewerRubberBandListener listener = new ImageViewerRubberBandListener() {
            @Override
            public void onRubberBandWorking(ImageViewerRubberBandEvent event) {
            }

            @Override
            public void onRubberBandStarted(ImageViewerRubberBandEvent event) {
            }

            @Override
            public void onRubberBandCompleted(ImageViewerRubberBandEvent event) {
                // Get the selected points
                PointF pt1 = event.getPoint1();
                PointF pt2 = event.getPoint2();

                int left = (int) pt1.x;
                int top = (int) pt1.y;
                int right = (int) pt2.x;
                int bottom = (int) pt2.y;

                if (right < left) {
                    int temp = left;
                    left = right;
                    right = temp;
                }

                if (bottom < top) {
                    int temp = top;
                    top = bottom;
                    bottom = temp;
                }

                try {
                    RasterImage image = mImageViewer.getImage();
                    RectF resRect = new RectF(left, top, right, bottom);
                    // Convert the selected rectangle to image coordinates
                    resRect = mImageViewer.convertRect(CoordinateType.CONTROL, CoordinateType.IMAGE, resRect);
                    LeadRect rc = LeadRect.fromLTRB((int)resRect.left, (int)resRect.top, (int)resRect.right, (int)resRect.bottom);
                    rc.intersect(new LeadRect(0, 0, image.getWidth(), image.getHeight()));

                    // Recognize the selected area
                    new RecognizeTextTask().execute(new RecognizeTextTaskParams(image.clone(), rc));
                } catch(Exception ex) {
                    Messager.showError(getActivity(), ex.getMessage(), "Error");
                }
            }
        };

        ImageViewerRubberBandInteractiveMode rubberBandInteractiveMode = new ImageViewerRubberBandInteractiveMode();
        rubberBandInteractiveMode.addImageViewerRubberBandListener(listener);
        rubberBandInteractiveMode.setBorderColor(Color.RED);
        rubberBandInteractiveMode.setWorkOnImageRectangle(false);
        mImageViewer.setTouchInteractiveMode(rubberBandInteractiveMode);
    }

    public void displayResults(MicrResutls micrRes, RasterImage image, LeadRect rc) {
        boolean bImageCloned = false;
        RasterImage micrImage = image;
        if (!rc.isEmpty()) {
            double scale = micrRes.getScale() < 1.0 ? 1.0 : micrRes.getScale();
            micrImage = image.clone(new LeadRect((int)(rc.getLeft() / scale),
                    (int)(rc.getTop() / scale),
                    (int)(rc.getWidth() / scale),
                    (int)(rc.getHeight() / scale)));
            bImageCloned = true;
        }

        OcrMicrData micrData = micrRes.getMicrData();
        String micrText = micrRes.getMicrText();

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View resultsView = layoutInflater.inflate(R.layout.micr_results, null);

        final ImageView imageView = (ImageView) resultsView.findViewById(R.id.imageview_micr);
        int height = mImageViewer.getHeight() / 4;
        if(micrImage.getHeight() < height)
            height = micrImage.getHeight();
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height));
        imageView.setImageBitmap(RasterImageConverter.convertToBitmap(micrImage, ConvertToImageOptions.NONE.getValue()));
        if(bImageCloned)
            micrImage.dispose();

        {
            final TextView micrLineResultsTxtView = (TextView) resultsView.findViewById(R.id.txtview_micr_line_results);
            Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/micrenc.ttf");
            micrLineResultsTxtView.setTypeface(type);
            micrLineResultsTxtView.setText(getMicrString(micrText));
        }

        {
            String strAuxiliary = micrData.getAuxiliary();
            MICRResulttobeSent = strAuxiliary;
            ChequeNum = strAuxiliary;
            final TextView auxiliaryOnUsTxtView = (TextView) resultsView.findViewById(R.id.txtview_auxiliary_on_us);
            auxiliaryOnUsTxtView.setText(strAuxiliary.length() > 0 ? strAuxiliary : "N/A");
        }

        {
            char[] epc = new char[1];
            epc[0] = micrData.getEpc();
            final TextView epcTxtView = (TextView) resultsView.findViewById(R.id.txtview_epc);
            if (epc[0] != ' ')
                epcTxtView.setText(new String(epc));
            else
                epcTxtView.setText("N/A");
        }

        {
            String strRouting = micrData.getRouting();
            MICRResulttobeSent = MICRResulttobeSent + "." + strRouting;
            Log.d("MICRResulttobeSent",MICRResulttobeSent);
            final TextView routingTxtView = (TextView) resultsView.findViewById(R.id.txtview_routing);
            routingTxtView.setText(strRouting.length() > 0 ? strRouting : "N/A");
        }

        {
            String strAcount = micrData.getAccount();
            final TextView accountTxtView = (TextView) resultsView.findViewById(R.id.txtview_account);
            accountTxtView.setText(strAcount.length() > 0 ? strAcount: "N/A");
        }

        {
            String strCheckNumber = micrData.getCheckNumber();
            final TextView checkNumberTxtView = (TextView) resultsView.findViewById(R.id.txtview_check_number);
            checkNumberTxtView.setText(strCheckNumber.length() > 0 ? strCheckNumber : "N/A");
        }

        {
            String strAmount = micrData.getAmount();
            final TextView amountTxtView = (TextView) resultsView.findViewById(R.id.txtview_amount);
            amountTxtView.setText(strAmount.length() > 0 ? strAmount : "N/A");
        }

        AlertDialog.Builder resultsDlgBuilder = new AlertDialog.Builder(getActivity());
        resultsDlgBuilder.setView(resultsView);
        final AlertDialog resultsDlg = resultsDlgBuilder.create();
        resultsDlg.setTitle(R.string.micr_result);

        resultsDlg.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resultsDlg.dismiss();


                Bundle bundle = new Bundle();
                bundle.putString("micrOutput", MICRResulttobeSent);
                bundle.putString("ChequeNum", ChequeNum);

                if(flagCreateCheque.equals("CreateCheque"))
                {
                    chequePaymentAddress=true;
                    SetPaymentAddressFragment setPaymentAddressFragment = new SetPaymentAddressFragment();
                    setPaymentAddressFragment.setArguments(bundle);
                    fragmentManager = getFragmentManager();
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.frame_container,setPaymentAddressFragment);
                    ft.commit();
                }

                if(flagReceiveMoneyviaCheq.equals("ReceiveMoneyviaCheq"))
                {
                    ReceiveMoneyviaCheque receiveMoneyviaCheque = new ReceiveMoneyviaCheque();
                    receiveMoneyviaCheque.setArguments(bundle);
                    fragmentManager = getFragmentManager();
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.frame_container,receiveMoneyviaCheque);
                    ft.commit();
                }
            }
        });

        if (mStartLiveCapture) {
            resultsDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    startLiveCapture();
                }
            });
        }

        resultsDlg.show();

        if (mStartLiveCapture)
            stopLiveCapture(true);
    }

    public void onSettings(View v) {
        // Stop live capture
        displayMICRSettings(mStartLiveCapture);
        if(mStartLiveCapture) {
            stopLiveCapture(true);
        }
    }

    private class MicrResutls {
        private String micrText;
        private OcrMicrData micrData;
        private LeadRect mArea;
        private boolean mMicrCodeDetected;
        private boolean mShowErrorMessage;
        private double mScale;

        public MicrResutls(OcrMicrData data, String text, LeadRect area, boolean micrDetected, boolean showErrMsg) {
            micrText = text;
            micrData = data;
            mArea = area.clone();
            mMicrCodeDetected = micrDetected;
            mShowErrorMessage = showErrMsg;
            mScale = 1.0;
        }

        public OcrMicrData getMicrData() {
            return micrData;
        }

        public void setMicrData(OcrMicrData value) {
            micrData = value;
        }

        public String getMicrText() {
            return micrText;
        }

        public void setMicrText(String value) {
            micrText = value;
        }

        public LeadRect getArea() {
            return mArea;
        }

        public void setArea(LeadRect value) {
            mArea = value.clone();
        }

        public boolean isMicrCodeDetected() {
            return mMicrCodeDetected;
        }

        public void setMicrCodeDetected(boolean value) {
            mMicrCodeDetected = value;
        }

        public boolean canShowErrorMessage() {
            return mShowErrorMessage;
        }

        public void setShowErrorMessage(boolean value) {
            mShowErrorMessage = value;
        }

        public double getScale() {
            return mScale;
        }

        public void setScale(double value) {
            mScale = value;
        }
    }

    private class RecognizeTextTaskParams {
        private RasterImage mImage;
        private LeadRect mRect;

        public RecognizeTextTaskParams(RasterImage image, LeadRect rc) {
            mImage = image;
            mRect = rc;
        }

        public RasterImage getImage() {
            return mImage;
        }

        public LeadRect getRect() {
            return mRect;
        }
    }

    private class RecognizeTextTask extends AsyncTask<RecognizeTextTaskParams, Void, MicrResutls> {
        protected void onPreExecute () {
            mIsWorking = true;
            // Show progress
            mProgressDlg = Progress.show(getActivity(), "", "");
        }

        protected MicrResutls doInBackground(RecognizeTextTaskParams... paramArray) {
            RecognizeTextTaskParams params = paramArray[0];
            RasterImage image = null;
            try {
                image = params.getImage();
                LeadRect rc = params.getRect();
                if(rc == null)
                    return null;

                return detectAndRecognizeMicr(image, rc);

            } catch(Exception ex) {
                return null;
            }
            finally {
                if(image != null)
                    image.dispose();
            }
        }

        protected void onPostExecute(MicrResutls result) {
            Progress.close(mProgressDlg);
            mIsWorking = false;
            if (result == null)
                Messager.showError(getActivity(), "Error recognizing MICR zone", null);
            else {
                if(!result.isMicrCodeDetected() && result.canShowErrorMessage()) {
                    Messager.showError(getActivity(), "No MICR code detected, or cheque doesn't match the standards", null);
                }
                else
                    displayResults(result, mImageViewer.getImage(), result.getArea());
            }
        }
    }
}





