package com.example.daily_function;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class BoardInsertActivity extends AppCompatActivity {

    private static final String TAG = "Daily";

    Button register,upbtn;
    EditText title;
    EditText content;


    //define firebase object
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    private FirebaseDatabase bDatabase; //데이터베이스
    private DatabaseReference bReference;
    private ChildEventListener bChild;

    FirebaseStorage storage;
    StorageReference storageReference;

    private ImageView imageView;
    private String cacheFilePath = null;

    private Boolean isPermission = true;

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_ALBUM = 101;

    private Uri albumUri;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_insert);

        title=(EditText)findViewById(R.id.title_et);
        content=(EditText)findViewById(R.id.content_et);
        imageView=(ImageView)findViewById(R.id.imageView);

        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        initDatabase();

        upbtn=(Button)findViewById(R.id.up_button);
        upbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                makeDialog();
            }
        });



        register= (Button)findViewById(R.id.reg_button);
        register.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alt_bld = new AlertDialog.Builder(BoardInsertActivity.this, R.style.MyAlertDialogStyle);
                alt_bld.setTitle("작성 완료").setIcon(R.drawable.ic_image).setMessage("글을 게시하시겠습니까?").setCancelable(
                        false).setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                
                                
                                //등록 시간 가져오기
                                Calendar cal = Calendar.getInstance();
                                Date nowDate = cal.getTime();

                                SimpleDateFormat dataformat = new SimpleDateFormat("yyyy.MM.dd a HH:mm:ss");

                                //User 이메일에서 유저아이디만 받아오기
                                user = firebaseAuth.getCurrentUser();

                                String r_email = user.getEmail();
                                int s= r_email.indexOf("@");
                                String r_id=r_email.substring(0,s);

                                String r_title =title.getText().toString().trim();
                                String r_content=content.getText().toString().trim();
                                String r_boardnum=bReference.child("Board").child("BoardData").push().getKey();

                                String r_date = dataformat.format(nowDate);

                                //좋아요 , 조회수 초기값 설정
                                int r_hit =0;
                                int r_get=0;


                                if(TextUtils.isEmpty(r_title)){
                                    return;
                                }
                                if(TextUtils.isEmpty(r_content)){
                                    return;
                                }


                                if(photoUri==null&&albumUri==null){ //사진을 등록 안할 경우

                                    BoardData bd = new BoardData(r_boardnum,r_title,r_content,r_date,r_hit,r_get,r_id,"0");
                                    bReference.child("Board").child("BoardData").child(r_boardnum).setValue(bd);
                                    //bReference.push().setValue(bd);

                                    startActivity(new Intent(getApplicationContext(), Board_totalActivity.class));
                                    finish();

                                }else if(albumUri!=null){ //앨범을 등록할경우
                                    final String randomKey=UUID.randomUUID().toString();

                                    final ProgressDialog progressDialog=new ProgressDialog(BoardInsertActivity.this);
                                    progressDialog.setTitle("Please Wait...");
                                    progressDialog.show();


                                    try {
                                        // Firebase Storeage관리 객체 얻어오기


                                        StorageReference reference = storageReference.child("BoardImage/" + randomKey);
                                        reference.putFile(albumUri)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                progressDialog.dismiss();

                                                Toast.makeText(BoardInsertActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                                                BoardData bd = new BoardData(r_boardnum,r_title,r_content,r_date,r_hit,r_get,r_id,randomKey);
                                                bReference.child("Board").child("BoardData").child(r_boardnum).setValue(bd);

                                                startActivity(new Intent(getApplicationContext(), Board_totalActivity.class));
                                                finish();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(BoardInsertActivity.this, "업로드 실패" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                                progressDialog.setMessage("Saved" + (int) progress + "%");
                                            }
                                        });
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    //bReference.push().setValue(bd);


                                }else if(photoUri!=null){//사진촬영등록일경우
                                    final String randomKey=UUID.randomUUID().toString();

                                    final ProgressDialog progressDialog=new ProgressDialog(BoardInsertActivity.this);
                                    progressDialog.setTitle("Please Wait...");
                                    progressDialog.show();

                                    try {
                                        // Firebase Storeage관리 객체 얻어오기


                                        StorageReference reference = storageReference.child("BoardImage/" + randomKey);
                                        reference.putFile(photoUri)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(BoardInsertActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();

                                                        BoardData bd = new BoardData(r_boardnum,r_title,r_content,r_date,r_hit,r_get,r_id,randomKey);
                                                        bReference.child("Board").child("BoardData").child(r_boardnum).setValue(bd);

                                                        startActivity(new Intent(getApplicationContext(), Board_totalActivity.class));
                                                        finish();


                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(BoardInsertActivity.this, "업로드 실패" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                                progressDialog.setMessage("Saved" + (int) progress + "%");
                                            }
                                        });
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    //bReference.push().setValue(bd);

                                }

                                
                            }
                        }).setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 아니오 클릭. dialog 닫기.
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alt_bld.create();
                alert.show();

            }
        });

        

    }


    private void makeDialog(){

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(BoardInsertActivity.this, R.style.MyAlertDialogStyle);
        alt_bld.setTitle("사진 업로드").setIcon(R.drawable.ic_image).setCancelable(
                false).setPositiveButton("사진촬영",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {
                        // 사진 촬영 클릭
                        Log.v("알림", "다이얼로그 > 사진촬영 선택");
                        //권한요청
                        requestPermissions( new String[]{ Manifest.permission.CAMERA }, REQUEST_CAMERA );
                    }
                }).setNeutralButton("앨범선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
                        //앨범에서 선택
                        onAlbum( REQUEST_ALBUM );
                    }
                }).setNegativeButton("취소   ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("알림", "다이얼로그 > 취소 선택");
                        // 취소 클릭. dialog 닫기.
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();

    }




    private void initDatabase() { //데이터베이스 초기화
        bDatabase = FirebaseDatabase.getInstance();

        bReference = bDatabase.getReference();

        bChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        bReference.addChildEventListener(bChild);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        bReference.removeEventListener(bChild);
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if ( requestCode == REQUEST_CAMERA ) {
            for ( int g : grantResults ) {
                if ( g == PackageManager.PERMISSION_DENIED ) {
                    //권한거부
                    return;
                }
            }
            //임시파일 생성
            File file = createImgCacheFile( );
            cacheFilePath = file.getAbsolutePath( );
            //카메라 호출
            onCamera( REQUEST_CAMERA, file );
        }
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ) {

            AlbumAdd( cacheFilePath );
            imageView.setImageBitmap( getBitmapCamera( imageView, cacheFilePath ) );

        } else if ( requestCode == REQUEST_ALBUM && resultCode == RESULT_OK ) {

            albumUri = data.getData( );
            String fileName = getFileName( albumUri );
            try {

                ParcelFileDescriptor parcelFileDescriptor = getContentResolver( ).openFileDescriptor( albumUri, "r" );
                if ( parcelFileDescriptor == null ) return;
                FileInputStream inputStream = new FileInputStream( parcelFileDescriptor.getFileDescriptor( ) );
                File cacheFile = new File( this.getCacheDir( ), fileName );
                FileOutputStream outputStream = new FileOutputStream( cacheFile );
                IOUtils.copy( inputStream, outputStream );

                cacheFilePath = cacheFile.getAbsolutePath( );

                imageView.setImageBitmap( getBitmapAlbum( imageView, albumUri ) );

            } catch ( Exception e ) {
                e.printStackTrace( );
            }

        } else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_CANCELED ) {
            fileDelete( cacheFilePath );
            cacheFilePath = null;
        }
    }

    /**
     * 카메라 및 앨범관련 작업함수
     */
    //캐시파일 생성
    public File createImgCacheFile( ) {
        File cacheFile = new File( getCacheDir( ), new SimpleDateFormat( "yyyyMMdd_HHmmss", Locale.US ).format( new Date( ) ) + ".jpg" );
        return cacheFile;
    }

    //카메라 호출
    public void onCamera( int requestCode, File createTempFile ) {
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        if ( intent.resolveActivity( getPackageManager( ) ) != null ) {
            if ( createTempFile != null ) {
                Uri photoURI = FileProvider.getUriForFile( this, BuildConfig.APPLICATION_ID, createTempFile );
                intent.putExtra( MediaStore.EXTRA_OUTPUT, photoURI );
                startActivityForResult( intent, requestCode );
            }
        }
    }

    //앨범 호출
    public void onAlbum( int requestCode ) {
        Intent intent = new Intent( Intent.ACTION_PICK );
        intent.setType( MediaStore.Images.Media.CONTENT_TYPE );
        startActivityForResult( intent, requestCode );
    }

    //앨범 저장
    public void AlbumAdd( String cacheFilePath ) {
        if ( cacheFilePath == null ) return;
        BitmapFactory.Options options = new BitmapFactory.Options( );
        ExifInterface exifInterface = null;

        try {
            exifInterface = new ExifInterface( cacheFilePath );
        } catch ( Exception e ) {
            e.printStackTrace( );
        }

        int exifOrientation;
        int exifDegree = 0;

        //사진 회전값 구하기
        if ( exifInterface != null ) {
            exifOrientation = exifInterface.getAttributeInt( ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL );

            if ( exifOrientation == ExifInterface.ORIENTATION_ROTATE_90 ) {
                exifDegree = 90;
            } else if ( exifOrientation == ExifInterface.ORIENTATION_ROTATE_180 ) {
                exifDegree = 180;
            } else if ( exifOrientation == ExifInterface.ORIENTATION_ROTATE_270 ) {
                exifDegree = 270;
            }
        }

        Bitmap bitmap = BitmapFactory.decodeFile( cacheFilePath, options );
        Matrix matrix = new Matrix( );
        matrix.postRotate( exifDegree );

        Bitmap exifBit = Bitmap.createBitmap( bitmap, 0, 0, bitmap.getWidth( ), bitmap.getHeight( ), matrix, true );

        ContentValues values = new ContentValues( );
        //실제 앨범에 저장될 이미지이름
        values.put( MediaStore.Images.Media.DISPLAY_NAME, new SimpleDateFormat( "yyyyMMdd_HHmmss", Locale.US ).format( new Date( ) ) + ".jpg" );
        values.put( MediaStore.Images.Media.MIME_TYPE, "image/*" );
        //저장될 경로
        values.put( MediaStore.Images.Media.RELATIVE_PATH, "DCIM/AndroidQ" );
        values.put( MediaStore.Images.Media.ORIENTATION, exifDegree );
        values.put( MediaStore.Images.Media.IS_PENDING, 1 );

        Uri u = MediaStore.Images.Media.getContentUri( MediaStore.VOLUME_EXTERNAL );
        Uri uri = getContentResolver( ).insert( u, values );

        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver( ).openFileDescriptor( uri, "w", null );
            if ( parcelFileDescriptor == null ) return;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream( );
            exifBit.compress( Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream );
            byte[] b = byteArrayOutputStream.toByteArray( );
            InputStream inputStream = new ByteArrayInputStream( b );

            ByteArrayOutputStream buffer = new ByteArrayOutputStream( );
            int bufferSize = 1024;
            byte[] buffers = new byte[ bufferSize ];

            int len = 0;
            while ( ( len = inputStream.read( buffers ) ) != -1 ) {
                buffer.write( buffers, 0, len );
            }

            byte[] bs = buffer.toByteArray( );
            FileOutputStream fileOutputStream = new FileOutputStream( parcelFileDescriptor.getFileDescriptor( ) );
            fileOutputStream.write( bs );
            fileOutputStream.close( );
            inputStream.close( );
            parcelFileDescriptor.close( );

            getContentResolver( ).update( uri, values, null, null );

        } catch ( Exception e ) {
            e.printStackTrace( );
        }

        values.clear( );
        values.put( MediaStore.Images.Media.IS_PENDING, 0 );
        getContentResolver( ).update( uri, values, null, null );
    }

    //이미지뷰에 뿌려질 앨범 비트맵 반환
    public Bitmap getBitmapAlbum( View targetView, Uri uri ) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver( ).openFileDescriptor( uri, "r" );
            if ( parcelFileDescriptor == null ) return null;
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor( );
            if ( fileDescriptor == null ) return null;

            int targetW = targetView.getWidth( );
            int targetH = targetView.getHeight( );

            BitmapFactory.Options options = new BitmapFactory.Options( );
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFileDescriptor( fileDescriptor, null, options );

            int photoW = options.outWidth;
            int photoH = options.outHeight;

            int scaleFactor = Math.min( photoW / targetW, photoH / targetH );
            if ( scaleFactor >= 8 ) {
                options.inSampleSize = 8;
            } else if ( scaleFactor >= 4 ) {
                options.inSampleSize = 4;
            } else {
                options.inSampleSize = 2;
            }
            options.inJustDecodeBounds = false;

            Bitmap reSizeBit = BitmapFactory.decodeFileDescriptor( fileDescriptor, null, options );

            ExifInterface exifInterface = null;
            try {
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) {
                    exifInterface = new ExifInterface( fileDescriptor );
                }
            } catch ( IOException e ) {
                e.printStackTrace( );
            }

            int exifOrientation;
            int exifDegree = 0;

            //사진 회전값 구하기
            if ( exifInterface != null ) {
                exifOrientation = exifInterface.getAttributeInt( ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL );

                if ( exifOrientation == ExifInterface.ORIENTATION_ROTATE_90 ) {
                    exifDegree = 90;
                } else if ( exifOrientation == ExifInterface.ORIENTATION_ROTATE_180 ) {
                    exifDegree = 180;
                } else if ( exifOrientation == ExifInterface.ORIENTATION_ROTATE_270 ) {
                    exifDegree = 270;
                }
            }

            parcelFileDescriptor.close( );
            Matrix matrix = new Matrix( );
            matrix.postRotate( exifDegree );

            Bitmap reSizeExifBitmap = Bitmap.createBitmap( reSizeBit, 0, 0, reSizeBit.getWidth( ), reSizeBit.getHeight( ), matrix, true );
            return reSizeExifBitmap;

        } catch ( Exception e ) {
            e.printStackTrace( );
            return null;
        }
    }

    //이미지뷰에 뿌려질 카메라 비트맵 반환
    public Bitmap getBitmapCamera( View targetView, String filePath ) {
        int targetW = targetView.getWidth( );
        int targetH = targetView.getHeight( );

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options( );
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile( filePath, bmOptions );

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        double scaleFactor = Math.min( photoW / targetW, photoH / targetH );
        if ( scaleFactor >= 8 ) {
            bmOptions.inSampleSize = 8;
        } else if ( scaleFactor >= 4 ) {
            bmOptions.inSampleSize = 4;
        } else {
            bmOptions.inSampleSize = 2;
        }


        bmOptions.inJustDecodeBounds = false;

        Bitmap originalBitmap = BitmapFactory.decodeFile( filePath, bmOptions );

        ExifInterface exifInterface = null;
        try {
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) {
                exifInterface = new ExifInterface( filePath );
            }
        } catch ( IOException e ) {
            e.printStackTrace( );
        }

        int exifOrientation;
        int exifDegree = 0;

        //사진 회전값 구하기
        if ( exifInterface != null ) {
            exifOrientation = exifInterface.getAttributeInt( ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL );

            if ( exifOrientation == ExifInterface.ORIENTATION_ROTATE_90 ) {
                exifDegree = 90;
            } else if ( exifOrientation == ExifInterface.ORIENTATION_ROTATE_180 ) {
                exifDegree = 180;
            } else if ( exifOrientation == ExifInterface.ORIENTATION_ROTATE_270 ) {
                exifDegree = 270;
            }
        }

        Matrix matrix = new Matrix( );
        matrix.postRotate( exifDegree );

        Bitmap reSizeExifBitmap = Bitmap.createBitmap( originalBitmap, 0, 0, originalBitmap.getWidth( ), originalBitmap.getHeight( ), matrix, true );
        return reSizeExifBitmap;

    }

    //앨범에서 선택한 사진이름 가져오기
    public String getFileName( Uri uri ) {
        Cursor cursor = getContentResolver( ).query( uri, null, null, null, null );
        try {
            if ( cursor == null ) return null;
            cursor.moveToFirst( );
            String fileName = cursor.getString( cursor.getColumnIndex( OpenableColumns.DISPLAY_NAME ) );
            cursor.close( );
            return fileName;

        } catch ( Exception e ) {
            e.printStackTrace( );
            cursor.close( );
            return null;
        }
    }

    //파일삭제
    public void fileDelete( String filePath ) {
        if ( filePath == null ) return;
        try {
            File f = new File( filePath );
            if ( f.exists( ) ) {
                f.delete( );
            }
        } catch ( Exception e ) {
            e.printStackTrace( );
        }
    }

    //실제 앨범경로가 아닌 앱 내에 캐시디렉토리에 존재하는 이미지 캐시파일삭제
    //확장자 .jpg 필터링해서 제거
    public void cacheDirFileClear( ) {
        File cacheDir = new File( getCacheDir( ).getAbsolutePath( ) );
        File[] cacheFiles = cacheDir.listFiles( new FileFilter( ) {
            @Override
            public boolean accept( File pathname ) {
                return pathname.getName( ).endsWith( "jpg" );
            }
        } );
        if ( cacheFiles == null ) return;
        for ( File c : cacheFiles ) {
            fileDelete( c.getAbsolutePath( ) );
        }
    }




}
