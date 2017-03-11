package hsy.barmiapp_android_third.notice;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import hsy.barmiapp_android_third.R;

public class NoticeModifyActivity extends AppCompatActivity {

    private EditText editTextId;
    private EditText editTextTitle;
    private EditText editTextUser;
    private EditText editTextCont;

    private Uri uri;
    int thumbnail;
    ImageButton Thumbnail_image;

    private static final int TAKE_PHOTO = 0;
    private static final int SELECT_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private int PHOTO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_modify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("바르미");

        editTextId = (EditText) findViewById(R.id.id);
        editTextTitle = (EditText) findViewById(R.id.title);
        editTextUser = (EditText) findViewById(R.id.user);
        editTextCont = (EditText) findViewById(R.id.cont);


        Thumbnail_image = (ImageButton) findViewById(R.id.photo);

        Thumbnail_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PHOTO = 0;

                AlertDialog fDialog = new AlertDialog.Builder(NoticeModifyActivity.this)
                        .setTitle("선택")
                        .setItems(R.array.file_dialog_list_item,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
								/* User clicked so do some stuff */
                                        if (which == 0) {
                                            doSelectAlbumAction();
                                        } else if (which == 1) {
                                            doTakePhotoAction();
                                        }
                                    }
                                }).create();
                fDialog.setCanceledOnTouchOutside(true);
                fDialog.show();
            }
        });


    }

    public void insert(View view){
        String id = editTextId.getText().toString();
        String title = editTextTitle.getText().toString();
        String user = editTextUser.getText().toString();
        String cont = editTextCont.getText().toString();

        insertToDatabase(id, title, user, cont);
    }

    private void insertToDatabase(String id, String title, String user, String cont){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NoticeModifyActivity.this, "Please Wait", null, true, true);
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
                    String id = (String)params[0];
                    String title = (String)params[1];
                    String user = (String)params[2];
                    String cont = (String)params[3];

                    String link="http://35.161.222.253/board_input.php";
                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8");
                    data += "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
                    data += "&" + URLEncoder.encode("cont", "UTF-8") + "=" + URLEncoder.encode(cont, "UTF-8");

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
        task.execute(id, title, user, cont);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            android.app.FragmentManager fm = getFragmentManager();

            // Uri에서 이미지 경로을 얻어온다.
            uri = data.getData();

            switch (requestCode) {
                case SELECT_ALBUM:
                case PICK_FROM_CAMERA: {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(uri, "image/*");
                    intent.putExtra("outputX", 280);
                    intent.putExtra("outputY", 280);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent,TAKE_PHOTO);
                    break;
                }

                case TAKE_PHOTO: {
                    final Bundle extras = data.getExtras();
                    Bitmap photo = null;
                    if(extras != null){
                        photo = extras.getParcelable("data");
                        if( PHOTO == 0 ) Thumbnail_image.setImageBitmap(photo);
                    }
//                    try {
//                        Bitmap image_bitmap = null;
//                        Bitmap re_bitmap = null;
//
//                        if (thumbnail == PHOTO1) {
//
//                            image_bitmap = ImageUtil.loadBackgroundBitmap(getBaseContext(), file_path);
//
//                            Log.d("abc", String.valueOf(image_bitmap));
//                            re_bitmap = ImageUtil.GetRotatedBitmap(image_bitmap, ImageUtil.GetExifOrientation(file_path));
//
//                            Thumbnail_image.setImageBitmap(re_bitmap);
//                        } else if (thumbnail == PHOTO2) {
//                            image_bitmap = ImageUtil.loadBackgroundBitmap(InputView.this, file_path2);
//                            re_bitmap = ImageUtil.GetRotatedBitmap(image_bitmap, ImageUtil.GetExifOrientation(file_path2));
//
//                            Thumbnail_image2.setImageBitmap(re_bitmap);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    break;
                }
            }
        }

    }

    private void doTakePhotoAction() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
    }

    private void doSelectAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, SELECT_ALBUM);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
        super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notice_modify, menu);
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
