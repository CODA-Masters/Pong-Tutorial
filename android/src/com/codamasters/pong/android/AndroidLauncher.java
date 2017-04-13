package com.codamasters.pong.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.codamasters.pong.Pong;
import com.codamasters.pong.R;
import com.codamasters.pong.helpers.ActionResolver;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.GameHelper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AndroidLauncher extends AndroidApplication implements ActionResolver, RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener{

    private GameHelper gameHelper;
    private final static int requestCode = 1;
    final static int RC_WAITING_ROOM = 10002;
    private String mRoomId;
    private Pong pong;

    private String creatorID;
    private String myID;

    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        pong = new Pong(this);
		initialize(pong, config);

        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(true);


        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
        {
            @Override
            public void onSignInFailed(){ }

            @Override
            public void onSignInSucceeded(){ }
        };

        gameHelper.setup(gameHelperListener);

    }


    @Override
    protected void onStop() {
        gameHelper.onStop();
        super.onStop();
    }

    @Override
    public void onActivityResult(int request, int response, Intent intent) {

        if (request == RC_WAITING_ROOM) {
            if (response == Activity.RESULT_OK) {

                int side;
                if(creatorID.equals(myID)){
                    side = 0;
                    Toast.makeText(this, "YO EMPIEZO", Toast.LENGTH_SHORT).show();
                }else{
                    side = 1;
                    Toast.makeText(this, "TU EMPIEZAS", Toast.LENGTH_SHORT).show();
                }

                pong.startOnlineGame(side);
            }
            else if (response == Activity.RESULT_CANCELED) {
                // Waiting room was dismissed with the back button. The meaning of this
                // action is up to the game. You may choose to leave the room and cancel the
                // match, or do something else like minimize the waiting room and
                // continue to connect in the background.

                // in this example, we take the simple approach and just leave the room:
                Games.RealTimeMultiplayer.leave(gameHelper.getApiClient(), this, mRoomId);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            else if (response == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                // player wants to leave the room.
                Games.RealTimeMultiplayer.leave(gameHelper.getApiClient(), this, mRoomId);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }

        }
        else{
            super.onActivityResult(requestCode, response, intent);
            gameHelper.onActivityResult(requestCode, response, intent);
        }
    }

    @Override
    public void signIn() {
        try{
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                    Toast.makeText(getApplicationContext(), "After signing in click again to show the leaderboard.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e) {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void automaticSignIn(){
        gameHelper.beginUserInitiatedSignIn();
        gameHelper.getApiClient().connect();
    }

    @Override
    public void signOut(){
        try{
            runOnUiThread(new Runnable() {
                @Override
                public void run(){
                    gameHelper.signOut();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame()
    {
        String str = "Your PlayStore Link";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }


    @Override
    public void submitScoreWallMode(int highScore)
    {
        if (isSignedIn() == true) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), getString(R.string.wall_mode_ranking_id), highScore);
        }
    }


    @Override
    public void displayLeaderboardWallMode(){
        if (isSignedIn() == true){
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    getString(R.string.wall_mode_ranking_id)), 0);
        }
        else{
            automaticSignIn();
        }
    }

    @Override
    public boolean isSignedIn()
    {
        return gameHelper.isSignedIn();
    }

    @Override
    public void startQuickGame() {
        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).

        if (isSignedIn() == true){
            Bundle am = RoomConfig.createAutoMatchCriteria(1, 1, 0);

            // build the room config:
            RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
            roomConfigBuilder.setAutoMatchCriteria(am);
            RoomConfig roomConfig = roomConfigBuilder.build();

            // prevent screen from sleeping during handshake
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            });

            // create room:
            Games.RealTimeMultiplayer.create(gameHelper.getApiClient(), roomConfig);

        }
        else{
            automaticSignIn();
        }
    }

    // create a RoomConfigBuilder that's appropriate for your implementation
    private RoomConfig.Builder makeBasicRoomConfigBuilder() {
        return RoomConfig.builder(this)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Toast.makeText(getApplicationContext(), "Error joining room. Error Code : ", Toast.LENGTH_SHORT).show();
            return;
        }
        mRoomId = room.getRoomId();

        // get waiting room intent
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(gameHelper.getApiClient(), room, 2);
        startActivityForResult(i, RC_WAITING_ROOM);
    }

    @Override
    public void onRoomCreated(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Toast.makeText(getApplicationContext(), "Error creating room. Error Code : " + String.valueOf(statusCode), Toast.LENGTH_SHORT).show();
            return;
        }

        mRoomId = room.getRoomId();

        // get waiting room intent
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(gameHelper.getApiClient(), room, 2);
        startActivityForResult(i, RC_WAITING_ROOM);
    }

    @Override
    public void onLeftRoom(int i, String s) {

    }

    @Override
    public void onRoomConnected(int i, Room room) {
        // Todos los usuarios están conectados
        mRoomId = room.getRoomId();


        // Calculamos quien va empezar

        ArrayList<String> parcitipants = room.getParticipantIds();
        creatorID = parcitipants.get(0);
        myID = room.getParticipantId(Games.Players.getCurrentPlayerId(gameHelper.getApiClient()));

    }

    @Override
    public void sendPos(float y, float restart){
        try{
            byte[] mensaje;
            mensaje = ByteBuffer.allocate(8).putFloat(y).putFloat(restart).array();
            Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(gameHelper.getApiClient(), mensaje, mRoomId);

            /*RealTimeMultiplayer.ReliableMessageSentCallback reliableMessageSentCallback =  new RealTimeMultiplayer.ReliableMessageSentCallback() {
                @Override
                public void onRealTimeMessageSent(int i, int i1, String s) {

                }
            });

            Games.RealTimeMultiplayer.sendReliableMessage(gameHelper.getApiClient(),reliableMessageSentCallback, mensaje , "", "");*/
        }catch(Exception e){
        }
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage rtm) {
        float y;
        float restart;
        byte[] b = rtm.getMessageData();
        ByteBuffer bf = ByteBuffer.wrap(b);
        y = bf.getFloat();
        restart = bf.getFloat();
        pong.getOnlineGame().updateGame(y, restart);
    }

    @Override
    public void onRoomConnecting(Room room) {

    }

    @Override
    public void onRoomAutoMatching(Room room) {

    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {

    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {

    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {

    }

    @Override
    public void onPeerLeft(Room room, List<String> list) {

    }

    @Override
    public void onConnectedToRoom(Room room) {

    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        pong.getOnlineGame().updateGame(0, 9);
        Toast.makeText(getApplicationContext(), "Error. Connection finished.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {

    }

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {

    }

    @Override
    public void onP2PConnected(String s) {

    }

    @Override
    public void onP2PDisconnected(String s) {
        byte[] mensaje = ByteBuffer.allocate(8).putFloat(0).putFloat(9).array();
        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(gameHelper.getApiClient(), mensaje, mRoomId);
    }
}
