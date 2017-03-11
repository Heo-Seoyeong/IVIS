package hsy.barmiapp_android_third.intro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by hsy on 2016. 12. 29..
 */

public class KakaoSignupActivity extends Activity {
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

    private static final int MY_PERMISSION_REQUEST_STORAGE = 3;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "UserUpdateActivity";
    private String tokentoken;
    //KEY
    private  static final String NICK_NAME_KEY = "nick";
    private  static final String NAME_KEY = "name";
    private  static final String SCHOOLNUM_KEY = "schoolnum";
    private  static final String PHONENUM_KEY = "phonenum";
    private  static final String THUMBNAIL_IMAGE = "    //KEY\n" +
            "    private  static final String NICK_NAME_KEY = \"nick\";\n" +
            "    private  static final String NAME_KEY = \"name\";\n" +
            "    private  static final String SCHOOLNUM_KEY = \"schoolnum\";\n" +
            "    private  static final String PHONENUM_KEY = \"phonenum\";\n" +
            "    private  static final String THUMBNAIL_IMAGE = \"thumb_path\";thumb_path";
    //    private  static final String BIRTH_KEY = "birth";
    //USER PROPERTY
    private String barcodeNum = null;


    private int mWidthPixels, mHeightPixels;




    private HashMap<String,String> mapA = null;


    //카카오 유저파일 콜백 함수를 통해서 카카오 서버에 유저 정보를 불러온다.
    private UserProfile userProfile;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Logger.d("UserProfile : 실패");
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Logger.d("UserProfile : " + userProfile);
                Log.d("abc","5" + userProfile.getNickname() + "id : " + userProfile.getId());
                UploadUserData(""+userProfile.getId(),userProfile.getNickname());
                Log.d("abc","6" + userProfile.getThumbnailImagePath());
                // new UploadUser().execute(""+userProfile.getId(),userProfile.getNickname());
                //    getKakaoUserInfo();
                redirectMainActivity(); // 로그인 성공시 MainActivity로
            }
        });
    }

    private abstract class UsermgmtResponseCallback<T> extends ApiResponseCallback<T> {
        @Override
        public void onNotSignedUp() {
            //redirectSignupActivity();
        }

        @Override
        public void onFailure(ErrorResult errorResult) {
            String message = "failed to get user info. msg=" + errorResult;
            Logger.e(message);
            //KakaoToast.makeToast(self, message, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            //redirectLoginActivity();
        }
    }

    protected void UploadUserData(String kakaoid, String nicname) {
        class UploadUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(KakaoSignupActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }


            @Override
            protected String doInBackground(String... params) {
                try {
                    String thumbnailImage;

                    thumbnailImage = userProfile.getThumbnailImagePath();

                    String kakaoid = params[0];
                    String nicname = params[1];

                    Log.d("abc","1" + kakaoid);
                    Log.d("abc","2" + nicname);
                    Log.d("abc","3" + thumbnailImage);

                    String link="http://35.161.222.253/upload_user_info.php";
                    String data  = URLEncoder.encode("kakaoid", "UTF-8") + "=" + URLEncoder.encode(kakaoid, "UTF-8");
                    data += "&" + URLEncoder.encode("nicname", "UTF-8") + "=" + URLEncoder.encode(nicname, "UTF-8");
                    data += "&" + URLEncoder.encode("thumbnailImage", "UTF-8") + "=" + URLEncoder.encode(thumbnailImage, "UTF-8");


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
                    // Log.d("abc","8" + userProfile.getNickname());
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

//        Log.d("abc","7" + userProfile.getNickname());
        UploadUser g = new UploadUser();
        g.execute(kakaoid, nicname);
    }


    private void redirectMainActivity() {
        startActivity(new Intent(this, MainInfoActivity.class));
        finish();
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}