package com.example.lookarounddemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.DownloadManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lookarounddemo.data.User;
import com.example.lookarounddemo.widget.ItemGroup;
import com.example.lookarounddemo.widget.RoundImageView;
import com.jpeng.jptabbar.JPTabBar;
import org.jetbrains.annotations.Nullable;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jpeng on 16-11-14.
 */
public class UserPager extends Fragment implements View.OnClickListener {
    public static String userName = "";
    private static final String TAG = "";
    public static String my_image = "";
    public static int flag = 0;
    public static int flag_register = 0;
    public static int flag_image = 0;
    private ImageView vi;
    private ImageView back;
    private FrameLayout phone;
    private FragmentManager fragmentManager;
    private FragmentTransaction beginTransaction;
    public String t = "test";
    private FrameLayout edit;
    private ItemGroup name;
    private RoundImageView image;
    private AppCompatImageView picture;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.persona_info_page,null);
        return layout;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        image = (RoundImageView) getActivity().findViewById(R.id.ri_portrait);
        picture = getActivity().findViewById(R.id.ri_portrait);
        ItemGroup t = this.getView().findViewById(R.id.ig_name);
        if (UserPager.flag == 0){
            UserPager.flag = 1;
            t.getContentEdt().setText(User.getName());
        }
        if (UserPager.flag == 1){
            t.getContentEdt().setText(User.getName());
        }
        if(flag_image == 1){
            Bitmap bitmap = BitmapFactory.decodeFile(my_image);
            picture.setImageBitmap(bitmap);

        }
        edit = (FrameLayout) getActivity().findViewById(R.id.ig_name);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditUsernameActivity.class);
                startActivity(intent);
            }
        });
        back = (ImageView) getActivity().findViewById(R.id.iv_backward);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControllerActivity activity = (ControllerActivity) getActivity();
                activity.setCurrentItem(0);
            }
        });
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission((ControllerActivity)getActivity() ,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((ControllerActivity)getActivity(),new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    flag_image = 1;
                    openAlbum();
                }
            }
        });
    }
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    flag_image = 1;
                    openAlbum();
                }else{
                    Toast.makeText(getActivity(),"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            if(Build.VERSION.SDK_INT >= 19){
                handleImageOnKitKat(data);
            }
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(),uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
            else if("content".equalsIgnoreCase(uri.getScheme())){
                imagePath = getImagePath(uri,null);
            }
            else if ("file".equalsIgnoreCase(uri.getScheme())){
                imagePath = uri.getPath();
            }
            my_image = imagePath;
            displayImage(imagePath);
        }
    }
    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            System.out.println("bitmap:" + bitmap.toString());
            System.out.println("imagePath:" + imagePath);
            picture.setImageBitmap(bitmap);
        }else{
            Toast.makeText(getActivity(),"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        JPTabBar tabBar = (JPTabBar) ((Activity)getContext()).findViewById(R.id.tabbar);
        tabBar.setTabTypeFace("fonts/Jaden.ttf");
    }
}
