package com.example.nick.smartpatroldrone;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class DroneControlActivity extends AppCompatActivity {
    private VideoView videoView;
    private Button takePictureButton;

    private Context context = null;
    WifiHandler gestoreWifi;

    private final String PATH = "tcp://192.168.1.1:5555/";  //Path per acquisizione stream video dal drone
    private final String SSID_DRONE = "ardrone2_044992";    //SSID di default del drone a cui connettersi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_control);
        context = getApplicationContext();

        videoView = (VideoView) findViewById(R.id.vitamio_videoView);
        takePictureButton = (Button) findViewById(R.id.takePhotoButton);

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto(null);
            }
        });
        gestoreWifi = new WifiHandler(getApplicationContext());
        if(gestoreWifi.getDroneConnected()) {
            //Drone connesso
            videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
            videoView.setBufferSize(4096);
            videoView.setVideoPath(PATH);
            videoView.requestFocus();
            videoView.setMediaController(new MediaController(this));
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setPlaybackSpeed(1.0f);
                }
            });
        }else{
            //Errore: drone non connesso
            Toast.makeText(context, "Errore connessione drone!", Toast.LENGTH_LONG).show();
            //Inserire redirect a MainActivity?
        }

    }

    private void capturePhoto(View view){
        try{
            PhotoSaver photoSaver = new PhotoSaver(context, videoView.getMediaPlayer());
            String imgPath = photoSaver.record();
            if(imgPath == null){
                //errore acquisizione foto
                Toast.makeText(getApplicationContext(), "Errore acquisizione foto", Toast.LENGTH_LONG);
            }else{
                //Foto acquisita con successo
                if(gestoreWifi.reconnectWifi() != true){
                    //Riconnesso alla wifi originaria
                    //Passo i dati
                }else{
                    //Errore
                }
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Picture error!", Toast.LENGTH_SHORT).show();
        }
    }

    /*private boolean getDroneConnected(){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            //Wi-Fi non attivo, richiesta attivazione
            if(wifiManager.setWifiEnabled(true) == false){
                //Errore accensione wifi
                return false;
            }
        }
        WifiInfo connInfo = wifiManager.getConnectionInfo();
        if(connInfo != null){
            //Il telefono è già connesso ad una rete, verifico se è il drone
            if(connInfo.getSSID().toString().equals(SSID_DRONE)){
                //Il telefono è già connesso al drone
                return true;
            }else{
                //Scollego il telefono dall'attuale rete
                wifiManager.disconnect();
            }
        }
        //Il telefono non è connesso alla wifi del drone
        if(!wifiManager.startScan()){
            //Errore avvio scansione reti wifi
            return false;
        }else{
            List<ScanResult> networksFound = wifiManager.getScanResults();
            for(ScanResult network : networksFound){
                if(network.SSID.equals(SSID_DRONE)){
                    //Tra le reti disponibili c'è la wifi del drone
                    List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
                    int netId = -1;
                    for(WifiConfiguration configured : configuredNetworks){
                        if(configured.SSID.equals(SSID_DRONE)){
                            //Rete del drone già tra quelle configurate
                            netId = configured.networkId;
                            break;
                        }
                    }
                    if(netId == -1){
                        //Rete del drone non ancora configurata
                        WifiConfiguration configuration = new WifiConfiguration();
                        configuration.SSID = SSID_DRONE;
                        //SE FOSSE UNA RETE WEP
                        /*configuration.wepKeys[0] ="\"" + networkPass + "\"";
                        configuration.wepTxKeyIndex = 0;
                        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

                        //SE FOSSE UNA RETE WPA DOVREMMO AGGIUNGERE
                        configuration.preSharedKey = "\"" + networkPass + "\"";*/
                        /*
                        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE); //POICHE' E' UNA RETE APERTA
                        netId = wifiManager.addNetwork(configuration);
                        if(netId == -1){
                            //Errore aggiunta network
                            return false;
                        }
                    }
                    if(wifiManager.enableNetwork(netId, true)){
                        //Dispositivo connesso alla wifi del drone
                        return true;
                    }else{
                        //Errore connessione alla wifi del drone
                        return false;
                    }
                }
            }
            //Rete del drone non è tra quelle disponibili
            //Inserire stampa elenco reti wifi così da consentire scelta
            return false;
        }
    }*/
}