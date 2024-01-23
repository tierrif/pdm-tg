package com.example.pdm_tg

import androidx.fragment.app.Fragment
import kotlinx.coroutines.Job

/**
 * An abstract class that is meant to
 * be inherited by fragments that are
 * creation forms for a certain entity
 * in this application.
 *
 * The other fragments that inherit from
 * those creation form fragments are those
 * responsible for editing/deleting the
 * respective entity. Since not much code
 * changes and the layout can be recycled,
 * this class has been made.
 */
abstract class InheritableFragment<T> : Fragment() {
    // If this is set to true by an inherited class, insertion won't happen but a method call.
    protected var isDetails = false

    /**
     * Method to call when fields are
     * ready to be filled with data.
     */
    protected abstract fun fillFields()

    /**
     * To call when the data is ready
     * to be saved according to the
     * user's behaviour. T is the type
     * of the entity for this fragment
     * and the returning Job is what is
     * returned by a coroutine that updates
     * the entity's information in the database.
     *
     * @param t The entity's object to update/save.
     * @return The job that is returned by the coroutine
     *         that interacts with the database.
     */
    protected abstract fun onSave(t: T): Job
}
