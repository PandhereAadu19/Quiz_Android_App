package com.anushka.quizapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.anushka.quizapp.Models.QuestionModel;
import com.anushka.quizapp.R;
import com.anushka.quizapp.databinding.ActivityQuestionBinding;
import com.google.android.material.color.utilities.Score;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    ActionBar actionBar;
    ArrayList<QuestionModel> list = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer ;
    ActivityQuestionBinding binding;

    private long timerRemainingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check if ActionBar is available
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); // Hide the ActionBar if available
        } else {
            // Handle case where ActionBar is null (if needed)
        }

        restoreQuizState();

        reserTimer();
        timer.start();

        String setName = getIntent().getStringExtra("set");
        if(setName.equals("SET-1")){
            setOne();
        } else if (setName.equals("SET-2")) {
            setTwo();
        }

        for(int i = 0; i < 4; i++){
            binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer((Button) view);
                }
            });
        }

        playAnimation(binding.question, 0, list.get(position).getQuestion());

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(timer != null){
                    timer.cancel();
                }

                timer.start();
                binding.btnNext.setEnabled(false);
                binding.btnNext.setAlpha((float) 0.3);
                enableOption(true);
                position ++;

                if(position == list.size()){
                    Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
                    intent.putExtra("score", score);
                    intent.putExtra("total", list.size());
                    startActivity(intent);
                    finish();
                    return;
                }

                count = 0;

                playAnimation(binding.question, 0, list.get(position).getQuestion());

            }
        });
    }
    protected void onPause() {
        super.onPause();
        saveQuizState();
    }

    private void saveQuizState() {
        SharedPreferences sharedPreferences = getSharedPreferences("quiz_state", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save current position of question, score, and timer state
        editor.putInt("position", position);
        editor.putInt("score", score);
        editor.putBoolean("isTimerRunning", timer != null); // Save whether the timer is running or not

        editor.apply();
    }

    private void restoreQuizState() {
        SharedPreferences sharedPreferences = getSharedPreferences("quiz_state", MODE_PRIVATE);

        // Restore saved state (default values are used if not found)
        position = sharedPreferences.getInt("position", 0);
        score = sharedPreferences.getInt("score", 0);
        timerRemainingTime = sharedPreferences.getLong("timerRemainingTime", 0);

        // Restore timer state if there is remaining time
        if (timerRemainingTime > 0) {
            timer = new CountDownTimer(timerRemainingTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // Update UI with remaining time
                    binding.timer.setText(String.valueOf(millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    // Handle timer finish
                    // Show timeout dialog, etc.
                }
            }.start();
        }
    }

    private void reserTimer() {
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                binding.timer.setText(String.valueOf(l/1000));

            }

            @Override
            public void onFinish() {
                Dialog dialog = new Dialog(QuestionActivity.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout_dialog);
                dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QuestionActivity.this, SetsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                dialog.show();
            }
        };
    }

    private void playAnimation(View view, int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                if(value == 0 && count < 4){
                    String option = "";

                    if(count == 0){
                        option = list.get(position).getOptionA();
                    } else if (count == 1) {
                        option = list.get(position).getOptionB();
                    }else if (count == 2) {
                        option = list.get(position).getOptionC();
                    }else if (count == 3) {
                        option = list.get(position).getOptionD();
                    }

                    playAnimation(binding.optionContainer.getChildAt(count), 0, option);
                    count ++;

                }
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {

                if(value == 0){

                    try {

                        ((TextView)view).setText(data);
                        binding.totalQuestion.setText(position+1+"/"+list.size());
                    }catch (Exception e){
                        ((Button)view).setText(data);
                    }

                    view.setTag(data);
                    playAnimation(view, 1, data);

                }

            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });

    }

    private void enableOption(boolean enable) {

        for(int i = 0; i <4; i++){
            binding.optionContainer.getChildAt(i).setEnabled(enable);

            if (enable) {

                binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_opt);
            }

        }



    }

    private void checkAnswer(Button selectedOption) {

        if(timer != null){
            timer.cancel();
        }

        binding.btnNext.setEnabled(true);
        binding.btnNext.setAlpha(1);

        if(selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())){
            score ++;
            selectedOption.setBackgroundResource(R.drawable.right_ans);
        }
        else{
            selectedOption.setBackgroundResource(R.drawable.wrong_ans);

            Button correctOption = (Button) binding.optionContainer.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOption.setBackgroundResource(R.drawable.right_ans);
        }

    }

    private void setTwo() {

        list.add(new QuestionModel("1. Generally designing of timber structural floors used of ",
                "A. Hard wood", "B. Very light wood", "C. Soft wood", "D. Moisture content wood", "B. Very light wood"));

        list.add(new QuestionModel("2. For D.P.C at plinth level, the commonly adopted material is ",
                "A. Bitumen sheeting", "B. Mastic asphalt", "C. Plastic sheeting", "D. Cement concrete", "D. Cement concrete"));

        list.add(new QuestionModel("3. Slenderness ratio is defined as ratio of effective height to",
                "A. volume", "B. least lateral dimension", "C. surface area", "D. None of the above", "B. least lateral dimension"));

        list.add(new QuestionModel("4. The growth of algae is useful in",
                "A. Oxidation pond", "B. Slow and filter", "C. Sedimentation tank", "D. Sludge digestion tankin", "A. Oxidation pond"));

        list.add(new QuestionModel("5. Generally the primary source of all water supply is to be said as",
                "A. River", "B. Canal", "C. Impounding reservoirs", "D. Precipitation", "D. Precipitation"));


    }

    private void setOne() {

        list.add(new QuestionModel("1. The fundamental principle of surveying is to work from the ",
                "A. part of the whole", "B. whole of the part", "C. lower level to the higher level", "D. higher level to the lower level", "B. whole of the part"));

        list.add(new QuestionModel("2. If the smallest division of a vernier is longer than the smallest division of its primary scale, the vernier is known as :",
                "A. direct vernier", "B. double vernier", "C. rectrograde vernier", "D. simple vernier", "C. rectrograde vernier"));

        list.add(new QuestionModel("3. The horizontal angle between the true and magnetic meridian at a placed is called ?",
                "A. declination", "B. azimuth", "C. local attraction", "D. magnetic bearing", "A. declination"));

        list.add(new QuestionModel("4. Contour lines ",
                "A. cross each other", "B. end abruptly", "C. are uniformed placed", "D. magnetic bearing", "D. magnetic bearing"));

        list.add(new QuestionModel("5. The final setting time of ordinary cement should not be more than ",
                "A. 2 hours", "B. 4 hours", "C. 8 hours", "D. 10 hours", "D. 10 hours"));

    }
}