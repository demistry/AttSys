package com.uniben.attsys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.uniben.attsys.adapters.ViewPagerAdapter;
import com.uniben.attsys.api.AttSysApi;
import com.uniben.attsys.api.ServiceGenerator;
import com.uniben.attsys.dialogs.LoadingDialog;
import com.uniben.attsys.fragments.AttendanceFragment;
import com.uniben.attsys.models.Attendance;
import com.uniben.attsys.models.FaceRecognitionResponse;
import com.uniben.attsys.models.Student;
import com.uniben.attsys.models.User;
import com.uniben.attsys.utils.Constants;
import com.uniben.attsys.utils.NotificationUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CoursesActivity extends AppCompatActivity implements AttendanceFragment.OnTakeAttendanceListener{
    private static final String TAG = "CoursesActivity";
    private LoadingDialog loadingDialog;
    ViewPager viewPager;
    TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private User user;
    private boolean doubleBackToExitPressedOnce;
    private Student student;
    private Attendance mAttendance;
    private String currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.course_activity_tab_title_attendance));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.course_activity_tab_title_course_list));

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }
                }
        );

        loadingDialog = new LoadingDialog(this, "Getting Data...");
        loadingDialog.show();

        user = getIntent().getParcelableExtra(Constants.USER_KEY);

        getData();
    }

    private void getData() {
        AttSysApi attSysApi = ServiceGenerator.createService(AttSysApi.class);
        attSysApi.getStudent("Token " + user.getToken().getToken()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubsciber());
    }

    @Override
    public void onBackPressed() {
        navigateBack();
    }

    private void navigateBack() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }
        this.doubleBackToExitPressedOnce = true;
        NotificationUtils.notifyUser(viewPager, getString(R.string.exit_app_message));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, Constants.EXIT_APP_DURATION);
    }

    private Observer<Student> getSubsciber() {
        return new DisposableObserver<Student>() {
            @Override
            public void onNext(Student studentUser) {
                loadingDialog.dismissWithAnimation();
                student = studentUser;
                if (student != null) {
                    //Log.i(TAG, "onNext: " + student.getAttendanceList().size());
                    adapter.setAttendaceList(student.getAttendanceList());
                    adapter.setCourseList(student.getCourseList());
                }else{
                    loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    loadingDialog.setContentText("An error occurred");
                    loadingDialog.setConfirmText("Retry");
                    loadingDialog.setConfirmClickListener(sweetAlertDialog -> {
                        getData();
                    });
                    NotificationUtils.notifyUser(viewPager, "An error occurred!");
                }
            }

            @Override
            public void onError(Throwable e) {
                loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                loadingDialog.setContentText("An error occurred");
                loadingDialog.setConfirmText("Retry");
                loadingDialog.setConfirmClickListener(sweetAlertDialog -> {
                    getData();
                });
                e.printStackTrace();
                NotificationUtils.notifyUser(viewPager, "An error occurred!");
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public void onTakeAttendace(Attendance attendance) {
        mAttendance = attendance;
        // NotificationUtils.notifyUser(viewPager, "Attendance section is in development");

//        Intent intent = new Intent(this, AttendanceActivity.class);
//        intent.putExtra(Constants.ATTENDANCE_KEY, attendance);
//        startActivity(intent);
        callCamera();
    }

    public Observer<FaceRecognitionResponse> getFacialResults() {
        return new DisposableObserver<FaceRecognitionResponse>() {
            @Override
            public void onNext(FaceRecognitionResponse faceRecognitionResponse) {
                Log.v("TAG", "Message is " + faceRecognitionResponse.getMessage());
                Log.v("TAG", "Similarity is " + faceRecognitionResponse.getSimilarity());
                NotificationUtils.notifyUser(viewPager, "Uploading successfully returned");

            }

            @Override
            public void onError(Throwable e) {
                NotificationUtils.notifyUser(viewPager, "Error occurred");

                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 120 && resultCode == RESULT_OK){

            File file = new File(currentImagePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            AttSysApi attSysApi = ServiceGenerator.createService(AttSysApi.class);
            attSysApi.verifyPicture("Token "+ user.getToken().getToken(), body,mAttendance.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getFacialResults());
        }

    }

    private void callCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.uniben.attsys.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 120);
            }
        }
    }
        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            currentImagePath = image.getPath();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeFile(image.getPath ());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(image));
                Log.v("TAG", "File Compressed");
            }
            catch (Throwable t) {
                Log.v("TAG", "Error compressing file." + t.toString ());
                t.printStackTrace ();
            }
            return image;
        }

}
