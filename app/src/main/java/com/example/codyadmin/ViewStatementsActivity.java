package com.example.codyadmin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.codyadmin.Model.Statement;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewStatementsActivity extends AppCompatActivity {

    private ListView listViewStatements;
    private Button buttonDeleteSelected;
    private ArrayAdapter<String> adapter;
    private List<Statement> statementList = new ArrayList<>();
    private List<String> statementDescriptions = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statements);

        listViewStatements = findViewById(R.id.listViewStatements);
        buttonDeleteSelected = findViewById(R.id.buttonDeleteSelected);
        db = FirebaseFirestore.getInstance();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, statementDescriptions);
        listViewStatements.setAdapter(adapter);
        listViewStatements.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        buttonDeleteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedStatements();
            }
        });

        fetchStatementsFromFirestore();
    }

    private void fetchStatementsFromFirestore() {
        db.collection("statements").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Statement statement = document.toObject(Statement.class);
                        if (statement != null) {
                            statementList.add(statement);
                            statementDescriptions.add(statement.getPictureName());
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            } else {
                Log.e("ViewStatementsActivity", "Error getting documents: ", task.getException());
            }
        });
    }

    private void deleteSelectedStatements() {
        long[] checkedItemIds = listViewStatements.getCheckItemIds();
        List<Statement> statementsToDelete = new ArrayList<>();
        for (int i = 0; i < checkedItemIds.length; i++) {
            int position = (int) checkedItemIds[i];
            statementsToDelete.add(statementList.get(position));
        }

        for (Statement statement : statementsToDelete) {
            db.collection("statements")
                    .whereEqualTo("description", statement.getDescription())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                db.collection("statements").document(document.getId()).delete()
                                        .addOnSuccessListener(aVoid -> {
                                            statementList.remove(statement);
                                            statementDescriptions.remove(statement.getDescription());
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(ViewStatementsActivity.this, "Statement deleted", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(ViewStatementsActivity.this, "Failed to delete statement", Toast.LENGTH_SHORT).show();
                                            Log.e("ViewStatementsActivity", "Error deleting document", e);
                                        });
                            }
                        }
                    });
        }
    }
}
