package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MyUserFeedActivity extends AppCompatActivity {


    LinearLayout mylinLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_user_feed);



        mylinLayout = findViewById(R.id.mylinLayout);


        String username = ParseUser.getCurrentUser().getUsername();

        setTitle(username + " - fotografije");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("username", username);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {  //pretrazivanje u fotografijama
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null && list.size() > 0){
                    for(ParseObject object : list){
                        ParseFile file = (ParseFile) object.get("image");

                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                if(e == null && bytes != null){
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    //postavljanje layouta i fotografija
                                    ImageView imageView = new ImageView(getApplicationContext());
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    ));
                                    View view = new View(getApplicationContext());
                                    view.setLayoutParams(new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            50,
                                            1.0f
                                    ));

                                    view.setBackgroundColor(12);

                                    /*Button myButton = new Button(getApplicationContext());
                                    myButton.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT));

                                    linLayout.addView(myButton);*/

                                    imageView.setImageBitmap(bitmap);
                                    mylinLayout.addView(imageView);
                                    mylinLayout.addView(view);
                                }
                            }
                        });
                    }
                }
            }
        });




    }
}
