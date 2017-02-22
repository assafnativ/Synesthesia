package com.assafnativ.synesthesia;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

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
    }
    protected final static ClickersInfo CLICKERS_INFO[] = {
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
    protected static Button[] clickers;
    protected RelativeLayout mainLayout;
    protected static int clickersSounds[];
    protected SoundPool soundPool;
    final int TOP_ID = 0xff;
    final int BOTTOM_ID = 0xfe;
    protected void initClickers()
    {
        if ((null != clickers) && (clickers.length == CLICKERS_INFO.length)) {
            return;
        }
        mainLayout = new RelativeLayout(this);

        clickers = new Button[CLICKERS_INFO.length];
        for (int i = 0; i < clickers.length; ++i) {
            clickers[i] = new Button(this);
            OvalShape clickerShape = new OvalShape();
            ShapeDrawable drawable = new ShapeDrawable(clickerShape);
            drawable.getPaint().setColor(CLICKERS_INFO[i].color);

            clickers[i].setBackground(drawable);
            clickers[i].setText(CLICKERS_INFO[i].name);
            clickers[i].setAllCaps(true);
            clickers[i].setId(BUTTONS_ID + i);
            clickers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickerClick(view);
                }
            });

            if (0 == i) {
                mainLayout.addView(clickers[i]);
            } else {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(15, 15, 0, 0);
                if (0 != (i % 3)) {
                    lp.addRule(RelativeLayout.RIGHT_OF, clickers[i - 1].getId());
                }
                if (i >= 3) {
                    lp.addRule(RelativeLayout.BELOW, clickers[i - 3].getId());
                }
                mainLayout.addView(clickers[i], lp);
            }
        }
        this.setContentView(mainLayout);
    }

    protected void onClickerClick(View view) {
        int clickerIndex = view.getId() - BUTTONS_ID;
        mainLayout.setBackgroundColor(CLICKERS_INFO[clickerIndex].color);

        soundPool.play(clickersSounds[clickerIndex], 1f, 1f, 1, 0, 1f);
    }

    protected void loadSounds()
    {
        if ((null != clickersSounds) && (clickersSounds.length == CLICKERS_INFO.length)) {
            return;
        }

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(CLICKERS_INFO.length + 1)
                .setAudioAttributes(audioAttributes)
                .build();
        clickersSounds = new int[CLICKERS_INFO.length];
        for (int i = 0; i < clickersSounds.length; ++i) {
            clickersSounds[i] = soundPool.load(this, CLICKERS_INFO[i].soundId, 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        loadSounds();
        initClickers();
    }


}
