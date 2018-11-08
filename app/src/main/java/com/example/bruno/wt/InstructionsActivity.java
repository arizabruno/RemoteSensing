package com.example.bruno.wt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class InstructionsActivity extends AppCompatActivity {

    PDFView instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        instructions = findViewById(R.id.instructions);
        instructions.fromAsset("cad4.pdf").load();

    }
}
