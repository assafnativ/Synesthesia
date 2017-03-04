package com.synesthesia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class StartGame extends AppCompatActivity {

    // PI to the Feynman point
    protected final String PI = new String("314159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955058223172535940812848111745028410270193852110555964462294895493038196442881097566593344612847564823378678316527120190914564856692346034861045432664821339360726024914127372458700660631558817488152092096282925409171536436789259036001133053054882046652138414695194151160943305727036575959195309218611738193261179310511854807446237996274956735188575272489122793818301194912983367336244065664308602139494639522473719070217986094370277053921717629317675238467481846766940513200056812714526356082778577134275778960917363717872146844090122495343014654958537105079227968925892354201995611212902196086403441815981362977477130996051870721134999999");
    protected final String PHI = new String("16180339887498948482045868343656381177203091798057628621354486227052604628189024497072072041893911374847540880753868917521266338622235369317931800607667263544333890865959395829056383226613199282902678");
    public static final String SEQUENCE_TO_PLAY = "com.synesthesia.sequence_to_play";
    public String sequenceToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
    }

    public void onPiButtonClick(View view) {
        sequenceToPlay = PI;
        switchToGame();
    }

    public void onPhiButtonClick(View view) {
        sequenceToPlay = PHI;
        switchToGame();
    }

    public void onCustomNumberButtonClick(View view) {
        EditText customValue = (EditText)findViewById(R.id.customNumberText);
        String value = customValue.getText().toString();
        if (value.length() < 3) {
            return;
        }
        if (value.length() >= 10000) {
            return;
        }
        sequenceToPlay = value;
        switchToGame();
    }

    protected void switchToGame() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(SEQUENCE_TO_PLAY, sequenceToPlay);
        startActivity(intent);
    }
}
