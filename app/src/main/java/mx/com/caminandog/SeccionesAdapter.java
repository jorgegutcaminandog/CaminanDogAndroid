package mx.com.caminandog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SeccionesAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> listaFragments=new ArrayList<>();
    private final List<String> listaTitulos=new ArrayList<>();

    public SeccionesAdapter(FragmentManager fm) {
        super(fm);
    }



    public void addFragment(Fragment fragment, String titulo){
        listaFragments.add(fragment);
        listaTitulos.add(titulo);

    }


    @Override
    public CharSequence getPageTitle(int position) {
        return listaTitulos.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return listaFragments.get(position);
    }

    @Override
    public int getCount() {
        return listaFragments.size();
    }
}
