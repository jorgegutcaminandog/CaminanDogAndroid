package mx.com.caminandog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Contenedor_Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    View vista;
    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;

    public Contenedor_Fragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_contenedor_, container, false);

        if (Utilidades.rotacion == 0) {
            View parent = (View) container.getParent();

            if (appBar == null) {
                appBar = parent.findViewById(R.id.appBar);
                //pestanas = new TabLayout(getActivity());
                pestanas = (TabLayout) vista.findViewById(R.id.sliding_tabs);
                //appBar.addView(pestanas);

                viewPager = (ViewPager) vista.findViewById(R.id.Viewpagerinformacion);
                llenarViewPager(viewPager);
                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
                pestanas.setupWithViewPager(viewPager);






            }

        }else {Utilidades.rotacion=1;}


        return vista;
    }

    private void llenarViewPager(ViewPager viewPager) {
        SeccionesAdapter adapter = new SeccionesAdapter(getFragmentManager());
        adapter.addFragment(new No_edit_Account_Frgmnt(),getResources().getString(R.string.Cuenta));
        adapter.addFragment(new Historic_Paseo_Fragmnt(),getString(R.string.Mis_paseos));
        //adapter.addFragment(new Historico(),"historico");


        viewPager.setAdapter(adapter);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Utilidades.rotacion == 0){
            appBar.removeView(pestanas);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
