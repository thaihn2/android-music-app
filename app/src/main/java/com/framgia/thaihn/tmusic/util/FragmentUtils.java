package com.framgia.thaihn.tmusic.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public final class FragmentUtils {

    public static boolean checkFragmentStackExist(AppCompatActivity context, String stack) {
        ArrayList<String> listFragmentStack = getAllFragmentStackName(context);
        for (int i = 0; i < listFragmentStack.size(); i++) {
            if (stack.equalsIgnoreCase(listFragmentStack.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getAllFragmentStackName(AppCompatActivity context) {
        ArrayList<String> listFragment = new ArrayList<>();
        FragmentManager fragmentmanager = getFragmentManager(context);
        if (fragmentmanager.getBackStackEntryCount() >= 1) {
            for (int i = 0; i < fragmentmanager.getBackStackEntryCount(); i++) {
                listFragment.add(fragmentmanager.getBackStackEntryAt(i).getName());
            }
        }
        return listFragment;
    }

    public static String getTopFragmentStack(AppCompatActivity context) {
        ArrayList<String> listFragmentStack = getAllFragmentStackName(context);
        if (countFragmentStack(context) > 1 && listFragmentStack.size() > 1) {
            int index = countFragmentStack(context) - 1;
            return listFragmentStack.get(index);
        }
        return "";
    }

    public static int countFragmentStack(AppCompatActivity context) {
        return getFragmentManager(context).getBackStackEntryCount();
    }

    public static void replaceFragment(
            AppCompatActivity context,
            Fragment fragment,
            int idFrameLayout,
            String stack) {
        getFragmentTransaction(context)
                .replace(idFrameLayout, fragment)
                .addToBackStack(stack)
                .commit();
    }

    public static void replaceFragmentNotStack(
            AppCompatActivity context,
            Fragment fragment,
            int idFrameLayout) {
        getFragmentTransaction(context)
                .replace(idFrameLayout, fragment)
                .commit();
    }

    public static void addFragment(
            AppCompatActivity context,
            Fragment fragment,
            int idFrameLayout,
            String stack) {
        getFragmentTransaction(context)
                .add(idFrameLayout, fragment)
                .addToBackStack(stack)
                .commit();
    }

    public static void addFragmentNotStack(
            AppCompatActivity context,
            Fragment fragment,
            int idFrameLayout) {
        getFragmentTransaction(context)
                .add(idFrameLayout, fragment)
                .commit();
    }

    public static void popFragmentStack(AppCompatActivity context, String name) {
        getFragmentManager(context).popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void popFragmentStack(AppCompatActivity context) {
        getFragmentManager(context).popBackStack();
    }

    private static FragmentTransaction getFragmentTransaction(AppCompatActivity context) {
        FragmentTransaction fragmentTransaction
                = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        return fragmentTransaction;
    }

    private static FragmentManager getFragmentManager(AppCompatActivity context) {
        return context.getSupportFragmentManager();
    }

    private FragmentUtils() {
    }
}
