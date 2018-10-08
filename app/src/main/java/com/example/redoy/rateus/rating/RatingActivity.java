package com.example.redoy.rateus.rating;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.redoy.rateus.utils.MyBounceInterpolator;
import com.example.redoy.rateus.R;
import com.example.redoy.rateus.report.ReportActivity;
import com.example.redoy.rateus.database.DatabaseHelper;
import com.example.redoy.rateus.database.Rating;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RatingActivity extends AppCompatActivity {

    private ImageView mGoodImageView, mAverageImageView, mBadImageView, mReportImageView;

    private MediaPlayer mPlayer;

    private int mGoodCount = 0;
    private int mAverageCount = 0;
    private int mBadCount = 0;

    private DatabaseHelper db;

    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        mGoodImageView = findViewById(R.id.good_image_view);
        mAverageImageView = findViewById(R.id.average_image_view);
        mBadImageView = findViewById(R.id.bad_image_view);
        mReportImageView = findViewById(R.id.report_image_view);

        db = new DatabaseHelper(this);

        Rating rating = db.getRating(date);

        if (rating != null) {
            if (rating.getDate().equals(date)) {
                mGoodCount = rating.getGood();
                mAverageCount = rating.getAverage();
                mBadCount = rating.getBad();
            }
        }

        mGoodImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoodCount++;
                animateButton(mGoodImageView, mGoodCount, "Its Good!", " Good Rating Given");
                createRating(date, mGoodCount, mAverageCount, mBadCount);
            }
        });

        mAverageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAverageCount++;
                animateButton(mAverageImageView, mAverageCount, "Its Average!", " Average Rating Given");
                createRating(date, mGoodCount, mAverageCount, mBadCount);
            }
        });

        mBadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBadCount++;
                animateButton(mBadImageView, mBadCount, "Its Bad!", " Bad Rating Given");
                createRating(date, mGoodCount, mAverageCount, mBadCount);
            }
        });

        mReportImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RatingActivity.this, ReportActivity.class));
            }
        });
    }

    void animateButton(ImageView mImageView, final int mCount, final String mTitle, final String mMessage) {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        double animationDuration = 0.3 * 1000;
        myAnim.setDuration((long) animationDuration);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(5.0, 5.0);
        myAnim.setInterpolator(interpolator);

        mImageView.startAnimation(myAnim);

        playSound();

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {

                final SweetAlertDialog success = new SweetAlertDialog(RatingActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(mTitle)
                        .setContentText(mCount + mMessage);

                success.show();
                new CountDownTimer(5000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        success.dismiss();
                    }
                }.start();
            }
        });
    }

    void playSound() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.reset();
        }

        mPlayer = MediaPlayer.create(RatingActivity.this, R.raw.bubble);
        mPlayer.start();
    }

    public void onDestroy() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer = null;
        }
        super.onDestroy();
    }

    private void createRating(String date, int good, int average, int bad) {
        Rating rating = db.getRating(date);
        if (rating != null) {
            if (rating.getDate().equals(date)) {
                db.updateRating(new Rating(date, good, average, bad));
            }
        } else {
            db.insertRating(date, good, average, bad);
        }
    }
}
