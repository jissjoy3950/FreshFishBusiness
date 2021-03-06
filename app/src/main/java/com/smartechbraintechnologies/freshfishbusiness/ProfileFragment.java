package com.smartechbraintechnologies.freshfishbusiness;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private TextView name, number, id, email, editBTN, logOutBTN;
    private TextView addressBTN, pastOrdersBTN, helpBTN, constactUsBTN, tosBTN, privacyBTN;
    private ProgressDialog mProgress;


    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DocumentReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initValues(view);

        loadData();

        logOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        return view;

    }

    private void confirm() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pop_up_logout);
        Window window = dialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ExtendedFloatingActionButton confirm_btn = (ExtendedFloatingActionButton) dialog.findViewById(R.id.logout_confirm);
        ExtendedFloatingActionButton cancel_btn = (ExtendedFloatingActionButton) dialog.findViewById(R.id.logout_cancel);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), AuthenticationBridgeActivity.class));
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }

    private void loadData() {
        mProgress.setMessage("Please wait...");
        mProgress.show();
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                name.setText(documentSnapshot.getString("sellerName"));
                number.setText(documentSnapshot.getString("sellerPhone"));
                email.setText(documentSnapshot.getString("sellerEmail"));
                id.setText("ID: " + documentSnapshot.getString("sellerID"));
                mProgress.dismiss();
            }
        });
    }

    private void initValues(View view) {
        name = (TextView) view.findViewById(R.id.profile_name);
        number = (TextView) view.findViewById(R.id.profile_number);
        email = (TextView) view.findViewById(R.id.profile_email);
        id = (TextView) view.findViewById(R.id.profile_user_id);
        editBTN = (TextView) view.findViewById(R.id.profile_edit_btn);
        addressBTN = (TextView) view.findViewById(R.id.profile_address_btn);
        pastOrdersBTN = (TextView) view.findViewById(R.id.profile_past_orders_btn);
        helpBTN = (TextView) view.findViewById(R.id.profile_help_btn);
        constactUsBTN = (TextView) view.findViewById(R.id.profile_contact_us_btn);
        tosBTN = (TextView) view.findViewById(R.id.profile_tos_btn);
        privacyBTN = (TextView) view.findViewById(R.id.profile_privacy_btn);
        logOutBTN = (TextView) view.findViewById(R.id.profile_log_out_btn);
        mProgress = new ProgressDialog(getContext());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userRef = db.collection("Sellers").document(currentUser.getUid());
    }
}
