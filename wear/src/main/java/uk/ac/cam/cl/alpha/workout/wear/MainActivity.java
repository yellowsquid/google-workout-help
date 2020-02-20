package uk.ac.cam.cl.alpha.workout.wear;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Constants;
import uk.ac.cam.cl.alpha.workout.shared.PureCircuit;
import uk.ac.cam.cl.alpha.workout.shared.Serializer;
import uk.ac.cam.cl.alpha.workout.shared.Signal;

public class MainActivity extends WearableActivity implements
        MessageClient.OnMessageReceivedListener {
    private static final String TAG = "MainActivity";
    private TextView statText;
    private byte[] circuit;

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        String messagePath = messageEvent.getPath();

        // FIXME: crashes if circuit is null.
        if(messageEvent.getData() == null) {
            return;
        }

        if (!(messagePath.equals(Constants.CIRCUIT_PATH) || messagePath.equals(Constants.SIGNAL_PATH)))  {
            return;
        }

        byte[] data = messageEvent.getData();
        Object message;

        try {
            message = Serializer.deserialize(data);
        } catch (ClassNotFoundException | IOException e) {
            Log.e(TAG, "Failed to receive message", e);
            return;
        }

        if (messagePath.equals(Constants.CIRCUIT_PATH)) {
            statText.setText(((PureCircuit) message).getName());
            circuit = data;
        } else if (messagePath.equals(Constants.SIGNAL_PATH)) {
            switch ((Signal) message) {
                case START:
                    openSportActivity();
                    break;
                case STOP:
                case PAUSE:
                case RESUME:
                    Log.w(TAG, "Signal not yet implemented");
            }
        } else {
            Log.w(TAG, "Unknown message");
        }
    }

    private void openSportActivity() {
        Intent intent = new Intent(this, SportActivity.class);
        intent.putExtra(SportActivity.CIRCUIT_ID, circuit);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        Wearable.getMessageClient(this).addListener(this);
        statText = findViewById(R.id.statusText);

        // Enables Always-on
        setAmbientEnabled();
    }
}


