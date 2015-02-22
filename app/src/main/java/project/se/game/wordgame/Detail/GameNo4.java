package project.se.game.wordgame.Detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.se.talktodeaf.R;

/**
 * Created by wiwat on 2/22/2015.
 */
public class GameNo4 extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static GameNo4 newInstance(int position) {
        GameNo4 f = new GameNo4();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_word_game, container, false);
        rootView.findViewById(R.id.linear_layout);

        return rootView;
    }

}


