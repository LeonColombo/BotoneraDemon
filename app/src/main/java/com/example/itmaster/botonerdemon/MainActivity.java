package com.example.itmaster.botonerdemon;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity
{
    private Button btnApleno, btnExtremo, btnTanBuena, btnRisa, btnEn, btnEl, btnSitua, btnInfierno, btnLarira, btnSi, btnNo,btnDisney;
    private MediaPlayer playerApleno, playerExtremo, playerTanBuena, playerRisa;
    private ImageButton buttonStopPlayingRecording, buttonPlayLastRecordAudio, buttonStart, buttonStop,btnCompartir;
    private String AudioSavePathInDevice = null;
    private MediaRecorder mediaRecorder;
    private Random random;
    private String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnApleno = findViewById(R.id.btnApleno);
        btnExtremo = findViewById(R.id.btnExtremo);
        btnTanBuena = findViewById(R.id.btnTanBuena);
        btnRisa = findViewById(R.id.btnRisa);
        btnEn = findViewById(R.id.btnEn);
        btnEl = findViewById(R.id.btnEl);
        btnSitua = findViewById(R.id.btnSitua);
        btnInfierno = findViewById(R.id.btnInfierno);
        btnLarira = findViewById(R.id.btnLarira);
        btnSi = findViewById(R.id.btnSi);
        btnNo = findViewById(R.id.btnNo);
        btnDisney = findViewById(R.id.btnDisney);

        buttonStart = findViewById(R.id.btnStart);
        buttonStop = findViewById(R.id.btnStop);
        buttonPlayLastRecordAudio = findViewById(R.id.btnPlay);
        buttonStopPlayingRecording = findViewById(R.id.btnStopPlay);

        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWhatsAppAudio();
            }
        });

        random = new Random();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);

                        buttonStart.startAnimation(myAnim);

                        mediaRecorder.prepare();
                        mediaRecorder.start();

                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);

                    Toast.makeText(MainActivity.this, "GRABANDO",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);

                buttonStop.startAnimation(myAnim);

                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);

                Toast.makeText(MainActivity.this, "GRABACION FINALIZADA",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);

                buttonPlayLastRecordAudio.startAnimation(myAnim);

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(MainActivity.this, "REPRODUCIENDO",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);

                buttonStopPlayingRecording.startAnimation(myAnim);

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });

    }



    public void MediaRecorderReady(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
    public void sendWhatsAppAudio(){
        try {
            //Copy file to external ExternalStorage.
            String mediaPath = copyFiletoExternalStorage(1,AudioSavePathInDevice);

            Intent shareMedia = new Intent(Intent.ACTION_SEND);
            //set WhatsApp application.
            shareMedia.setPackage("com.whatsapp");
            shareMedia.setType("audio/*");
            //set path of media file in ExternalStorage.
            shareMedia.putExtra(Intent.EXTRA_STREAM, Uri.parse(mediaPath));
            startActivity(Intent.createChooser(shareMedia, "Compartiendo archivo."));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Whatsapp no se encuentra instalado", Toast.LENGTH_LONG).show();
        }
    }

    public String copyFiletoExternalStorage(int resourceId, String resourceName){
        String pathSDCard = Environment.getExternalStorageDirectory() + "/Android/data/" + resourceName;
        try{
            InputStream in = getResources().openRawResource(resourceId);
            FileOutputStream out = null;
            out = new FileOutputStream(pathSDCard);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  pathSDCard;
    }



    public void btnAplenoOnClink (View v)
    {
        playerApleno = MediaPlayer.create(this,R.raw.apleno);
        playerApleno.start();
    }
    public void btnExtremoOnClink (View v)
    {
        playerExtremo = MediaPlayer.create(this,R.raw.extremo);
        playerExtremo.start();
    }
    public void btnTanBuenaOnClink (View v)
    {
        playerTanBuena = MediaPlayer.create(this,R.raw.tanbuena);
        playerTanBuena.start();
    }
    public void btnRisaOnClink (View v)
    {
        playerRisa = MediaPlayer.create(this,R.raw.risa);
        playerRisa.start();
    }
    public void btnEnOnClink (View v)
    {
        playerRisa = MediaPlayer.create(this,R.raw.en);
        playerRisa.start();
    }
    public void btnElOnClink (View v)
    {
        playerRisa = MediaPlayer.create(this,R.raw.el);
        playerRisa.start();
    }
    public void btnSituaOnClink (View v)
    {
        playerRisa = MediaPlayer.create(this,R.raw.situa);
        playerRisa.start();
    }
    public void btnInfiernoOnClink (View v)
    {
        playerRisa = MediaPlayer.create(this,R.raw.infierno);
        playerRisa.start();
    }
    public void btnLariraOnClink (View v)
    {
        playerRisa = MediaPlayer.create(this,R.raw.larira);
        playerRisa.start();
    }
    public void btnSiOnClink (View v)
    {
        playerRisa = MediaPlayer.create(this,R.raw.si);
        playerRisa.start();
    }
    public void btnNoOnClink (View v)
    {
        playerRisa = MediaPlayer.create(this,R.raw.no);
        playerRisa.start();
    }
    public void btnDisneyOnClink (View v)
    {
        playerRisa = MediaPlayer.create(this,R.raw.disney);
        playerRisa.start();
    }
}
