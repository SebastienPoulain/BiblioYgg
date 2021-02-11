package com.annequinpoulain.biblioygg;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class loginDialog extends AppCompatDialogFragment{
    private EditText etMail, etPass, etPass2, etNom, etPrenom,etTel;
    private LoginDialogListener listener;
    AlertDialog.Builder builder;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.login_dialog, null);

        etTel = view.findViewById(R.id.tel);
        etMail = view.findViewById(R.id.email);
        etPass = view.findViewById(R.id.password);
        etPass2 = view.findViewById(R.id.password2);
        etNom = view.findViewById(R.id.nom);
        etPrenom = view.findViewById(R.id.prenom);

        etTel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        SingletonSession.Instance().setLoginDialogConnexion(true);

        builder.setView(view)
                .setTitle("Connexion")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNeutralButton("Inscription", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        return builder.create();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            final Button neutralButton = (Button) d.getButton(Dialog.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SingletonSession.Instance().setLoginDialogConnexion(false);
                    d.setTitle("Inscription");
                    etNom.setVisibility(View.VISIBLE);
                    etPrenom.setVisibility(View.VISIBLE);
                    etTel.setVisibility(View.VISIBLE);
                    etPass2.setVisibility(View.VISIBLE);
                    neutralButton.setVisibility(View.GONE);
                }
            });

            final Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String nom = etNom.getText().toString();
                    String prenom = etPrenom.getText().toString();
                    String mail = etMail.getText().toString();
                    String tel = etTel.getText().toString();
                    String password = etPass.getText().toString();
                    String password2 = etPass2.getText().toString();

                    if (!SingletonSession.Instance().getLoginDialogConnexion()){
                        if (!nom.trim().equals("") && !prenom.trim().equals("") && !mail.trim().equals("") && SingletonSession.Instance().isValidEmail(mail.trim()) && !tel.trim().equals("") && !password.equals("") && !SingletonSession.Instance().userExists(mail) && password.equals(password2)) {
                            listener.login(nom, prenom, mail,tel, password, password2);
                            d.setTitle("Connexion");
                            etNom.setVisibility(View.GONE);
                            etPrenom.setVisibility(View.GONE);
                            etTel.setVisibility(View.GONE);
                            etPass2.setVisibility(View.GONE);
                            neutralButton.setVisibility(View.VISIBLE);
                            etPass.setText("");
                            etPass2.setText("");
                            etNom.setText("");
                            etPrenom.setText("");
                            SingletonSession.Instance().setLoginDialogConnexion(true);
                      }
                        else{
                            listener.login(nom, prenom, mail,tel, password, password2);
                        }
                    }
                    else {
                        listener.login(nom, prenom, mail,tel,password, password2);
                        dismiss();
                    }


                }

            });


        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (LoginDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " doit implementer LoginDialogListener");
        }
    }

    public interface LoginDialogListener{
        void login(String nom, String prenom, String email,String tel, String password, String password2);
    }
}
