/**
 * Created by nativ on 2017-02-23.
 */

package com.synesthesia;

public class Game {
    public enum GameState {
        START,
        WAITING_FOR_PLAYER,
        PLAYING_SEQUENCE,
        GAME_OVER
    }
    protected int[] sequence;
    protected int nextNumberIndex;
    protected int playerIndex;
    protected int playingIndex;

    public Game(String playSequence) {
        String sequenceStr = playSequence;
        isSequenceValid(sequenceStr);
        sequence = sequenceFromString(sequenceStr);
        reset();
    }

    protected int[] sequenceFromString(String str) {
        int[] result = new int[str.length()];
        for (int i = 0; str.length() > i; ++i) {
            result[i] = str.charAt(i) - '0';
        }
        return result;
    }

    protected boolean isSequenceValid(String sequenceStr) {
        for (int i = 0; sequenceStr.length() > i; ++i) {
            char c = sequenceStr.charAt(i);
            if ((c < '0') || (c > '9')) {
                return false;
            }
        }
        return true;
    }

    public void reset()
    {
        nextNumberIndex = 1;
        playerIndex = 0;
        playingIndex = 0;
    }

    int getNextNumber() {
        if (isEndOfSequence()) {
            throw new RuntimeException("Next number out of sequence");
        }
        playerIndex = 0;
        playingIndex = 0;
        int result = sequence[nextNumberIndex];
        nextNumberIndex += 1;
        return result;
    }

    int getPrevNumber() {
        if (0 < nextNumberIndex) {
            nextNumberIndex -= 1;
        }
        playerIndex = 0;
        playingIndex = 0;
        return sequence[nextNumberIndex];
    }

    int getNextNumberToPlay() {
        if (isPlayingDone()) {
            throw new RuntimeException("Playing out of sequance");
        }
        int result = sequence[playingIndex];
        playingIndex += 1;
        return result;
    }

    int getNextPlayerNumber() {
        if (isPlayerDoneHisTurn()) {
            throw new RuntimeException("Player out of sequance");
        }
        int result = sequence[playerIndex];
        playerIndex += 1;
        return result;
    }

    void resetPlayer() {
        playerIndex = 0;
    }

    boolean isEndOfSequence() {
        return (nextNumberIndex >= sequence.length);
    }
    boolean isPlayerDoneHisTurn() { return (playerIndex >= nextNumberIndex); }
    boolean isPlayingDone() { return (playingIndex >= nextNumberIndex); }
}
