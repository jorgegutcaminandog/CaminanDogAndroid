    package mx.com.caminandog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Locale;

    /**
 * A simple {@link Fragment} subclass.
 * Use the {@link AliadosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AliadosFragment extends Fragment {

        private RecyclerView mPeopleRV;
        //private DatabaseReference mDatabase;
        private FirebaseRecyclerAdapter<Aliados, AliadosFragment.CardsViewHolder> mPeopleRVAdapter;
        //private AliadosFragment.OnFragmentInteractionListener mListener;
        private LinearLayoutManager mLayoutManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AliadosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AliadosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AliadosFragment newInstance(String param1, String param2) {
        AliadosFragment fragment = new AliadosFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_aliados, container, false);

        mPeopleRV = (RecyclerView) root.findViewById(R.id.rec_aliados);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Aliados");

        Query personsQuery = personsRef.orderByChild("numero").startAt(1);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);
        mPeopleRV.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Aliados>().setQuery(personsQuery, Aliados.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Aliados, AliadosFragment.CardsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(AliadosFragment.CardsViewHolder holder, final int position, final Aliados model) {
                //progressBar.setVisibility(View.GONE);
                holder.setTitle(model.getNombre());
                holder.setImage(getActivity().getBaseContext(),model.getFoto());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = model.getUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });


            }

            @Override
            public AliadosFragment.CardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.elemento_aliados, parent, false);
                return  new AliadosFragment.CardsViewHolder(view);
            }



        };
        mPeopleRV.setAdapter(mPeopleRVAdapter);

        return root;
    }

        @Override
        public void onStart() {
            super.onStart();
            mPeopleRVAdapter.startListening();
        }
        @Override
        public void onStop() {
            super.onStop();
            mPeopleRVAdapter.stopListening();
        }
        public static class CardsViewHolder extends RecyclerView.ViewHolder{

            View mView;
            public CardsViewHolder(View itemView){
                super(itemView);
                mView = itemView;
            }
            public void setTitle(String title2){
                TextView post_title2 = (TextView)mView.findViewById(R.id.title_aliados);
                post_title2.setText(title2);
            }
            public void setImage(Context ctx, String image){
                ImageView post_image = (ImageView) mView.findViewById(R.id.img_aliados);
                Glide.with(ctx).load(image).into(post_image);
            }










        }
}