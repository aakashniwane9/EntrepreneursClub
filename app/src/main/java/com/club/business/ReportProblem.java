package com.club.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.club.business.SendEmail;

public class ReportProblem extends AppCompatActivity {

    EditText subject,description;
    Button button;
    String s_subject,s_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);

        subject=(EditText)findViewById(R.id.problem_subject);
        description=(EditText)findViewById(R.id.problem_description);
        button=(Button)findViewById(R.id.problem_button);

            s_subject=subject.getText().toString();
            s_description=description.getText().toString();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        sendMessage();
                    }
                    catch (Exception e) {
                        Toast.makeText(ReportProblem.this,"Error Ocurred",Toast.LENGTH_SHORT).show();
                    }
                }
            });
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String s_subject=subject.getText().toString();
//                String s_description=description.getText().toString();
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_EMAIL,"omkar.raykar@spit.ac.in");
//                intent.putExtra(Intent.EXTRA_SUBJECT,s_subject);
//                intent.putExtra(Intent.EXTRA_TEXT,s_description);
//                intent.setType("message/rfc882");
//                startActivity(Intent.createChooser(intent,"Choose an email client: "));
//            }
        //});
    }

    private void sendMessage() {
        final ProgressDialog dialog = new ProgressDialog(ReportProblem.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SendEmail sender = new SendEmail("o.raykar96@gmail.com", "Question_Everything!");
                    sender.sendMail(s_subject,
                                    s_description,
                            "o.raykar96@gmail.com",
                            "o.raykar96@gmail.com");
                    dialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(ReportProblem.this,"Error Ocurred",Toast.LENGTH_SHORT).show();
                    Log.e("mylog", "Omkar Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }
}