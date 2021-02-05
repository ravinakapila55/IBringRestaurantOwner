package com.ibring_restaurantowner.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ibring_restaurantowner.R;
import com.ibring_restaurantowner.utils.SharedHelper;
import com.ibring_restaurantowner.views.home.HomeScreen;
import java.util.Date;
import java.util.Map;

public class FirebaseMessages extends FirebaseMessagingService{

    private String title, message;
    private Map<String, String> data;
    private String type;
    Intent intent;
    private static final String TAG = "FCM";
    Notification.Builder builder;

    /*Todo Payload*/
/*
   [{
	"drop_location_lat": "30.7333148",
	"ride_id": 1481,
	"lng": "74.8722642",
	"distance": 128.75809648232607,
	"sign_up": "1",
	"drop_location_lng": "76.7794179",
	"booking_id": 773,
	"location_name": "Amritsar, Punjab, India",
	"reaccuring": [],
	"enddate": null,
	"ridestatus": 0,
	"priority_drop": "1",
	"price": 203.6371447234891,
	"drop_location_name": "Chandigarh, India",
	"priority_pick": "1",
	"booking_type": "2",
	"time": 4.15,
	"time_of_ride": "aa:23:45",
	"rider_id": "518",
	"lat": "31.6339793",
	"sign_in": "0"
}]
*/


/*{data={"restaurant_id":5,"latitude":"75.7551023","discount":null,"created_at":"2020-05-14 13:33:37","restaurant_commission":null,"updated_at":"2020-05-14 13:33:37","dine_in_commission":null,"payment_id":null,"price":"900","service_id":18,"id":127,"longitude":"31.7413754","payment_mode":"cash","item":"[{\"menu_id\":\"18\",\"quantity\":\"3\",\"item_name\":\"Deserts\",\"price\":\"300\"}]","hours":"0","minutes":"12","ibring_commission":null,"restaurant":{"image":"restaurant\/830d978f8d018caddb7108d7c428b8dc.png","hours":"0","address":"Garhdiwala, Punjab, India","lattitude":"31.7412342","minutes":"12","rating":"0","description":"kopko","title":"kapila90","deleted_at":null,"provider":{"latitude":null,"mobile":"5674321890","rating":"5.00","last_name":"new","description":null,"otp":0,"email_verified_at":"2018-12-22 18:12:34","avatar":null,"rating_count":0,"social_unique_id":null,"id":171,"first_name":"kapila","login_by":"manual","device":{"sns_arn":null,"provider_id":171,"id":115,"udid":"730d855e8af3eca2","type":"android","token":"cQkVonNR1RA:APA91bGaBcgtY1BcygyLIkvkkqsup7u5emV7vieNQ77Fo8LsKkMS000V7u-TdHgT2LLC7qf62Ai2TJa2XEGMCxyJ5Fq5fPWe7W4Hit4Hjjxx_S6TP24jH7l9gn2-76s_Dsu_nvgbqU8P"},"email":"kapila90@mailinator.com","status":"approved","longitude":null},"provider_id":171,"id":5,"status":"1","longitude":"75.7560455"},"tax":null,"registration_fee":null,"delivery_charge":null,"user_id":142,"location":"Bus Stop, Dasuya - Hoshiarpur Rd, Garhdiwala, Punjab 144207, India","order_id":"5EBD48B1B9D05","delivery_boy_id":null,"status":"pending"}, type=Order placed, message=Order placed}*/


//{data=[], type=Order placed, message=Order placed}
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        try
        {
            Log.e(TAG, "onMessageReceived "+remoteMessage);
            if (remoteMessage.getData() != null)
            {
                data = remoteMessage.getData();
                Log.e(TAG, "onMessageReceived:data "+remoteMessage.getData().get("data"));
//                Log.e(TAG, "onMessageReceived:Body "+remoteMessage.getNotification().getBody());
                Log.e(TAG, "onMessageReceivedMapp: "+data);

                type = data.get("type");
                message = data.get("message");
//                title = data.get("title");

                Log.e(TAG, "type "+type);
                Log.e(TAG, "message "+message);

                if(SharedHelper.Companion.getKey(this,"loggedIn").equalsIgnoreCase("true"))
                {
                    if (type.equalsIgnoreCase(""))
                    {
                        intent = new Intent(this, HomeScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(121);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        //showSmallNotification(message,title,pendingIntent);
                        sendNotificationnn(getApplicationContext(), intent, message, title);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void sendNotificationnn(Context context, Intent intent, String message, String title)
    {
        Log.e("MyMessageCheck ", "mmm hereee");
        Log.e("Message ", message);
        Log.e("title ", title);
        NotificationChannel mChannel = null;
        String NOTIFICATION_CHANNEL_ID = "1010";
        int NOTIFICATION_CODE = 10000;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                .setSubText(title)
                .setContentText(message);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            mChannel.setDescription(message);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            if (notificationManager != null)
            {
            notificationManager.createNotificationChannel(mChannel);
            }
        }

        else
        {
            builder.setContentTitle(context.getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setVibrate(new long[]{100, 250})
                    .setSound(uri)
                    .setContentText(message)
                    .setContentTitle(title)
                    .setLights(Color.GREEN, 500, 5000)
                    .setGroupSummary(true)
                    .setAutoCancel(true);
        }

        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_ALL;
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, notification);
    }

/*    private void showSmallNotification(String message, String title, PendingIntent pendingIntent)
    {
        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            // create android channel
            NotificationChannel androidChannel = new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            notificationManager.createNotificationChannel(androidChannel);

        }

        CharSequence cs = message;


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Taxi Nanny");
            builder = new Notification.Builder(getApplicationContext(), channelId)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(getNotificationIcon())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setStyle(new Notification.BigTextStyle().bigText(cs))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSound(defaultSoundUri)
                    .setWhen(0);
            notificationManager.notify(0, builder.build());

        } else {
            builder = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(getNotificationIcon())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setStyle(new Notification.BigTextStyle().bigText(cs))
                    .setAutoCancel(true)
                    .setWhen(0);
            notificationManager.notify(0, builder.build());
        }

    }*/

    private void sendNotification(String message, String title)
    {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(defaultSoundUri);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private static int getNotificationIcon()
    {
        boolean whiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        return whiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

}
