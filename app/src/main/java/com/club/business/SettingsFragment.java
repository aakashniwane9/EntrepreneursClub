package com.club.business;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    RelativeLayout relativeLayout;
    TextView Invite_Contacts,Change_Password,ReportProblem,logout;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(R.id.setting_relative_layout);
        Invite_Contacts = (TextView) v.findViewById(R.id.frag_settings_contact);
        ReportProblem=(TextView) v.findViewById(R.id.frag_settings_problem);
        Change_Password = (TextView) v.findViewById(R.id.frag_settings_password);


        mAuth = FirebaseAuth.getInstance();

        ReportProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), com.club.business.ReportProblem.class));
            }
        });


        logout = v.findViewById(R.id.frag_settings_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("See you soon... ");
                pd.show();


                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });



        Invite_Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plan");
                String shareSub = "App Contact Invite";
                String shareBody = "Download Entrepreneur Club\nDeveloped by:\n" +
                        "Omkar Raykar\n" +
                        "(SYMCA, SPIT)\n" +
                        "\n\nDownload the app's .apk file using the link given below:\n" +
                        "https://bit.ly/2UTc9eG";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using: "));
            }
        });

        Change_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getActivity(),ChangePassword.class));
            }
        });
        return v;
    }
}
