//package com.baselibrary.album;
//
//import android.app.Activity;
//
//import android.content.ClipData;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.PixelFormat;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.provider.MediaStore;
//import android.support.v4.content.FileProvider;
//
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.baselibrary.utils.Log;
//import com.baselibrary.utils.ToastUtils;
//import com.baselibrary.utils.utlis.AppUtils;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import me.leefeng.promptlibrary.PromptButton;
//import me.leefeng.promptlibrary.PromptButtonListener;
//import me.leefeng.promptlibrary.PromptDialog;
//
///**
// * android相册工具类
// */
//public class ImageUtils {
//    private Context mContext;    //全局Context变量
//    private Activity activity;  //见构造方法中介绍
//    private ImageView mImageView;   //见构造方法中介绍
//    private Bitmap mNewSavedPhotoBitmap;  //见构造方法中介绍
//    private Drawable mNewSetPhotoDrawable;  //见构造方法中介绍
//    private static final int REQUEST_COOD_TAKE_PHOTO=1001; //在onActivityResult中返回码中代表中“拍照"
//    private static final int REQUEST_COOD_CHOOSE_PHOTO=1002;//在onActivityResult中返回码中代表中“从图库选择"
//    private static final int REQUEST_COOD_CROP_PHOTO=1003; //在onActivityResult中返回码中代表中“裁剪"
//    private final Uri mCropImageUri; //剪裁后保存图片的路径
//    private final Uri mTakePhotoUri;  //拍照后保存图片的路径
//    private static final String CROP_IMAGE_NAME = "CropPhoto.jpg";
//    private static final String TAKE_IMAGE_NAME = "TakePhoto.jpg";
//    private static final String NEW_USER_PHOTO_FILE_NAME = "NewUserPhoto.png";//保存下来的临时文件，用于横竖屏切换时Savephoto的选项
//    private static final String FILE_PROVIDER_AUTHERITY = AppUtils.getAppPackageName() + ".fileprovider";
//    private Uri myUri;//保存图片时需要用到的变量
//    private final int mPhotoSize;//保存图片的大小
//    private PromptDialog promptDialog;
//
//    /**
//     *
//     * @param imageView 为要设置的是哪个图片
//     * @param savePhoto 为剪裁后保存下来的图片
//     * @param drawable  为临时变量，代表着返还失败设置原来图片，还是通过剪裁成功后需要设置的图片
//     */
//    public ImageUtils(ImageView imageView, Bitmap savePhoto, Drawable drawable) {
//        mContext = imageView.getContext();
//        activity = (Activity) mContext;
//        mImageView=imageView;
//        mNewSavedPhotoBitmap = savePhoto;
//        mNewSetPhotoDrawable = drawable;
//        mCropImageUri = createTempImageUri(mContext, CROP_IMAGE_NAME);
//        mTakePhotoUri = createTempImageUri(mContext, TAKE_IMAGE_NAME);
//        mPhotoSize= getPhotoSize(mContext);
//        //创建对象
//        promptDialog = new PromptDialog(activity);
//        //设置自定义属性
//        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
//        //设置传进来的ImageView的点击事件：
//        imageView.setOnClickListener(v -> chooseType());
//    }
//
//    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
//        //图片Uri
//        final Uri pictureUri = data != null && data.getData() != null
//                ? data.getData() : mTakePhotoUri;
//        if (resultCode != Activity.RESULT_OK) {
//            //代表着拍照取消或失败了。这里用的返回结果的resultCode
//            return false;
//        }
//        switch (requestCode) {
//            //剪裁
//            case REQUEST_COOD_CROP_PHOTO:
//                //剪裁方法，跳转到系统图库剪裁
//                onPhotoCropped(pictureUri,true);
//                return true;
//            case REQUEST_COOD_TAKE_PHOTO:
//            case REQUEST_COOD_CHOOSE_PHOTO:
//                //跳转到剪裁
//                cropPhoto(pictureUri);
//                return true;
//        }
//        return false;
//    }
//
//    /**
//     * 此为创建ImageUri到FileProvider的方法
//     * @param context
//     * @param fileName
//     * @return Uri的地址。
//     */
//    private Uri createTempImageUri(Context context, String fileName) {
//        final File folder = context.getExternalFilesDir("image/*");//这里注意，有的机器是没有SD卡的，故需要使用ExternalFilesDir,保存在内置储存卡中。
//        Log.e("文件夹为=" + folder);
//        //如果文件夹不存在则创建。这里不要怕空指针，一般是不会有问题的。
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        final File fullPath = new File(folder, fileName);//获取图片
//        return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHERITY, fullPath);
//    }
//
//    private void chooseType() {
//        PromptButton cancle = new PromptButton("取消", null);
//        cancle.setTextColor(Color.parseColor("#0076ff"));
//        promptDialog.showAlertSheet("", true, cancle,
//                new PromptButton("相机", promptButton -> {
//                    promptDialog.dismiss();
//                    takePhoto();
//                }), new PromptButton("相册", promptButton -> {
//                    promptDialog.dismiss();
//                    choosePhoto();
//                }));
//    }
//
//    /**
//     * 拍照
//     */
//    private void takePhoto() {
//        //启动系统相机
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        appendOutputExtra(intent, mTakePhotoUri);
//        try {
//            //拍照完成后走onActivityResult方法，再进行处理照片
//            activity.startActivityForResult(intent, REQUEST_COOD_TAKE_PHOTO);
//        } catch (Exception e) {
//            e.printStackTrace();
//            ToastUtils.showShort("没有相机");
//        }
//    }
//
//    /**
//     * 从图库选择
//     */
//    private void choosePhoto() {
//        //启动系统相册
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setType("image/*");
//        appendOutputExtra(intent, mTakePhotoUri);
//        try {
//            //选择完成后走onActivityResult方法，再进行处理照片
//            activity.startActivityForResult(intent, REQUEST_COOD_CHOOSE_PHOTO);
//        } catch (Exception e) {
//            Log.d(e.toString());
//        }
//    }
//
//    private void appendOutputExtra(Intent intent, Uri uri) {
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        //这里需要打开Provider的读写权限，否则会报 open content provider failed错误
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//                | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, uri));
//    }
//    private void cropPhoto(Uri pictureUri) {
//        myUri=pictureUri;
//        // TODO: Use a public intent, when there is one.
//        //这里是先调用了保存图片的方法，因为有些时候调用系统图库的图片会产生保存失败的情况，一些机器是不支持Bmp和GIF保存的
//        //在调用系统图库进行剪裁前，先保存下来我们拍的照片或从图库的照片到我们的Uri中。
//        File file = saveMyPhotoBitmap();
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        if (file != null) {
//            intent.setDataAndType(mCropImageUri, "image/*");
//        } else {
//            intent.setDataAndType(pictureUri, "image/*");
//        }
//        appendOutputExtra(intent, mCropImageUri);
//
//        //传递到剪裁页面的图片，为了方便我就不封装到方法中了
//        intent.putExtra("crop", "true");
//        intent.putExtra("scale", true);
//        intent.putExtra("scaleUpIfNeeded", true);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", mPhotoSize);
//        intent.putExtra("outputY", mPhotoSize);
//
//        //这里的逻辑，走的方法可以查看LOG中的打印
//        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
//            Log.d("bitmap=========1");
//            try {
//                Log.e("bitmap=========5");
//                activity.startActivityForResult(intent, REQUEST_COOD_CROP_PHOTO);
//                Log.e("bitmap=========6");
//            } catch (Exception e) {
//                ToastUtils.showShort("没有发现可提供剪裁的图库");
//                Log.e( "bitmap========7");
//            }
//        } else {
//            Log.e( "bitmap=========2");
//            onPhotoCropped(pictureUri, false);
//        }
//    }
//
//    /**
//     * 在跳转到剪裁前，保存拍照或者从图库中选择的图片到本地的FileProvider中，防止系统图库中的图片不能进行修改的问题
//     * @return
//     */
//    File saveMyPhotoBitmap() {
//
//        Bitmap bitmap = mySaveBitmap();
//        if (bitmap == null) {
//            return null;
//        }
//        try {
//            File file = new File(mContext.getExternalFilesDir("image/*"), CROP_IMAGE_NAME);
//            OutputStream os = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//            os.flush();
//            os.close();
//            return file;
//        } catch (IOException e) {
//            Log.e("Cannot create temp file:"+e);
//        }
//        return null;
//    }
//    private Bitmap mySaveBitmap() {
//        Bitmap croppedImage = Bitmap.createBitmap(mPhotoSize, mPhotoSize,
//                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(croppedImage);
//        Bitmap fullImage = null;
//        try {
//            InputStream imageStream = mContext.getContentResolver()
//                    .openInputStream(myUri);
//            fullImage = BitmapFactory.decodeStream(imageStream);
//        } catch (FileNotFoundException fe) {
//            Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
//        }
//        if (fullImage != null) {
//            final int squareSize = Math.min(fullImage.getWidth(),
//                    fullImage.getHeight());
//            final int left = (fullImage.getWidth() - squareSize) / 2;
//            final int top = (fullImage.getHeight() - squareSize) / 2;
//            Rect rectSource = new Rect(left, top,
//                    left + squareSize, top + squareSize);
//            Rect rectDest = new Rect(0, 0, mPhotoSize, mPhotoSize);
//            Paint paint = new Paint();
//            canvas.drawBitmap(fullImage, rectSource, rectDest, paint);
//            return croppedImage;
//        } else {
//            // Bah! Got nothin.
//            Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//    }
//
//    /**
//     * 得到图片可使用的最大尺寸
//     * @param context
//     * @return
//     */
//    private static int getPhotoSize(Context context) {
////        Cursor cursor = context.getContentResolver().query(
////                ContactsContract.DisplayPhoto.CONTENT_MAX_DIMENSIONS_URI,
////                new String[]{ContactsContract.DisplayPhoto.DISPLAY_MAX_DIM}, null, null, null);
////        try {
////            cursor.moveToFirst();
////            return cursor.getInt(0);
////        } finally {
////            cursor.close();
////        }
//        return 720;
//    }
//
//
//    /**
//     * 对图片进行剪裁处理，其中使用到的CirCleFramedDrawable为处理为圆形图案的工具类
//     * 使用AsyncTask异步处理，是为了防止ANR的出现
//     * 异步处理的流程是先走doInBackGround方法，处理耗时操作，然后再onPostExecute方法中处理RI操作。
//     * @param data
//     * @param cropped
//     */
//    private void onPhotoCropped(final Uri data, final boolean cropped) {
//        new AsyncTask<Void, Void, Bitmap>() {
//            @Override
//            protected Bitmap doInBackground(Void... params) {
//                Log.e( "bitmap=========4");
//                Log.e("ImageUri" + data);
//                if (cropped) {
//                    InputStream imageStream = null;
//                    try {
//                        imageStream = mContext.getContentResolver()
//                                .openInputStream(data);
//                        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
//                        if (bitmap != null) {
//                            Drawable drawble = CircleFramedDrawable.getInstance(mContext, bitmap);
//                            bitmap = drawableToBitamp(drawble);
//                        }
//                        return bitmap;
//                    } catch (FileNotFoundException fe) {
//                        Log.w( "Cannot find image file"+ fe);
//                        return null;
//                    } finally {
//                        if (imageStream != null) {
//                            try {
//                                imageStream.close();
//                            } catch (IOException ioe) {
//                                Log.w( "Cannot close image stream"+ioe);
//                            }
//                        }
//                    }
//                } else {
//                    // Scale and crop to a square aspect ratio
//                    Bitmap croppedImage = Bitmap.createBitmap(mPhotoSize, mPhotoSize,
//                            Bitmap.Config.ARGB_8888);
//                    Canvas canvas = new Canvas(croppedImage);
//                    Bitmap fullImage = null;
//                    try {
//                        InputStream imageStream = mContext.getContentResolver()
//                                .openInputStream(data);
//                        fullImage = BitmapFactory.decodeStream(imageStream);
//                    } catch (FileNotFoundException fe) {
//                        return null;
//                    }
//                    if (fullImage != null) {
//                        final int squareSize = Math.min(fullImage.getWidth(),
//                                fullImage.getHeight());
//                        final int left = (fullImage.getWidth() - squareSize) / 2;
//                        final int top = (fullImage.getHeight() - squareSize) / 2;
//                        Rect rectSource = new Rect(left, top,
//                                left + squareSize, top + squareSize);
//                        Rect rectDest = new Rect(0, 0, mPhotoSize, mPhotoSize);
//                        Paint paint = new Paint();
//                        canvas.drawBitmap(fullImage, rectSource, rectDest, paint);
//                        return croppedImage;
//                    } else {
//                        // Bah! Got nothin.
//                        return null;
//                    }
//                }
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap bitmap) {
//                Log.e( "bitmap=" + bitmap);
//                if (bitmap != null) {
//                    mNewSavedPhotoBitmap = bitmap;
//                    mNewSetPhotoDrawable = CircleFramedDrawable
//                            .getInstance(mImageView.getContext(), mNewSavedPhotoBitmap);
//                    mImageView.setImageDrawable(mNewSetPhotoDrawable);
//                }
//                //使用完后删除文件，防止文件过多占用系统内存
//                new File(mContext.getExternalFilesDir("image/*"), TAKE_IMAGE_NAME).delete();
//                new File(mContext.getExternalFilesDir("image/*"), CROP_IMAGE_NAME).delete();
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
//    }
//
//    /**
//     * 将drawable转化为bitmap 用户生成圆形图片
//     * @param drawable
//     * @return
//     */
//    public static Bitmap drawableToBitamp(Drawable drawable) {
//        int w = drawable.getIntrinsicWidth();
//        int h = drawable.getIntrinsicHeight();
//        Bitmap.Config config =
//                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                        : Bitmap.Config.RGB_565;
//        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
//        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, w, h);
//        drawable.draw(canvas);
//        return bitmap;
//    }
//
//
//    /**
//     * 保存回调的msavePhoto
//     * @return
//     */
//
//    public File saveNewUserPhotoBitmap() {
//        if (mNewSavedPhotoBitmap == null) {
//            return null;
//        }
//        try {
//            File file = new File(mContext.getExternalFilesDir("image/*"), NEW_USER_PHOTO_FILE_NAME);
//            OutputStream os = new FileOutputStream(file);
//            mNewSavedPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//            os.flush();
//            os.close();
//            return file;
//        } catch (IOException e) {
//            Log.e("Cannot create temp file"+ e);
//        }
//        return null;
//    }
//
//    /**
//     * 在横竖屏切换时，去读取设置过的图片
//     * @param file
//     * @return
//     */
//    public static Bitmap loadNewUserPhotoBitmap(File file) {
//        return BitmapFactory.decodeFile(file.getAbsolutePath());
//    }
//
//    public Bitmap getNewUserPhotoBitmap() {
//        return mNewSavedPhotoBitmap;
//    }
//
//    public Drawable getNewUserPhotoDrawable() {
//        return mNewSetPhotoDrawable;
//    }
//}
