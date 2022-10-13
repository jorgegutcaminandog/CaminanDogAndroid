package mx.com.caminandog;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.app.Notification.VISIBILITY_PUBLIC;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    NotificationCompat.Builder mBuilder;
    String CHANNEL_ID = "main_channel";
    int mNotificationId = 1;

    public boolean shouldSound = true;
    public boolean shouldVibrate = true;
    NotificationManager notificationManager;






    public MyFirebaseMessagingService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();

            String title = data.get("title");
            String message = data.get("body");

            Intent resultIntent = new Intent(this, MainActivity.class);

            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_ONE_SHOT
            );


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logotipo)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setContentIntent(resultPendingIntent);


            int notificationId = (int) System.currentTimeMillis();
            mBuilder.setSound(alarmSound);
            mBuilder.setContentIntent(resultPendingIntent);


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (manager != null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                            "Human Readable channel id",
                            NotificationManager.IMPORTANCE_HIGH);
                    channel.setShowBadge(true);
                    manager.createNotificationChannel(channel);

                }

                manager.notify(notificationId, mBuilder.build());
            }
        }



        //createNotificationChannel();

        String id = remoteMessage.getData().get((getResources().getString(R.string.Asunto)));

        //makeNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

        /*notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (isOreoOrAbove()) {
            setupNotificationChannels();
        }*/

        /*if (Build.VERSION.SDK_INT   >=   Build.VERSION_CODES.O){
            if (id.equals("chatCaminandog")){
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("EXTRA", "NotifChat");
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logotipo)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(1, builder.build());

            }else {

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logotipo)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(1, builder.build());

            }

        }else{
            Uri sound = Uri.parse( ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.guau);

            NotificationManager mNotificationManger = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID);

            mBuilder.setSmallIcon(getNotificationIcon())
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    //.setLights(Color.WHITE,100,1000)
                    //.setVibrate(new long[]{100,200,300,400,500,400,300,200,400})
                    .setSound(sound)
                    .setAutoCancel(false)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.logotipo);
                    //.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);

            mNotificationManger.notify(mNotificationId,mBuilder.build());







        }*/
















/*
        //Uri sound = Uri.parse( ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.guau);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        Intent resultIntent = new Intent(getApplication() , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplication(),
                0 , resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);



        Intent resultIntentChat = new Intent(getApplication() , MainActivity.class);
        resultIntentChat.putExtra("EXTRA", "NotifChat");
        resultIntentChat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntentChat = PendingIntent.getActivity(getApplication(),
                0 , resultIntentChat,
                PendingIntent.FLAG_UPDATE_CURRENT);





        NotificationManager mNotificationManger = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID);
        if (Build.VERSION.SDK_INT   >=   Build.VERSION_CODES.O){
            CharSequence name = "Recuperandog_channel";
            String descripcion = "notificaciones recuperandog";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name , importance);
            mChannel.setDescription(descripcion);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            mChannel.setLockscreenVisibility(VISIBILITY_PUBLIC);
            mChannel.setShowBadge(true);
            //mChannel.setSound(sound,audioAttributes);
            mChannel.describeContents();




            mNotificationManger.createNotificationChannel(mChannel);

            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);


        }

        if (id.equals("chatCaminandog")){
            mBuilder.setSmallIcon(getNotificationIcon())
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setLights(Color.WHITE,100,1000)
                    .setVibrate(new long[]{100,200,300,400,500,400,300,200,400})
                    //.setSound(sound)
                    .setAutoCancel(false)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setContentIntent(resultPendingIntentChat);

        }else {
            mBuilder.setSmallIcon(getNotificationIcon())
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setLights(Color.WHITE,100,1000)
                    .setVibrate(new long[]{100,200,300,400,500,400,300,200,400})
                    //.setSound(sound)
                    .setAutoCancel(false)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setContentIntent(resultPendingIntent);
        }







        mNotificationManger.notify(mNotificationId,mBuilder.build());*/



    }



    public void sendNotification(RemoteMessage remoteMessage) {

    }










    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.logotipo: R.drawable.logotipo;
    }




}
