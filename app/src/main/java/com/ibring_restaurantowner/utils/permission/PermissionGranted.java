package com.ibring_restaurantowner.utils.permission;

import java.util.ArrayList;

public interface PermissionGranted
{
    void showPermissionAlert(ArrayList<String> permissionList, int code);
}
