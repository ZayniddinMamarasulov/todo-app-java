package uz.mamarasulov.todoappjava.util

import android.content.Context
import android.widget.Toast

/**
 *  Created by Zayniddin on Sep 09 - 11:02
 */
fun toastMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}