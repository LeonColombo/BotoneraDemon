package com.example.itmaster.botonerdemon;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity
{
    private Button btnApleno, btnExtremo, btnTanBuena, btnRisa;
    private MediaPlayer playerApleno, playerExtremo, playerTanBuena, playerRisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnApleno = findViewById(R.id.btnApleno);
        btnExtremo = findViewById(R.id.btnExtremo);
        btnTanBuena = findViewById(R.id.btnTanBuena);
        btnRisa = findViewById(R.id.btnRisa);

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

}
