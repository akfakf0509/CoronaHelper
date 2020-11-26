package com.dongkyan.coronahelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Third_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Third_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Third_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Third_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Third_Fragment newInstance(String param1, String param2) {
        Third_Fragment fragment = new Third_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private static final int RC_SIGN_IN = 8001;

    private GoogleSignInClient googleSignInClient;

    private FirebaseFirestore db;

    Map<String, Object> user = new HashMap<>();

    private String email = "";
    private String family = "none";

    private Context context;

    ChipGroup q1;
    ChipGroup q2;
    ChipGroup q3;
    ChipGroup q4;
    ChipGroup q5;

    EditText family_id_input;

    TextView family_id;

    Button commit_button;
    Button self_check_button;
    Button join_family_button;

    FrameLayout login;
    LinearLayout self_check_linear;
    FrameLayout join_family;
    FrameLayout records;
    LinearLayout records_list;
    ScrollView self_check;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third_, container, false);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), googleSignInOptions);

        v.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        db = FirebaseFirestore.getInstance();

        context = container.getContext();

        q1 = v.findViewById(R.id.q1_chipGroup);
        q2 = v.findViewById(R.id.q2_chipGroup);
        q3 = v.findViewById(R.id.q3_chipGroup);
        q4 = v.findViewById(R.id.q4_chipGroup);
        q5 = v.findViewById(R.id.q5_chipGroup);

        family_id_input = v.findViewById(R.id.family_id_input);

        family_id = v.findViewById(R.id.family_id);

        commit_button = v.findViewById(R.id.commit_button);
        self_check_button = v.findViewById(R.id.self_check_button);
        join_family_button = v.findViewById(R.id.join_family_button);

        login = v.findViewById(R.id.login);
        self_check_linear = v.findViewById(R.id.self_check_linear);
        join_family = v.findViewById(R.id.join_family);
        records = v.findViewById(R.id.records);
        records_list = v.findViewById(R.id.records_list);
        self_check = v.findViewById(R.id.self_check);

        commit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List tmp = new ArrayList();

                tmp.add(R.id.q2_chip1);

                if (q1.getCheckedChipId() == -1 || q2.getCheckedChipIds().isEmpty() || q3.getCheckedChipId() == -1 || q4.getCheckedChipId() == -1 || q5.getCheckedChipId() == -1) {
                    Toast.makeText(context, "체크 하지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> diagnosis = new HashMap<>();

                    if (q1.getCheckedChipId() != R.id.q1_chip1 || q2.getCheckedChipIds().retainAll(tmp) || q3.getCheckedChipId() != R.id.q3_chip1 || q4.getCheckedChipId() != R.id.q4_chip1 || q5.getCheckedChipId() != R.id.q5_chip1) {
                        Toast.makeText(context, "정밀한 검사를 받는걸 추천 드립니다.", Toast.LENGTH_SHORT).show();

                        diagnosis.put("warn", true);
                    } else {
                        diagnosis.put("warn", false);
                    }

                    diagnosis.put("user", email);
                    diagnosis.put("date", Calendar.getInstance().getTime());

                    db.collection("diagnosises")
                            .add(diagnosis);

                    self_check.setVisibility(View.GONE);
                    self_check_button.setVisibility(View.VISIBLE);
                    join_family.setVisibility(View.VISIBLE);
                    records.setVisibility(View.VISIBLE);
                }

                updateDiagnosisRecords();
            }
        });

        self_check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                self_check.setVisibility(View.VISIBLE);
                self_check_button.setVisibility(View.GONE);
                join_family.setVisibility(View.GONE);
                records.setVisibility(View.GONE);
            }
        });

        join_family_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                family = family_id_input.getText().toString();

                db.collection("users").document(email)
                        .update("family", family);

                db.collection("familys").document(family)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        ArrayList<String> members = (ArrayList<String>) document.get("members");

                                        if (!members.contains(email)) {
                                            members.add(email);

                                            db.collection("familys").document(family)
                                                    .update("members", members);
                                        }
                                    } else {
                                        ArrayList<String> members = new ArrayList<>();
                                        members.add(email);

                                        Map<String, Object> data = new HashMap<>();
                                        data.put("members", members);

                                        db.collection("familys").document(family)
                                                .set(data);
                                    }
                                }
                            }
                        });

                family_id.setText(family);

                updateDiagnosisRecords();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (GoogleSignIn.getLastSignedInAccount(context) != null) {
            email = GoogleSignIn.getLastSignedInAccount(context).getEmail();
            Log.d("GoogleLogin", "Email : " + email);
            doLoginSuccess();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            task.addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                @Override
                public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                    email = googleSignInAccount.getEmail();
                    Log.d("GoogleLogin", "Email : " + email);
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("GoogleLogin", "Exception : " + e);
                }
            });
            task.addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
                @Override
                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                    doLoginSuccess();
                }
            });
        }
    }

    private void doLoginSuccess() {
        login.setVisibility(View.GONE);
        self_check_linear.setVisibility(View.VISIBLE);
        join_family.setVisibility(View.VISIBLE);
        records.setVisibility(View.VISIBLE);

        Log.d("Email", ""+ email);

        db.collection("users").document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                family = task.getResult().get("family").toString();

                                family_id.setText(family);

                                Log.d("DB", "" + family);
                            } else {
                                user.put("family", "none");

                                db.collection("users").document(email)
                                        .set(user);
                            }

                            updateDiagnosisRecords();
                        } else {
                            Log.e("DB", "" + task.getException());
                        }
                    }
                });
    }

    private void updateDiagnosisRecords(){
        records_list.removeAllViews();

        db.collection("familys").document(family)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<String> members = (ArrayList<String>) document.get("members");

                                for(final String member : members){
                                    db.collection("diagnosises").whereEqualTo("user", member).orderBy("date", Query.Direction.DESCENDING).limit(1)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        QuerySnapshot query = task.getResult();

                                                        for(DocumentSnapshot document : query.getDocuments()){
                                                            if(document.exists()){
                                                                String message;

                                                                if((boolean)document.get("warn")){
                                                                    message = "정밀한 검사를 추천합니다";
                                                                }
                                                                else{
                                                                    message ="괜찮습니다! 앞으로도 조심해주세요";
                                                                }

                                                                Diagnosis_record record = new Diagnosis_record(context, member, message);
                                                                records_list.addView(record);

                                                                Log.d("DB", "Added New Record : " + member + " " + message);
                                                            }
                                                            else{
                                                                Log.e("DB", "Can not find Diagnosis record : " + member);
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        Log.e("DB", "Member Diagnosis Document Load Error : " + task.getException());
                                                    }
                                                }
                                            });
                                }
                            }
                            else{
                                Log.e("DB", "Can not find Family Document : " + family);

                                ArrayList<String> members = new ArrayList<>();
                                members.add(email);

                                Map<String, Object> data = new HashMap<>();
                                data.put("members", members);

                                db.collection("familys").document(family)
                                        .set(data);
                            }
                        }
                        else{
                            Log.e("DB", "Family Document Load Error : " + task.getException());
                        }
                    }
                });
    }
}