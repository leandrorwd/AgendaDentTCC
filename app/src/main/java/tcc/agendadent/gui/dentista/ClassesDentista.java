package tcc.agendadent.gui.dentista;

import android.content.Intent;

/**
 * Created by natha on 23/03/2017.
 */

public interface ClassesDentista {
    void onResume();
    boolean needResume();
    int getIdMenu();
    void flipper(boolean next);
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
