package com.synesthesia;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    protected static class ClickersInfo {
        public int value;
        public String name;
        public int color;
        public int soundId;
        public ClickersInfo(int _value, String _name, int _color, int _soundId) {
            name = _name;
            value = _value;
            color = _color;
            soundId = _soundId;
        }

        public Button button;
        public int clickSound;
        public ShapeDrawable clickedShape;
        public ShapeDrawable idleShape;
    }
    protected final static ClickersInfo clickers[] = {
            new ClickersInfo( 1, "One",   0x7ff40000, R.raw.dtmf_1 ),
            new ClickersInfo( 2, "Two",   0x7ff47a00, R.raw.dtmf_2 ),
            new ClickersInfo( 3, "Three", 0x7fffe314, R.raw.dtmf_3 ),
            new ClickersInfo( 4, "Four",  0x7f55aa11, R.raw.dtmf_4 ),
            new ClickersInfo( 5, "Five",  0x7f2255bb, R.raw.dtmf_5 ),
            new ClickersInfo( 6, "Six",   0x7f333388, R.raw.dtmf_6 ),
            new ClickersInfo( 7, "Seven", 0x7f772277, R.raw.dtmf_7 ),
            new ClickersInfo( 8, "Eight", 0x7fff54ff, R.raw.dtmf_8 ),
            new ClickersInfo( 9, "Nine",  0x7fb0b0b0, R.raw.dtmf_9 ),
            new ClickersInfo( 0, "Zero",  0x7f303030, R.raw.dtmf_0 )
    };
    protected static int BUTTONS_ID = 0x100;
    protected RelativeLayout mainLayout;
    protected SoundPool soundPool;
    final int TOP_ID = 0xff;
    final int BOTTOM_ID = 0xfe;
    protected Game game;
    protected TextView counterTextBox;
    protected int lastTone;
    protected void initClickers()
    {
        for (int i = 0; i < clickers.length; ++i) {
            if (null != clickers[i].button) {
                continue;
            }
            clickers[i].button = new Button(this);
            OvalShape clickerIdleShape = new OvalShape();
            OvalShape clickerClickedShape = new OvalShape();
            ShapeDrawable clickerIdleDrawable = new ShapeDrawable(clickerIdleShape);
            ShapeDrawable clickerClickedDrawable = new ShapeDrawable(clickerClickedShape);
            clickerIdleDrawable.getPaint().setColor(getClickerColor(i));
            clickerClickedDrawable.getPaint().setColor(makeColorDarker(getClickerColor(i), 20));
            clickers[i].idleShape = clickerIdleDrawable;
            clickers[i].clickedShape = clickerClickedDrawable;

            clickers[i].button.setBackground(clickerIdleDrawable);
            clickers[i].button.setText(clickers[i].name);
            clickers[i].button.setAllCaps(true);
            clickers[i].button.setId(BUTTONS_ID + i);
            clickers[i].button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickerClick(view);
                }
            });

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(15, 15, 0, 0);
            if (0 < i) {
                if (0 != (i % 3)) {
                    lp.addRule(RelativeLayout.RIGHT_OF, clickers[i - 1].button.getId());
                }
                if (i >= 3) {
                    lp.addRule(RelativeLayout.BELOW, clickers[i - 3].button.getId());
                }
            }
            mainLayout.addView(clickers[i].button, lp);
        }
    }

    protected int getClickerColor(int clickerIndex) {
        return clickers[clickerIndex].color;
    }

    protected void onClickerClick(View view) {
        int clickerIndex = view.getId() - BUTTONS_ID;
        mainLayout.setBackgroundColor(getClickerColor(clickerIndex));
        soundPool.play(clickers[clickerIndex].clickSound, 1f, 1f, 1, 0, 1f);
    }

    protected void loadSounds()
    {
        if (null != soundPool) {
            return;
        }

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(clickers.length + 1)
                .setAudioAttributes(audioAttributes)
                .build();
        for (int i = 0; i < clickers.length; ++i) {
            clickers[i].clickSound = soundPool.load(this, clickers[i].soundId, 1);
        }
    }

    protected int makeColorDarker(int c, int darkPercent) {
        int red = Color.red(c);
        int green = Color.green(c);
        int blue = Color.blue(c);
        red = Math.max(0, red - (red * darkPercent / 100));
        green = Math.max(0, green - (green * darkPercent / 100));
        blue = Math.max(0, blue - (blue * darkPercent / 100));
        return Color.rgb(red, green, blue);
    }

    protected void playTone(int tone) {
        soundPool.play(clickers[tone].clickSound, 1f, 1f, 1, 0, 1f);
    }

    protected void updateCounter() {
        counterTextBox.setText(Integer.toString(game.nextNumberIndex));
    }

    protected void makeClickerDark(int clickerIndex) {
        clickers[clickerIndex].button.setBackground(
                clickers[clickerIndex].clickedShape);
    }

    protected void setClickerToDefaultColor(int clickerIndex) {
        clickers[clickerIndex].button.setBackground(
                clickers[clickerIndex].idleShape);
    }

    protected int numberToClickerIndex(int num) {
        if (0 == num) {
            return 9;
        }
        return num - 1;
    }

    protected void playNext() {
        lastTone = game.getNextNumber();
        int clickerIndex = numberToClickerIndex(lastTone);
        makeClickerDark(clickerIndex);
        playTone(clickerIndex);
        Handler restoreClickerColorHandler = new Handler();
        restoreClickerColorHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setClickerToDefaultColor(numberToClickerIndex(lastTone));
            }
        }, 200);
        updateCounter();
    }

    protected void startGame() {
        playNext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == mainLayout) {
            setContentView(R.layout.activity_game);
            mainLayout = (RelativeLayout)findViewById(R.id.activity_game);
        }
        counterTextBox = (TextView)findViewById(R.id.counterTextBox);
        loadSounds();
        initClickers();

        game = new Game();
        updateCounter();
        Handler startGameHandler = new Handler();
        startGameHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startGame();
            }
        }, 1000);
    }
}
