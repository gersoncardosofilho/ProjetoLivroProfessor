package br.com.grupoccaa.projetolivroprofessor.helper_classes;

import android.content.pm.PackageManager;

/**
 * Created by gerso on 1/7/2018.
 */

public abstract class PermissionsUtil{

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }


        // Verify that each required  permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
