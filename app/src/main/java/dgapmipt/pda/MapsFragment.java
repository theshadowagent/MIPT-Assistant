package dgapmipt.pda;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapsFragment extends Fragment {
    private FloatingActionButton fabPlus;
    private FloatingActionButton fabMinus;
    private TouchImageView map;


    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, // like an onCreate method in Activities
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScanActivity.class);
                startActivity(intent);
            }
        });

        map = (TouchImageView) rootView.findViewById(R.id.mapImage);
        fabPlus = (FloatingActionButton) rootView.findViewById(R.id.fabPlus);
        fabMinus = (FloatingActionButton) rootView.findViewById(R.id.fabMinus);
        map.setMaxZoom(3);
        map.setMinZoom(1);
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               map.setZoom(1.5f*map.getCurrentZoom());
            }
        });
        fabMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setZoom(map.getMinZoom());
            }
        });

        return rootView;
    }
}
