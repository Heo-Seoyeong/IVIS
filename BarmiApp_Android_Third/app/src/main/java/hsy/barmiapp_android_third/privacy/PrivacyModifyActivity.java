package hsy.barmiapp_android_third.privacy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import hsy.barmiapp_android_third.R;
import hsy.barmiapp_android_third.RoundedAvatarDrawable;


public class PrivacyModifyActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextMan;
    private EditText editTextBirth;
    private EditText editTextAge;
    private EditText editTextWeight;
    private EditText editTextHeight;
    private EditText editTextBlood;

    private Uri uri;
    private Uri mImageCaptureUri;
    private boolean isDefault = false;
    int thumbnail;
    ImageButton profileImage;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private int PHOTO = 0;

    private RoundedAvatarDrawable roundedAvatarDrawable = null;
    private Intent myintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_modify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("바르미");

        editTextName = (EditText) findViewById(R.id.name);
        editTextMan = (EditText) findViewById(R.id.man);
        editTextBirth = (EditText) findViewById(R.id.birth);
        editTextAge = (EditText) findViewById(R.id.age);
        editTextWeight = (EditText) findViewById(R.id.weight);
        editTextHeight = (EditText) findViewById(R.id.height);
        editTextBlood = (EditText) findViewById(R.id.blood);


        profileImage = (ImageButton)findViewById(R.id.user_update_profile_imageButton);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAlert();
            }
        });

    }

    public void insert(View view){
        String name = editTextName.getText().toString();
        String man = editTextMan.getText().toString();
        String birth = editTextBirth.getText().toString();
        String age = editTextAge.getText().toString();
        String weight = editTextWeight.getText().toString();
        String height = editTextHeight.getText().toString();
        String blood = editTextBlood.getText().toString();

        insertToDatabase(name, man, birth, age, weight, height, blood);


        myintent = new Intent(getApplicationContext(), PrivacyInfoActivity.class);
        startActivity(myintent);
    }

    private void insertToDatabase(String name, String man, String birth, String age, String weight, String height, String blood){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PrivacyModifyActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String name = (String)params[0];
                    String man = (String)params[1];
                    String birth = (String)params[2];
                    String age = (String)params[3];
                    String weight = (String)params[4];
                    String height = (String)params[5];
                    String blood = (String)params[6];

                    String link="http://35.161.222.253/person_update.php";
                    String data  = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("man", "UTF-8") + "=" + URLEncoder.encode(man, "UTF-8");
                    data += "&" + URLEncoder.encode("birth", "UTF-8") + "=" + URLEncoder.encode(birth, "UTF-8");
                    data += "&" + URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(age, "UTF-8");
                    data += "&" + URLEncoder.encode("weight", "UTF-8") + "=" + URLEncoder.encode(weight, "UTF-8");
                    data += "&" + URLEncoder.encode("height", "UTF-8") + "=" + URLEncoder.encode(height, "UTF-8");
                    data += "&" + URLEncoder.encode("blood", "UTF-8") + "=" + URLEncoder.encode(blood, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(name, man, birth, age, weight, height, blood);
    }

    private void callAlert(){
        final CharSequence[] items = {"사진촬영","앨범 선택","이미지 없음"};

        new AlertDialog.Builder(this)
                .setTitle("업로드 할 이미지 선택")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0 :
                                doTakePhotoAction();
                                break;
                            case 1 :
                                doTakeAlbumAction();
                                break;
                            case 2 :
                                doTakeDefault();
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    //카메라에서 이미지 가져오기
    private void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = "tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
        isDefault = false;
    }
    //앨범에서 이미지 가져오기
    private void doTakeAlbumAction(){
        //앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
        isDefault = false;
    }
    private void doTakeDefault(){

        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.thumb_story);
        Bitmap bitmap = drawable.getBitmap();
        profileImage.setImageBitmap(bitmap);
        isDefault = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case CROP_FROM_CAMERA:
                //크롭이 된 후 이미지를 넘겨받아 임시파일까지 작동하는 알고리즘
                final Bundle extras = data.getExtras();
                Bitmap photo = null;
                if(extras != null){
                    photo = extras.getParcelable("data");
                    roundedAvatarDrawable = new RoundedAvatarDrawable(photo);
                    profileImage.setImageDrawable(roundedAvatarDrawable);
                    //profileImage.setImageBitmap(photo);
                }
                Log.d("MIMAGE ", getRealImagePath(mImageCaptureUri));
                File f = new File(getRealImagePath(mImageCaptureUri));
                OutputStream out = null;

                try {
                    f.createNewFile();
                    out = new FileOutputStream(f);
                    photo.compress(Bitmap.CompressFormat.PNG,100,out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case PICK_FROM_ALBUM:
            {
                mImageCaptureUri = data.getData();
            }
            case PICK_FROM_CAMERA:
            {
                //카메라를 자를 크기를 정함
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
                intent.putExtra("outputX", 280);
                intent.putExtra("outputY", 280);
                intent.putExtra("aspectX",1);
                intent.putExtra("aspectY",1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);
                break;
            }
        }
    }

    //////이미지 서버 전송 관련
    ///바뀐 이미지를 우리쪽 서버에 저장을 시킨다
    ///저장된 경로를 카카오 서버의 유저 thumb_path로 변경시켜준다
    public String getRealImagePath (Uri uriPath)
    {

        String []proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uriPath, proj, null, null, null);
        if (cursor == null){
            return uriPath.getPath();
        }else {
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String path = cursor.getString(index);

            path = path.replace(".", "_change.");

            return path;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.privacy_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}
