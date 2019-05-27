package com.rakesh.shop99.shop99admin.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rakesh.shop99.shop99admin.ImageUploadInfo;
import com.rakesh.shop99.shop99admin.R;
import com.rakesh.shop99.shop99admin.RecyclerViewAdapter;
import com.rakesh.shop99.shop99admin.adapter.ProductAdapter;
import com.rakesh.shop99.shop99admin.model.ProductModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    ProgressDialog progressDialog;
    List<ProductModel> products = new ArrayList<ProductModel>();
    RecyclerView recProducts;

    private ProductFragment.OnFragmentInteractionListener mListener;

    public ProductFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_product, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductModel productModel=new ProductModel();
                                productModel.setId(document.getId());
                                productModel.setName(document.getData().get("name").toString());
                                productModel.setDescription(document.getData().get("description").toString());
                                productModel.setImage(document.getData().get("image").toString());
                                productModel.setRegular_price(document.getData().get("regular_price").toString());
                                productModel.setSelling_price(document.getData().get("selling_price").toString());
                                products.add(productModel);
                            }
                            try{
                                recProducts=view.findViewById(R.id.rec_products);
                                recProducts.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                                ProductAdapter adapter=new ProductAdapter(getActivity().getApplicationContext(),products);
                                recProducts.setAdapter(adapter);

                                Toast.makeText(getActivity().getApplicationContext(),products.get(0).getName(),Toast.LENGTH_LONG).show();
                            }catch (Exception ex){
                                Toast.makeText(getActivity().getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),"Error getting documents.",Toast.LENGTH_LONG).show();
                        }
                    }
                });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProductFragment.OnFragmentInteractionListener) {
            mListener = (ProductFragment.OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
