package id.sisi.postoko.utils.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import id.sisi.postoko.R

fun FragmentManager.detach() {
    findFragmentById(R.id.main_container)?.also {
        beginTransaction().detach(it).commit()
    }
}

fun FragmentManager.remove(){
    findFragmentById(R.id.main_container)?.also {
        beginTransaction().remove(it).commit()
    }
}

fun FragmentManager.add(fragment: Fragment, tag: String){
    beginTransaction().add(R.id.main_container, fragment, tag).commit()
}

fun FragmentManager.attach(fragment: Fragment, tag: String) {
    if (fragment.isDetached) {
        beginTransaction().attach(fragment).commit()
    } else {
        beginTransaction().add(R.id.main_container, fragment, tag).commit()
    }
    // Set a transition animation for this transaction.
    //beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
}

fun FragmentManager.detachSearch() {
    findFragmentById(R.id.search_container)?.also {
        beginTransaction().detach(it).commit()
    }
}

fun FragmentManager.attachSearch(fragment: Fragment, tag: String) {
    if (fragment.isDetached) {
        beginTransaction().attach(fragment).commit()
    } else {
        beginTransaction().add(R.id.search_container, fragment, tag).commit()
    }
}