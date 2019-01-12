package ch.heigvd.iict.sym.sym_labo4;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import ch.heigvd.iict.sym.wearcommon.Constants;

public class NotificationActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1; //code to use for the notification id
    private static final int NOTIFICATION_ID2 = 2; //code to use for the notification id
    private static final int NOTIFICATION_ID21 = 21; //code to use for the notification id
    private static final int NOTIFICATION_ID3 = 3; //code to use for the notification id
    private static final int NOTIFICATION_ID31 = 31; //code to use for the notification id

    String id = "mon_id";
    final static String CHANNEL_ID = "Labo4";
    final static String CHANNEL_ID2 = "Action Button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        TextView button = findViewById(R.id.btn_simple_notification);
        TextView button1 = findViewById(R.id.btn_notification_actions_button);
        TextView button2 = findViewById(R.id.btn_notification_wearable_only);


        if(getIntent() != null)
            onNewIntent(getIntent());


        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = getString(R.string.channel_name);
            CharSequence name = "my channel";
            String description = "super channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }



        /* A IMPLEMENTER */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            PendingIntent viewPendingIntent = createPendingIntent(NOTIFICATION_ID, CHANNEL_ID);
            NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_alert_white_18dp)
                    .setContentTitle("Attention !!!")
                    .setContentText("Notification")
                    .setContentIntent(viewPendingIntent);


            NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PendingIntent viewPendingIntent = createPendingIntent(NOTIFICATION_ID21, "SMS de réponse");

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_directions_car_black_18dp)
                                .setContentTitle("Besoin d'une nouvelle voiture ?")
                                .setContentText("Jouez et gagnez une nouvelle voiture")
                                .setContentIntent(viewPendingIntent)
                                .addAction(R.drawable.ic_message_bulleted_black_18dp,
                                        getString(R.string.reponse), viewPendingIntent);

                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(getApplicationContext());

                notificationManager.notify(NOTIFICATION_ID2, notificationBuilder.build());


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PendingIntent viewPendingIntent = createPendingIntent(NOTIFICATION_ID31, "SMS de réponse");

                NotificationCompat.Action action =
                        new NotificationCompat.Action.Builder(R.drawable.ic_lightbulb_on_white_18dp,
                                getString(R.string.reponse2), viewPendingIntent)
                                .build();


                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_motorcycle_black_18dp)
                                .setContentTitle("Votre moto est cassé ?")
                                .setContentText("Avez-vous besoin d'aide pour la réparer")
                                .setContentIntent(viewPendingIntent)
                                .extend(new NotificationCompat.WearableExtender()
                                    .addAction(action));

                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(getApplicationContext());

                notificationManager.notify(NOTIFICATION_ID3, notificationBuilder.build());


            }
        });


    }

    /* A IMPLEMENTER */

    /*
     *  Code fourni pour les PendingIntent
     */

    /*
     *  Method called by system when a new Intent is received
     *  Display a toast with a message if the Intent is generated by
     *  createPendingIntent method.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent == null) return;
        if(Constants.MY_PENDING_INTENT_ACTION.equals(intent.getAction())) {
            Toast.makeText(this, "" + intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
            NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID); //we close the notification
        }
    }

    /**
     * Method used to create a PendingIntent with the specified message
     * The intent will start a new activity Instance or bring to front an existing one.
     * See parentActivityName and launchMode options in Manifest
     * See https://developer.android.com/training/notify-user/navigation.html for TaskStackBuilder
     * @param requestCode The request code
     * @param message The message
     * @return The pending Intent
     */
    private PendingIntent createPendingIntent(int requestCode, String message) {
        Intent myIntent = new Intent(NotificationActivity.this, NotificationActivity.class);
        myIntent.setAction(Constants.MY_PENDING_INTENT_ACTION);
        myIntent.putExtra("msg", message);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(myIntent);

        return stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
