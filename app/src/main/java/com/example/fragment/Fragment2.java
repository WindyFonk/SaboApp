package com.example.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.adapter.DetailAdapter;
import com.example.adapter.OrderAdapter;
import com.example.models.Orders;
import com.example.models.OrdersDetails;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Fragment2 extends Fragment {
    ListView listorder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    View view;
    TextView total;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab2,container,false);
        listorder = view.findViewById(R.id.listordersold);
        total= view.findViewById(R.id.Total);
        listorder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Orders order = (Orders) parent.getItemAtPosition(position);
                loadOrderDetails(order);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        loadData();
        super.onResume();
    }

    public void loadData(){
        db.collection("Orders")
                .whereEqualTo("status","Delivered")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Orders> list = new ArrayList<>();
                            Long totalprice = Long.valueOf(0);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String id= document.getId();
                                String userid= map.get("userid").toString();
                                userid = userid.replace("/AppUsers/","");
                                Timestamp time = (Timestamp) map.get("date");
                                String date = time.toDate().toString();
                                date = date.replace("GMT+07:00","");
                                String status = map.get("status").toString();
                                Long total = (Long) map.get("total");
                                Orders order =new Orders(id,userid,total,date,status);
                                list.add(order);
                                totalprice+=total;
                            }
                            OrderAdapter adapter = new OrderAdapter(list);
                            listorder.setAdapter(adapter);
                            total.setText("TOTAL: "+totalprice);
                            Log.d(">>>TAG TOTAL",""+totalprice);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void loadOrderDetails(Orders order){
        db.collection("OrdersDetails")
                .whereEqualTo("orderid","/Orders/"+order.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<OrdersDetails> detailslist = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                Long quantity = (Long) map.get("quantity");
                                String shoeid = map.get("shoeid").toString();
                                String orderid = map.get("orderid").toString();
                                String name = map.get("name").toString();
                                name  = name.replace("/Shoe/","");
                                String price = map.get("price").toString();
                                price  = price.replace("/Shoe/","");
                                String size = map.get("size").toString();
                                size  = size.replace("/Shoe/","");
                                String color = map.get("color").toString();
                                color  = color.replace("/Shoe/","");
                                String image = map.get("image").toString();
                                image  = image.replace("/Shoe/","");
                                OrdersDetails details = new OrdersDetails(orderid,shoeid,image,name,price,color,size,quantity);
                                detailslist.add(details);
                                String userid = order.getUserid();
                                userid=userid.replace("/AppUsers/","");
                                setDetailslist(detailslist, userid);
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //dialog showing bought items
    public void setDetailslist(ArrayList<OrdersDetails> list, String userid){
        AlertDialog alertDialog;
        LayoutInflater mLayoutInflater = getLayoutInflater();
        View view = mLayoutInflater.inflate(R.layout.dialog_ordersdetails_staff, null);

        TextView Name, Address, Phonenumber;
        Name = view.findViewById(R.id.customername);
        Address = view.findViewById(R.id.customeraddress);
        Phonenumber = view.findViewById(R.id.customerphone);

        DocumentReference docRef = db.collection("AppUsers").document(userid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //get and set user's info
                        String name = (String) document.get("name");
                        String address = (String) document.get("address");
                        String phonenumber = (String) document.get("phonenumber");
                        Name.setText(name);
                        Address.setText(address);
                        Phonenumber.setText(phonenumber);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        ListView lv =view.findViewById(R.id.listdetails);
        DetailAdapter adapter = new DetailAdapter(list);
        lv.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                .setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }

}
