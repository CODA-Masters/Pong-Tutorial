package com.codamasters.pong.helpers;

/**
 * Created by Juan on 29/04/2016.
 */
public interface ActionResolver {

    public void submitScoreWallMode(int score);
    public void displayLeaderboardWallMode();
    public void signIn();
    public void automaticSignIn();
    public void signOut();
    public void rateGame();
    public boolean isSignedIn();
    public void startQuickGame();
    public void sendPos(float y, float restart);

}
